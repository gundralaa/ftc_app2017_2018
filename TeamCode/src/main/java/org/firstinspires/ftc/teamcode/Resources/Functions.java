package org.firstinspires.ftc.teamcode.Resources;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LED;

import org.firstinspires.ftc.teamcode.Comp_OpModes.HardwareBot;

/**
 * Created by abhin on 10/23/2017.
 */

public class Functions {

    public static double inchesToEncoder(double inches) {
        return (1120 / (4 * Math.PI)) * inches;
    }

    public static void runToTarget(HardwareBot bot, double inches, double power, LinearOpMode opMode) {
        int count = (int)Math.floor(inchesToEncoder(inches));

        // only using front motors for encoders
        bot.leftFrontMotor.setTargetPosition(count);
        bot.rightFrontMotor.setTargetPosition(count);
        bot.leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bot.rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        setMotors(bot, power);
        while ((bot.leftFrontMotor.isBusy() || bot.rightFrontMotor.isBusy()) && opMode.opModeIsActive()) {
            opMode.telemetry.addData("Right Front Encoder: ", bot.rightFrontMotor.getCurrentPosition());
            opMode.telemetry.addData("Left Front Encoder: ", bot.leftFrontMotor.getCurrentPosition());
            opMode.telemetry.update();

        }
        setMotors(bot, 0.0);
        bot.leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bot.rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bot.leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bot.rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public static void setRightMotors(HardwareBot bot, double power) {
        bot.rightFrontMotor.setPower(power);
        bot.rightBackMotor.setPower(power);
    }

    public static void setLeftMotors(HardwareBot bot, double power) {
        bot.leftFrontMotor.setPower(power);
        bot.leftBackMotor.setPower(power);
    }

    public static void setMotors(HardwareBot bot, double power) {
        setLeftMotors(bot, power);
        setRightMotors(bot, power);
    }
}
