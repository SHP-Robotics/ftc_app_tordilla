package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import static com.qualcomm.robotcore.hardware.DistanceSensor.distanceOutOfRange;
// Created  for 16887 by Paul
// Edited for whoever by Jonathon Tordilla
@Autonomous(name="BlueSkyStone", group="AUTO")
//@Disabled
public class AutoColor extends Base {
    private int step = 0;
    private double distance_mec1 = 0.0;     // distance moved to find the 1st black skystone
    //    private double distance_mec2 = 0.0;     // distance moved to find the 2nd black skystone
    private boolean found_stone1 = false;   // Found 1st black skystone
    //    private boolean found_stone2 = false;   // Found 2nd black skystone
    private double detected_dist;           // Distance detected by the sensor in CM
    private boolean touches = false;// Almost touching the skystone

    @Override
    public void init() {
        super.init();
    }
    @Override
    public void start() { super.start(); }
    @Override
    public void loop() {

        switch (step) {
            case 0:

                detected_dist = distance_sensor.getDistance(DistanceUnit.CM);

                if (!(detected_dist == distanceOutOfRange)) {
                    touches = detected_dist < 25.0;
                }

                if (drive(0.5, 70.0) || touches) {
                    reset_drive();
                    step++;
                }
                break;

            case 1:
                found_stone1 = is_black(color_sensor.red(),color_sensor.blue());
                if (strafe(0.5, 60.0) || found_stone1) {
                    if (found_stone1) {
                        strafe(1.0, 60.0 + 2.0);  // Strafe from the edge to the center of the black skystone
                    } else {                        // Did not find a black skystone; Just pick up any skystone

                    }
                    distance_mec1 = get_rf() / ConstantVariables.K_PPIN_DRIVE;
                    reset_drive();
                    step++;
                }
                break;
            case 2:
                if (drive(0.1, 2.0)) {
                    reset_drive();
                    step++;
                }
            case 3:
                //does nothing
                step++;
                break;
            case 4:         // Close Servos: push back the two stones on the two sides and pick up the middle black stone
                if (close()) step++;
                break;
            case 5:         // Raise rack 3 inches: the opposite of drop
                if (move_arm(0.5, 3)) {
                    reset_arm();
                    step++;
                }
                break;
            case 6:         // Back 20 inches
                if (drive(-1.0, 40.0)) {
                    reset_drive();
                    step++;
                }
                break;
            case 7:         // Move LEFT 30 inches + distance moved to find the black skystone (or turn right and go straight)
                if (strafe(-1.0, (100.0 + distance_mec1))) {    // Strafe right
                    reset_drive();
                    step++;
                }
                break;
            case 8:         // Open Servos to drop the skystone
                if (open()){
                    step++;
                }
                break;

            case 9:
                if(move_arm(-0.5, 3)){
                    reset_arm();
                    step++;
                }
                break;

            case 10:
                if(drive(-0.5, 10)){
                    reset_drive();
                    step++;
                }
                break;

            case 11:
                if(move_arm(0.5, 3)){
                    reset_arm();
                    step++;
                }
                break;

            case 12:
                if(drive(-0.5, 10)){
                    reset_drive();
                    step++;
                }
                break;

            case 13:         // Move RIGHT 15 inch to Park
                if (strafe(1.0, 80.0)) {   // Strafe RIGHT
                    reset_drive();
                    color_sensor.enableLed(false);              // Turn off LED
                    step++;
                }
                break;

            case 14:
                if(move_arm(-0.5, 3)){
                    reset_arm();
                    step++;
                }
                break;

            default:
                break;
        }
        super.loop();
    }
}