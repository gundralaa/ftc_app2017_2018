package org.firstinspires.ftc.teamcode.Sensor_Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HardwareBot;

/**
 * Created by abhin on 8/29/2017.
 */
@Autonomous(name = "ColorSensor", group = "Test")
public class ColorSensor extends LinearOpMode {
    HardwareBot bot;
    byte[] colorCache;
    boolean buttonState = false;
    boolean ledState = false;

    @Override
    public void runOpMode() throws InterruptedException {

        bot = new HardwareBot();

        telemetry.addData("State:","Init Beginning");
        telemetry.update();
        bot.init(hardwareMap);
        telemetry.addData("State:","Init Ended");
        telemetry.addData("Start State:","Waiting");
        telemetry.update();

        waitForStart();

        telemetry.addData("Start State:","Running");
        telemetry.update();
        bot.mrColorReader.write8(3,1);

        while (opModeIsActive()){
            if(!buttonState && gamepad1.x){
                buttonState = true;
                if(ledState){
                    bot.mrColorReader.write8(3,1);
                    ledState = false;
                } else if(!ledState){
                    bot.mrColorReader.write8(3,0);
                    ledState = true;
                }
                telemetry.addData("LEDState: ", ledState);
                telemetry.update();
            }
            if(!gamepad1.x){
                buttonState = false;
            }
            colorCache = bot.mrColorReader.read(0x04,1);
            telemetry.addData("LEDState: ", ledState);
            telemetry.addData("Color Number: ", colorCache[0] & 0xff);
            telemetry.update();

            idle();

        }
    }
}
