package org.firstinspires.ftc.teamcode.Testing_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by abhin on 10/12/2017.
 */
@TeleOp(name = "HdriveTeleop", group = "Teleop")
public class HDriveTeleop extends LinearOpMode {

    DcMotor leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor, hDriveMotor, linearSlideMotor;
    Servo leftGrabServo, rightGrabServo;
    double lPower, rPower, hPower, forwardPower, turningPower, lTrigger, rTrigger;
    boolean buttonWasOffX = true;
    boolean buttonWasOffY = true;
    double leftPosition = 1.0;
    double rightPosition = 0.0;
    private enum stickConfig{
        TANK, // Uses the two sticks to control each side and the left and right triggers to control the H-Drive
        ARCADE // Uses the right stick to control forward and turning movements and the left x for H-Drive
    };
    stickConfig config = stickConfig.TANK;


    @Override
    public void runOpMode() {
        leftFrontMotor = hardwareMap.dcMotor.get("lFrontMotor");
        rightFrontMotor = hardwareMap.dcMotor.get("rFrontMotor");
        leftBackMotor = hardwareMap.dcMotor.get("lBackMotor");
        rightBackMotor = hardwareMap.dcMotor.get("rBackMotor");
        hDriveMotor = hardwareMap.dcMotor.get("hDriveMotor");
        linearSlideMotor = hardwareMap.dcMotor.get("linearSlideMotor");

        leftGrabServo = hardwareMap.servo.get("lServo");
        rightGrabServo = hardwareMap.servo.get("rServo");
/*
        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
*/

        leftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontMotor.setDirection(DcMotor.Direction.REVERSE);
/*
        leftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
*/
        rightBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Mode", "Waiting for start...");
        telemetry.update();
        waitForStart();
        telemetry.addData("Mode", "Started");

        leftGrabServo.setPosition(leftPosition);
        rightGrabServo.setPosition(rightPosition);

        while (opModeIsActive()) {

            switch (config) {
                case TANK:
                    turningPower = -Range.clip(array(gamepad1.right_stick_x), -1.0, 1.0);
                    forwardPower = -(Range.clip(array(gamepad1.right_stick_y), -1.0, 1.0));
                    hPower = Range.clip(array(gamepad1.left_stick_x), -1.0, 1.0);

                    lPower = forwardPower + turningPower;
                    rPower = forwardPower - turningPower;
                    break;

                case ARCADE:
                    lPower = -Range.clip(array(gamepad1.left_stick_y), -1.0, 1.0);
                    rPower = -Range.clip(array(gamepad1.right_stick_y), -1.0, 1.0);
                    lTrigger = Range.clip(array(gamepad1.left_trigger), 0.0, 1.0);
                    rTrigger = Range.clip(array(gamepad1.right_trigger), 0.0, 1.0);

                    hPower = lTrigger - rTrigger;
                    break;
            }

            telemetry.addData("Front Motor L Power", lPower);
            telemetry.addData("Front Motor R Power", rPower);
            telemetry.addData("Back Motor L Power", lPower);
            telemetry.addData("Back Motor R Power", rPower);
            telemetry.addData("H Bridge Motor Power", hPower );

            leftFrontMotor.setPower(lPower);
            rightFrontMotor.setPower(rPower);
            leftBackMotor.setPower(lPower);
            rightBackMotor.setPower(rPower);
            hDriveMotor.setPower(hPower);

            if (gamepad1.x) {
                closeServos(leftGrabServo, rightGrabServo);
            } else if (gamepad1.b) {
                openServos(leftGrabServo, rightGrabServo);
            }
            if(gamepad1.y){
                linearSlideMotor.setPower(1.0);
            }
            else if(gamepad1.a){
                linearSlideMotor.setPower(-1.0);
            } else {
                linearSlideMotor.setPower(0.0);
            }

            if(gamepad1.dpad_down){
                switch (config){
                    case TANK:
                        config = stickConfig.ARCADE;
                        break;
                    case ARCADE:
                        config = stickConfig.TANK;
                        break;
                }
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
    public final double LEFT_SERVO_MIN = 0.4;
    public final double RIGHT_SERVO_MAX = 0.6;

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



    private double[] values = {0.00, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24, 0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};
    public double array(double stickValue) {
        int index = Range.clip((int)Math.abs(stickValue) * 16,0,16);
        double power = values[index];
        if (stickValue < 0){
            power = -power;
        }
        return power;
    }
}