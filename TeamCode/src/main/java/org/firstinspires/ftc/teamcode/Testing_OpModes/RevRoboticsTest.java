package org.firstinspires.ftc.teamcode.Testing_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by abhin on 10/2/2017.
 */
@TeleOp(name = "RevRoboticsTest", group = "")
public class RevRoboticsTest extends LinearOpMode {


    DcMotor leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor;

    Servo leftGrabServo, rightGrabServo;
    double lPower, rPower;
    boolean buttonWasOffX = true;
    boolean buttonWasOffY = true;
    double leftPosition = 1.0;
    double rightPosition = 0.0;


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

        leftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);


        telemetry.addData("Mode", "Waiting for start...");
        telemetry.update();
        waitForStart();
        telemetry.addData("Mode", "Started");
        leftGrabServo.setPosition(leftPosition);
        rightGrabServo.setPosition(rightPosition);

        while (opModeIsActive()) {
            lPower = steadyAcceleration(Range.clip(gamepad1.left_stick_y, -1.0, 1.0));
            rPower = steadyAcceleration(Range.clip(gamepad1.right_stick_y, -1.0, 1.0));

            telemetry.addData("Front Motor L Power", lPower);
            telemetry.addData("Front Motor R Power", rPower);
            telemetry.addData("Back Motor L Power", lPower);
            telemetry.addData("Back Motor R Power", rPower);
            telemetry.update();

            leftFrontMotor.setPower(lPower);
            rightFrontMotor.setPower(rPower);
            leftBackMotor.setPower(lPower);
            rightBackMotor.setPower(rPower);


            if (gamepad1.y) {
                closeServos(leftGrabServo, rightGrabServo);
            } else if (gamepad1.x) {
                openServos(leftGrabServo, rightGrabServo);
            }

            telemetry.addData("Right Position: ", rightPosition);
            telemetry.addData("Left Position: ", leftPosition);
            telemetry.update();

            idle();
        }

    }

    public double steadyAcceleration(double raw) {
        if (raw >= -0.5 && raw <= 0.5) {
            return linear(raw);
        }
        else {
            return cubic(raw);
        }
    }
    // y pressed
    public final double SERVO_INCREMENT = 0.01;
    public final double LEFT_SERVO_MIN = 0.5;
    public final double RIGHT_SERVO_MAX = 0.5;
    public void closeServos(Servo left, Servo right) {
        double currPosL, currPosR;
        while (gamepad1.y) {
            currPosL = left.getPosition();
            currPosR = right.getPosition();
            telemetry.addData("Left Position: ", currPosL);
            telemetry.addData("Right Position: ", currPosR);
            telemetry.update();
            left.setPosition(Range.clip(currPosL - SERVO_INCREMENT, LEFT_SERVO_MIN, 1.0));
            right.setPosition(Range.clip(currPosR + SERVO_INCREMENT, 0.0, RIGHT_SERVO_MAX));
            idle();
        }
    }

    // x pressed
    public void openServos(Servo left, Servo right) {
        double currPosL, currPosR;
        while (gamepad1.x) {
            currPosL = left.getPosition();
            currPosR = right.getPosition();
            telemetry.addData("Left Position: ", currPosL);
            telemetry.addData("Right Position: ", currPosR);
            telemetry.update();
            left.setPosition(Range.clip(currPosL + SERVO_INCREMENT, LEFT_SERVO_MIN, 1.0));
            right.setPosition(Range.clip(currPosR - SERVO_INCREMENT, -1.0, RIGHT_SERVO_MAX));
            idle();
        }
    }

    public double cubic(double x) {
        return Math.pow(x, 3.0);
    }

    public double linear(double x) {
        return x * 0.25;
    }
}
