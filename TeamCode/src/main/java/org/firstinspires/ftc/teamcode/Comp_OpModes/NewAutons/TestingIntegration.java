package org.firstinspires.ftc.teamcode.Comp_OpModes.NewAutons;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * Created by saeli on 1/23/2018.
 */
@Autonomous(name="Integration",group="Testing")
public class TestingIntegration extends LinearOpMode {

    public void runOpMode() throws InterruptedException {
        org.firstinspires.ftc.teamcode.Comp_OpModes.HardwareBot bot = new org.firstinspires.ftc.teamcode.Comp_OpModes.HardwareBot();
        bot.init(hardwareMap);
        bot.imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

        Position position;
        while (opModeIsActive()) {
            position = bot.imu.getPosition();
        }

    }
}
