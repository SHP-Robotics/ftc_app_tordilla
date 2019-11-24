//final test

package org.firstinspires.ftc.teamcode;
import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class Depot extends Base {
    private int stage = 0;
    private GoldAlignDetector detector;

    private int direction = 0;

    @Override
    public void init(){
        super.init();
        telemetry.addData("Status", "DogeCV 2018.0 - Gold Align Example");
        //set_marker_servo(ConstantVariables.K_MARKER_SERVO_UP);
        detector = new GoldAlignDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();
        // Optional Tuning
        detector.alignSize = 50; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        detector.downscale = 0.4; // How much to downscale the input frames
        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005;
        detector.ratioScorer.weight = 5;
        detector.ratioScorer.perfectRatio = 1.0;
        marker_servo.setPosition(up_position);
        detector.enable();
    }

    @Override
    public void start(){
        super.start();
    }

    @Override
    public void loop(){
        super.loop();
        telemetry.addData("IsAligned", detector.getAligned());
        telemetry.addData("XPos", detector.getXPosition());

        switch(stage){

            case 0:

                left_claw.setPosition(left_grab);
                right_claw.setPosition(right_grab);
                stage++;
                break;

            case 1:

                if(Math.abs(get_climb_enc())> 4250){

                    climb(0);
                    timer.reset();
                    stage ++;

                }else{
                    climb(1);
                }

                break;
            //drive sequence setup

            case 2:

                if(auto_drive(0.4, 5)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 3:

                if(Math.abs(get_climb_enc())< 1500){
                    climb(0);
                    timer.reset();
                    stage++;
                }else{
                    climb(-1);
                }

                break;

            case 4:

                if(auto_drive(0.4, 60)){ //forward
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 5:

                marker_servo.setPosition(drop_position);
                stage++;
                break;

            default:

                break;
        }
    }
    @Override
    public void stop(){
        detector.disable();
    }
}
