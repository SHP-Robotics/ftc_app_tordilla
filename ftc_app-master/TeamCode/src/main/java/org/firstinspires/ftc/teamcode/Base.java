/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import android.hardware.Sensor;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Basic: Iterative OpMode", group="Iterative Opmode")
@Disabled
public class Base extends OpMode
{
    // Declare OpMode members.
    public ElapsedTime timer = new ElapsedTime();
    public DcMotor lf;
    public DcMotor rf;
    public DcMotor lb;
    public DcMotor rb;
    public DcMotor arm;
    public Servo ls;
    public Servo rs;
    public DistanceSensor distance_sensor;
    public ColorSensor color_sensor;

    public double red_arm_power = 0.3;
    public double inc_arm_power = 0.7;
    public double max_arm_power = 1.0;
    //both start and end values are bound to change
    public double ls_start = 0;
    public double ls_end = 0.5;
    public double rs_start = 0;
    public double rs_end = 0.5;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        lf  = hardwareMap.get(DcMotor.class, "left_front");
        rf = hardwareMap.get(DcMotor.class, "right_front");
        lb  = hardwareMap.get(DcMotor.class, "left_back");
        rb = hardwareMap.get(DcMotor.class, "right_back");
        rs = hardwareMap.get(Servo.class, "right_servo");
        ls = hardwareMap.get(Servo.class, "left_servo");
        arm = hardwareMap.get(DcMotor.class, "arm");
        color_sensor = hardwareMap.get(ColorSensor.class, "color");
        distance_sensor = hardwareMap.get(DistanceSensor.class, "distance");

        lf.setDirection(DcMotor.Direction.FORWARD); //positive
        lb.setDirection(DcMotor.Direction.FORWARD);
        rf.setDirection(DcMotor.Direction.REVERSE); //negative
        rb.setDirection(DcMotor.Direction.REVERSE);
        //setting the direction of the arm; subject to change
        arm.setDirection(DcMotor.Direction.REVERSE);
        rs.setPosition(rs_start);
        ls.setPosition(ls_start);

        double LfDrive = -gamepad1.left_stick_y;
        double LbDrive = gamepad1.left_stick_y;
        double RfDrive  =  -gamepad1.right_stick_y;
        double RbDrive = gamepad1.right_stick_y;

        double LfPower = Range.clip(LfDrive, -1.0, 1.0) ;
        double RfPower = Range.clip(RfDrive, -1.0, 1.0) ;
        double LbPower = Range.clip(LbDrive, -1.0, 1.0);
        double RbPower = Range.clip(RbDrive, -1.0, 1.0);

        rf.setPower(RfPower);
        lf.setPower(LfPower);
        rb.setPower(RbPower);
        lb.setPower(LbPower);

        timer.reset();
        reset_drive();
        reset_arm();

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Go Gatorbots", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void loop() {

        //using the arm - also subject to change for a different reason
        if(gamepad1.right_trigger >= 0.3 && gamepad1.right_trigger < 0.6 &&
                gamepad1.left_trigger >= 0.3 && gamepad1.left_trigger < 0.6){
            arm.setPower(red_arm_power);
        }
        if(gamepad1.right_trigger >= 0.6 && gamepad1.right_trigger < 1 &&
                gamepad1.left_trigger >= 0.6 && gamepad1.left_trigger < 1){
            arm.setPower(inc_arm_power);
        }
        if(gamepad1.right_trigger == 1 && gamepad1.left_trigger == 1){
            arm.setPower(max_arm_power);
        }

        if(gamepad1.a && rs.getPosition() == 0 && ls.getPosition() == 0){
            rs.setPosition(rs_end);
            ls.setPosition(ls_end);
        }
        if(gamepad1.b && rs.getPosition() != 0 && ls.getPosition() != 0){
            rs.setPosition(rs_start);
            ls.setPosition(ls_start);
        }

        telemetry.addData("Status", "Run Time: " + timer.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", lf, rf, lb, rb);
    }

    @Override
    public void stop() {
        rf.setPower(0);
        lf.setPower(0);
        rb.setPower(0);
        lb.setPower(0);
    }

    public void reset_drive(){
        if(rf.getMode() != DcMotor.RunMode.RUN_USING_ENCODER &&
                rb.getMode() != DcMotor.RunMode.RUN_USING_ENCODER &&
                lf.getMode() != DcMotor.RunMode.RUN_USING_ENCODER &&
                lb.getMode() != DcMotor.RunMode.RUN_USING_ENCODER ){

            rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        }

        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void reset_arm(){
        if(arm.getMode() != DcMotor.RunMode.RUN_USING_ENCODER){
            arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public int get_rf(){
        if(rf.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        return rf.getCurrentPosition();
    }

    public int get_rb(){
        if(rb.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        return rb.getCurrentPosition();
    }

    public int get_lf(){
        if(lf.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        return lf.getCurrentPosition();
    }

    public int get_lb(){
        if(lb.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        return lb.getCurrentPosition();
    }

    public boolean close(){
        rs.setPosition(rs_end);
        ls.setPosition(ls_end);
        return true;
    }

    public boolean open(){
        rs.setPosition(rs_start);
        ls.setPosition(ls_start);
        return true;
    }

    public void grab(){
        rs.setPosition(rs_end);
        ls.setPosition(ls_end);
    }

    public void release(){
        rs.setPosition(rs_start);
        ls.setPosition(ls_start);
    }

    public boolean drive(double power, double in){
        double target = Math.abs(ConstantVariables.K_PPIN_DRIVE * in);

        if(rf.getCurrentPosition() >= target && lf.getCurrentPosition() >= target &&
        rb.getCurrentPosition() >= target && rb.getCurrentPosition() >= target){
            stop_all();
        }
        else{
            rf.setPower(power);
            lf.setPower(power);
            rb.setPower(power);
            lb.setPower(power);
        }
        return false;
    }

    public boolean move_arm(double power, double in){
        double target = Math.abs(ConstantVariables.K_PIN_CIRCUMFRENCE * in);

        if(arm.getCurrentPosition() >= target){
            arm.setPower(0);
        }
        else{
            //also bound to change
            arm.setPower(power);
        }
        return false;
    }

    public boolean turn(double power, double deg){
        double target = Math.abs(ConstantVariables.K_PPDEG_DRIVE * deg);

        if(rf.getCurrentPosition() >= target && lf.getCurrentPosition() >= target &&
                rb.getCurrentPosition() >= target && rb.getCurrentPosition() >= target){
            stop_all();
        }
        //positive turns right
        else{
            rf.setPower(-power);
            rb.setPower(-power);
            lf.setPower(power);
            lb.setPower(power);
        }
        return false;
    }

    //this is seriously going to change, I'm sure.

    public boolean strafe(double power, double in){
        double target = Math.abs(ConstantVariables.K_PPIN_DRIVE * in *
                ConstantVariables.K_RATIO_STRAFE);
        if(rf.getCurrentPosition() >= target && lf.getCurrentPosition() >= target &&
                rb.getCurrentPosition() >= target && rb.getCurrentPosition() >= target){
            stop_all();
        }

        else{
            rf.setPower(power);
            rb.setPower(-power);
            lf.setPower(power);
            lb.setPower(-power);
        }
        return false;
    }

    //Checking for colors
    public boolean is_black(int red, int blue){
        return blue > red * (3.0/4.0);
    }
    public boolean is_yellow(int red, int green, int blue){
        return ((red > 2*blue) && (green > 2*blue));
    }

    public void stop_all(){
        rf.setPower(0);
        lf.setPower(0);
        rb.setPower(0);
        lb.setPower(0);
    }

}
