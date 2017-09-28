package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by abhin on 6/15/2017.
 */

/**
 * Create a teleop Program that changes emotions based on button
 * presses and also shoots and drives normally from memory.
 */
@TeleOp(name = "HarborDemo", group = "Solutions")
public class FacialTest extends LinearOpMode {
    HardwareBot bot;
    float stickR, stickL;
    final float POWERMIN = -0.5f;
    final float POWERMAX = 0.5f;
    double[] values = {0.00,0.05,0.09,0.10,0.12,0.15,0.18,0.24,0.30,0.36,0.43,0.50,0.60,0.72,0.85,1.00,1.00};
    int indexL, indexR;
    double powerL, powerR;

    @Override
    public void runOpMode() throws InterruptedException {

        bot = new HardwareBot();
        bot.init(hardwareMap);

        waitForStart();
        while(opModeIsActive()){

            stickL = gamepad1.left_stick_y;
            stickR = gamepad1.right_stick_y;

            //powerR = stickR*stickR;
            //powerL = stickL*stickL;

            indexL = (int)Range.clip(Math.abs(stickL)*16,0,16);
            indexR = (int)Range.clip(Math.abs(stickR)*16,0,16);

            if (stickL < 0){
                powerL = -values[indexL];
                //powerL = -powerL;
            }
            else {
                powerL = values[indexL];
            }

            if (stickR < 0){
                powerR = -values[indexR];
                //powerR = -powerR;
            }
            else {
                powerR = values[indexR];
            }

            bot.lMotor.setPower(Range.clip(powerL,POWERMIN,POWERMAX));
            bot.rMotor.setPower(Range.clip(powerR,POWERMIN,POWERMAX));

            if (gamepad1.a){
                EmotionRecognition.happyFace(bot);
            }
            if (gamepad1.b){
                EmotionRecognition.sadFace(bot);
            }
            if (gamepad1.x){
                EmotionRecognition.neutralFace(bot);
            }
            if (gamepad1.y){
                EmotionRecognition.angryFace(bot);
            }
            /**
            if (gamepad1.right_trigger > 0.5){
                bot.shooter.setPower(1.0);
            } else {
                bot.shooter.setPower(0.0);
            }
             **/
            idle();

        }


    }
}
