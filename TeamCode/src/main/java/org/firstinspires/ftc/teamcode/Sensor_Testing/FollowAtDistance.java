package org.firstinspires.ftc.teamcode.Sensor_Testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.HardwareBot;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by abhin on 8/31/2017.
 */
@Autonomous(name="Follow At Distance", group="Tests")
public class FollowAtDistance extends LinearOpMode {
    HardwareBot bot;
    DcMotor leftMotor, rightMotor;
    ModernRoboticsI2cRangeSensor rangeSensor;

    final int FOLLOW_DISTANCE = 20; // follow x cm behind
    double zeroPower = 0.0;
    double motorPower = 0.0;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = new HardwareBot();
        bot.init(hardwareMap);

        rangeSensor = bot.mrRange;
        leftMotor = bot.lMotor;
        rightMotor = bot.rMotor;

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double currentDistance;

        telemetry.addData("Mode", "waiting");
        telemetry.update();

        waitForStart();

        telemetry.addData("Mode", "running");
        telemetry.update();

        while (opModeIsActive()) {

            if (rangeSensor.cmUltrasonic() < 10) currentDistance = rangeSensor.cmOptical();
            else currentDistance = rangeSensor.cmUltrasonic();

            if (currentDistance > FOLLOW_DISTANCE){
                //incrementMotorSpeed();
                motorPower = -0.5;
            } else if (currentDistance < FOLLOW_DISTANCE){
                motorPower = 0.0;
                //decrementMotorSpeed();
            } else {
                motorPower = zeroPower;
            }
            leftMotor.setPower(motorPower);
            rightMotor.setPower(motorPower);

            telemetry.addData("Current Distance", currentDistance);
            telemetry.addData("Motor Power", motorPower);
            telemetry.update();
            idle();
        }

    }

    public void decrementMotorSpeed() {
        if (motorPower >= -1.0) {
            motorPower -= 0.2; // can be changed if change in speed is too slow
        }
    }

    public void incrementMotorSpeed() {
        if (motorPower <= 1.0) {
            motorPower += 0.01;
        }
    }

}
