package org.firstinspires.ftc.teamcode.Testing_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Harbor_Days.HardwareBot;

/**
 * Created by abhin on 8/29/2017.
 */
@Autonomous (name = "Gyro", group = "Test")
public class GyroSensorMR extends LinearOpMode {
    HardwareBot bot;
    int heading;
    int zRotation;

    @Override
    public void runOpMode() throws InterruptedException {

        bot = new HardwareBot();

        telemetry.addData("State: ", "Start Init");
        telemetry.update();
        bot.init(hardwareMap);

        telemetry.addData("State: ", "Gyro Calibration Start");
        telemetry.update();
        bot.mrGyro.calibrate();
        telemetry.addData("State: ", "Gyro Calibrate Done");
        telemetry.update();

        waitForStart();

        while (bot.mrGyro.isCalibrating()){}
        telemetry.addData("State: ", "Gyro Done");
        telemetry.update();

        while (opModeIsActive()){
            heading = bot.mrGyro.getHeading();
            zRotation = bot.mrGyro.getIntegratedZValue();

            telemetry.addData("Heading: ", heading);
            telemetry.addData("ZRotation: ", zRotation);
            telemetry.update();

            idle();
        }

    }
}
