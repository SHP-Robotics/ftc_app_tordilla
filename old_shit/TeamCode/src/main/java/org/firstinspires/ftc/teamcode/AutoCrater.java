//needs testing

package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous
public class AutoCrater extends Base {
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

        left_claw.setPosition(left_grab);
        right_claw.setPosition(right_grab);

        //detector.enable();
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

                if(Math.abs(get_climb_enc())> 4250){
                    detector.enable();
                    climb(0);
                    timer.reset();
                    stage ++;
                }else{
                    climb(1);
                }

                break;

            case 1:

                if(timer.seconds() < 3){
                    stop_all();
                }
                else{
                    stage++;
                }

                break;

            case 2:

                if(auto_drive(0.2, 1)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 3:

                if(detector.getAligned()){
                    stage +=4;
                }
                else{
                    stage++;
                }

                break;

                //turning

            case 4:

                if(detector.getXPosition() > 280){

                    direction = 1;

                    if(detector.getAligned()){
                        stage+=2;
                    }
                    else if(auto_turn(0.4, 5)){ //right
                        reset_drive_encoders();
                    }
                }
                else{
                    stage++;
                }

                break;

            case 5:

                if(detector.getXPosition() < 280) {

                    direction = 2;

                    if(detector.getAligned()){
                        stage+=2;
                    }
                    else if (auto_turn(-0.4, -5)) { //left
                        reset_drive_encoders();
                    }
                }
                else{
                    stage++;
                }

                break;

                //drive sequence setup

            case 6:

                if(auto_turn(0.4, 10)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 7:

                if(auto_drive(0.7, 23)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

                //check direction

            case 8:

                if(direction == 0){
                    stage++;
                }
                else if(direction == 1){
                    stage +=5;
                }
                else if(direction == 2){
                    stage += 8;
                }
                else{
                    stage++;
                }

                break;

                //default - change units

            case 9:

                if(auto_drive(-0.7, 23)){ //back
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 10:

                if(auto_turn(-0.4, 90)){ //left
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 11:

                if(auto_drive(0.8, 48)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 12:

                if(auto_turn(-0.4, 45)){ //left
                    reset_drive_encoders();
                    stage+=7;//to end
                }

                break;

            //right - 120 - 35d - 70

            case 13:

                if(auto_turn(-0.4, 120)){ //left - change
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 14:

                if(auto_drive(0.8, 35)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 15:

                if(auto_turn(-0.4, 70)){ //left
                    reset_drive_encoders();
                    stage+=4; //to end
                }

                break;

            //left - 20d - 20 - 90d

            case 16:

                if(auto_turn(-0.4, 20)){ //left
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 17:

                if(auto_drive(0.8, 20)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 18:

                if(auto_turn(-0.4, 90)){ //left
                    reset_drive_encoders();
                    stage++;
                }

                break;

            //end

            case 19:

                if(auto_drive(0.6, 50)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 20:

                if(auto_turn(0.4, 90)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 21:

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
