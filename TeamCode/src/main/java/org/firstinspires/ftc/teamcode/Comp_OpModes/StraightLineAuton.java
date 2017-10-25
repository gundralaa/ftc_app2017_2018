package org.firstinspires.ftc.teamcode.Comp_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
/**
 * Created by saeli on 10/16/2017.
 */


// 1120 encoder counts for 4 * PI inches of travel
@Autonomous(name="Straight Line Autonomous", group="")
public class StraightLineAuton extends LinearOpMode {
    DcMotor leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor;
    Servo leftGrabServo, rightGrabServo;
    @Override
    public void runOpMode() {
        leftFrontMotor = hardwareMap.dcMotor.get("lFrontMotor");
        rightFrontMotor = hardwareMap.dcMotor.get("rFrontMotor");
        leftBackMotor = hardwareMap.dcMotor.get("lBackMotor");
        rightBackMotor = hardwareMap.dcMotor.get("rBackMotor");

        leftGrabServo = hardwareMap.servo.get("lServo");
        rightGrabServo = hardwareMap.servo.get("rServo");

        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setDirection(DcMotor.Direction.REVERSE);

        leftBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Mode", "waiting");
        telemetry.update();
        waitForStart();
        telemetry.addData("Mode", "running");
        telemetry.update();
        // close arms around a block
        leftGrabServo.setPosition(0.5);
        rightGrabServo.setPosition(0.5);

        // straight to in front of fence
        //runToTarget(4, 0.25);
        sleep(200);

        // testing for 90 degree turn
        setRightMotors(0.1);
        setLeftMotors(-0.1);
        while (opModeIsActive()) {
            telemetry.addData("Right Front Encoder: ", rightFrontMotor.getCurrentPosition());
            telemetry.addData("Left Front Encoder: ", leftFrontMotor.getCurrentPosition());
            telemetry.update();
            idle();
        }
        setMotors(0.0);
    }

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
