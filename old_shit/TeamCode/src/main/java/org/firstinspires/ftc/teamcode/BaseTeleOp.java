package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

//created by jonathon for 13981

@TeleOp
public class BaseTeleOp extends Base {

    @Override
    public void init(){

        super.init();

    }

    @Override
    public void start(){

        super.start();
    }

    @Override
    public void loop(){
        super.loop();

        if(gamepad1.x){
            marker_servo.setPosition(drop_position);
        }else if(gamepad1.y){
            marker_servo.setPosition(up_position);
        }

        //climber

        if(gamepad1.dpad_up) {
            climb(0.5);
        } else if (gamepad1.dpad_down) {
            climb(-0.5);
        } else {
            climb(0);
        }

        if(gamepad1.left_bumper){
            extend(0.5);
        } else if(gamepad1.right_bumper){
            extend(-0.5);
        }else{
            extend(0);
        }

        if(gamepad1.b){
            grab();
        }else if(gamepad1.a){
            ungrab();
        }

        //drive train

        left = this.gamepad1.left_stick_y;
        right = -this.gamepad1.right_stick_y;

        leftBack.setPower(left);
        rightBack.setPower(right);
        leftFront.setPower(left);
        rightFront.setPower(right);

        if (gamepad1.right_trigger > 0.7 && gamepad1.right_trigger < 0.9) {

            intakeMove.setPower(0.8);
            marker_servo.setPosition(drop_position);

        }
        else if (gamepad1.left_trigger > 0.7 && gamepad1.left_trigger < 0.9) {

            intakeMove.setPower(-0.8);
            marker_servo.setPosition(up_position);

        }
        else if(gamepad1.right_trigger > 0.3 && gamepad1.right_trigger < 0.7){

            intakeMove.setPower(0.4);
            marker_servo.setPosition(drop_position);

        }
        else if(gamepad1.left_trigger > 0.3 && gamepad1.left_trigger < 0.7){

            intakeMove.setPower(-0.4);
            marker_servo.setPosition(up_position);

        }
        else if(gamepad1.right_trigger > 0.1 && gamepad1.right_trigger < 0.3){

            intakeMove.setPower(0.2);
            marker_servo.setPosition(drop_position);

        }
        else if(gamepad1.left_trigger > 0.1 && gamepad1.left_trigger < 0.3){

            intakeMove.setPower(-0.2);
            marker_servo.setPosition(up_position);
        }
        else {
            intakeMove.setPower(0);
        }

    }

}
