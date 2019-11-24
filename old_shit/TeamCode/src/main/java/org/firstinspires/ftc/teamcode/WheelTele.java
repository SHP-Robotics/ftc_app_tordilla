package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

//created by jonathon for 13981

@Disabled
@TeleOp
public class WheelTele extends BaseWheel{

    @Override
    public void init(){

        super.init();

    }

    @Override
    public void start(){

        super.start();
    }

    @Override
    public void loop() {
        super.loop();

        //drive train

        left = this.gamepad1.left_stick_y;
        right = -this.gamepad1.right_stick_y;

        leftBack.setPower(left);
        rightBack.setPower(right);
        leftFront.setPower(left);
        rightFront.setPower(right);

        if(gamepad1.a) {
            servo.setPosition(0.3);
        }
        else if(gamepad1.b) {
            servo.setPosition(0);
        }
    }
}

