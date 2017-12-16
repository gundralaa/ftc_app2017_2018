package org.firstinspires.ftc.teamcode.Comp_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by abhin on 10/12/2017.
 */
@TeleOp(name = "HdriveTeleop", group = "Teleop")
public class HDriveTeleop extends LinearOpMode {

    DcMotor leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor, hDriveMotor, linearSlideMotor, relicSlide;
    Servo leftGrabServo, rightGrabServo, horizontalJewel, relicGrabber, relicRotater, verticalJewel;
    double lPower, rPower, hPower, forwardPower, turningPower, lTrigger, rTrigger;
    ColorSensor colorSensor;
    boolean buttonWasOffX = true;
    boolean buttonWasOffY = true;
    double leftPosition = 1.0;
    double rightPosition = 0.0;
    /*
    private enum stickConfig{
        TANK, // Uses the two sticks to control each side and the left and right triggers to control the H-Drive
        ARCADE // Uses the right stick to control forward and turning movements and the left x for H-Drive
    };
    */
    //stickConfig config = stickConfig.TANK;


    @Override
    public void runOpMode() {
        leftFrontMotor = hardwareMap.dcMotor.get("lFrontMotor");
        rightFrontMotor = hardwareMap.dcMotor.get("rFrontMotor");
        leftBackMotor = hardwareMap.dcMotor.get("lBackMotor");
        rightBackMotor = hardwareMap.dcMotor.get("rBackMotor");
        hDriveMotor = hardwareMap.dcMotor.get("hDriveMotor");
        linearSlideMotor = hardwareMap.dcMotor.get("linearSlideMotor");
        relicSlide = hardwareMap.dcMotor.get("relicSlide");

        leftGrabServo = hardwareMap.servo.get("lServo");
        rightGrabServo = hardwareMap.servo.get("rServo");
        verticalJewel = hardwareMap.servo.get("vJewel"); // continuous
        horizontalJewel = hardwareMap.servo.get("hJewel"); // 180
        relicRotater = hardwareMap.servo.get("rRotater");
        relicGrabber = hardwareMap.servo.get("rGrabber");
        verticalJewel = hardwareMap.servo.get("vJewel");
        horizontalJewel = hardwareMap.servo.get("hJewel");
        colorSensor = hardwareMap.get(ColorSensor.class, "sensor_color");
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

        /*leftBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);*/

        telemetry.addData("Mode", "Waiting for start...");
        telemetry.update();
        waitForStart();
        telemetry.addData("Mode", "Started");

        leftGrabServo.setPosition(leftPosition);
        rightGrabServo.setPosition(rightPosition);
        horizontalJewel.setPosition(0.0);
        relicRotater.setPosition(0.6);
        relicGrabber.setPosition(0.0);
        boolean drivingSlow = false;
        while (opModeIsActive()) {
            if (gamepad1.y) {
                lPower = -0.25;
                rPower = -0.25;
            } else if (gamepad1.a) {
                lPower = 0.25;
                rPower = 0.25;
            } else if (drivingSlow) {
                lPower = Range.clip((linear(gamepad1.left_stick_y)), -1.0, 1.0);
                rPower = Range.clip((linear(gamepad1.right_stick_y)), -1.0, 1.0);
                lTrigger = Range.clip(cubic((gamepad1.left_trigger)), 0.0, 1.0);
                rTrigger = Range.clip(cubic((gamepad1.right_trigger)), 0.0, 1.0);
            } else {
                lPower = Range.clip((cubic(gamepad1.left_stick_y)), -1.0, 1.0);
                rPower = Range.clip((cubic(gamepad1.right_stick_y)), -1.0, 1.0);
                lTrigger = Range.clip(cubic((gamepad1.left_trigger)), 0.0, 1.0);
                rTrigger = Range.clip(cubic((gamepad1.right_trigger)), 0.0, 1.0);
            }

            if (gamepad1.left_bumper) {
                drivingSlow = !(drivingSlow);
            }

            hPower = lTrigger - rTrigger;

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

            if (gamepad2.x) {
                closeServos(leftGrabServo, rightGrabServo);
            } else if (gamepad2.b) {
                openServos(leftGrabServo, rightGrabServo);
            }
            if(gamepad2.y){
                linearSlideMotor.setPower(0.7);
            }
            else if(gamepad2.a){
                linearSlideMotor.setPower(-0.7);
            } else {
                linearSlideMotor.setPower(0.0);
            }

            if (gamepad2.left_bumper) {
                relicSlide.setPower(0.5);
            } else {
                relicSlide.setPower(0.0);
            }

            // Jewel stuff
            if (gamepad1.dpad_up) {
                verticalJewel.setPosition(Range.clip(verticalJewel.getPosition() + 0.01, 0.0, 1.0));
            } else if (gamepad1.dpad_down) {
                verticalJewel.setPosition(Range.clip(verticalJewel.getPosition() - 0.01, 0.0, 1.0));
            } else if (gamepad1.dpad_right) {
                horizontalJewel.setPosition(Range.clip(horizontalJewel.getPosition() + 0.01, 0.0, 1.0)); // possibly, position is speed?
            } else if (gamepad1.dpad_left) {
                horizontalJewel.setPosition(Range.clip(horizontalJewel.getPosition() - 0.01, 0.0, 1.0)); // possibly, position is speed?
            }

            // Grabber stuff
            // 0.53994 = down
            if (gamepad2.dpad_up) {
                relicRotater.setPosition(Range.clip(relicRotater.getPosition() + 0.005, 0.4, 0.6));
            } else if (gamepad2.dpad_down) {
                relicRotater.setPosition(Range.clip(relicRotater.getPosition() - 0.005, 0.4, 0.6));
            }
            telemetry.addData("RelicRotater: ", relicRotater.getPosition());
            if (gamepad2.dpad_left) {
                relicGrabber.setPosition(Range.clip(relicGrabber.getPosition() + 0.05, 0.0, 0.55));
            } else if (gamepad2.dpad_right) {
                relicGrabber.setPosition(Range.clip(relicGrabber.getPosition() - 0.05, 0.0, 0.55));
            }
            telemetry.addData("Grabber: ", relicGrabber.getPosition());

            if (gamepad2.left_trigger > 0) {
                relicGrabber.setPosition(Range.clip(relicGrabber.getPosition() + 0.05, -1.0, 1.0));
            } else if (gamepad2.right_trigger > 0) {
                relicGrabber.setPosition(Range.clip(relicGrabber.getPosition() - 0.05, -1.0, 1.0));
            }


            telemetry.addData("Right Position: ", rightPosition);
            telemetry.addData("Left Position: ", leftPosition);
            telemetry.addData("Color Sensor (R G B): ", (colorSensor.red() + " " + colorSensor.blue() + " " + colorSensor.green()));
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
        while (gamepad2.x) {
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
        while (gamepad2.b) {
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