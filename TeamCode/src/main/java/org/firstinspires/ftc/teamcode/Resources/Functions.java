package org.firstinspires.ftc.teamcode.Resources;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by abhin on 10/23/2017.
 */

public class Functions {

    public double inchesToEncoder(double inches) {
        return (1120 / (4 * Math.PI)) * inches;
    }

    public void runToTarget(double inches, double power) {
        int count = (int)Math.floor(inchesToEncoder(inches));

        // only using front motors for encoders
        leftFrontMotor.setTargetPosition(count);
        rightFrontMotor.setTargetPosition(count);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        setMotors(power);
        while ((leftFrontMotor.isBusy() || rightFrontMotor.isBusy()) && opModeIsActive()) {
            telemetry.addData("Right Front Encoder: ", rightFrontMotor.getCurrentPosition());
            telemetry.addData("Left Front Encoder: ", leftFrontMotor.getCurrentPosition());
            telemetry.update();

        }
        setMotors(0.0);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void setRightMotors(double power) {
        rightFrontMotor.setPower(power);
        rightBackMotor.setPower(power);
    }

    public void setLeftMotors(double power) {
        leftFrontMotor.setPower(power);
        leftBackMotor.setPower(power);
    }

    public void setMotors(double power) {
        setLeftMotors(power);
        setRightMotors(power);
    }

    // only use if there is jumping at the beginning
    public void accelerate(double targetPower) {
        double currPower;
        double increment = 0.01;

        if (leftFrontMotor.getPower() != rightFrontMotor.getPower()) {
            currPower = (leftFrontMotor.getPower() + rightFrontMotor.getPower()) / 2;
        } else {
            currPower = leftFrontMotor.getPower();
        }

        while (currPower < targetPower) {
            setMotors(currPower);
            currPower += increment; // may be too slow
            idle();
        }
        setMotors(targetPower);
    }
}
