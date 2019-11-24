package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class AutoPark extends Base {
    public int step = 0;

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

        switch(step){

            case 0:
                //test the arm
                move_arm(0.4, 1);
                break;

            case 1:
                //test the strafe
                strafe(0.4, 36);
                break;

            default:
                break;
        }
    }

    @Override
    public void stop(){
        super.stop();
    }

}
