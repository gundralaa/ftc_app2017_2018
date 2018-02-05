package org.firstinspires.ftc.teamcode.Comp_OpModes.Mechanum;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by saeli on 1/16/2018.
 */

@TeleOp(name = "Mechanum TeleOp", group = "Mechanum")
public class MechanumTeleOp extends LinearOpMode {
    public final double SERVO_INCREMENT = 0.01;
    public final double LEFT_SERVO_MIN = 0.4;
    public final double RIGHT_SERVO_MAX = 0.6;
    double lPower, rPower;
    double strafingPower = 0.5;

    public void runOpMode() {
        HardwareBot bot = new HardwareBot();
        bot.init(hardwareMap);

        telemetry.addData("Mode", "Waiting for start...");
        telemetry.update();
        waitForStart();
        telemetry.addData("Mode", "Running...");
        telemetry.update();
        bot.leftGrabServo.setPosition(1.0);
        bot.rightGrabServo.setPosition(0.0);

        while (opModeIsActive()) {
            lPower = Range.clip(cubic(gamepad1.left_stick_y), -1.0, 1.0);
            rPower = Range.clip(cubic(gamepad1.right_stick_y), -1.0, 1.0);

            // Glyph grabbing
            if (gamepad2.x) {
                closeServos(bot.leftGrabServo, bot.rightGrabServo);
            } else if (gamepad2.b) {
                openServos(bot.leftGrabServo, bot.rightGrabServo);
            }
            telemetry.addData("Left Grab Servo", bot.leftGrabServo.getPosition());
            telemetry.addData("Right Grab Servo", bot.rightGrabServo.getPosition());

            // Linear slide power
            if (gamepad2.y) {
                bot.linearSlideMotor.setPower(0.7);
                telemetry.addData("Glyph Slide Motor", "UP");
            } else if (gamepad2.a) {
                bot.linearSlideMotor.setPower(-0.7);
                telemetry.addData("Glyph Slide Motor", "DOWN");
            } else {
                bot.linearSlideMotor.setPower(0.0);
                telemetry.addData("Glyph Slide Motor", "OFF");
            }

            // Relic slide power
            if (gamepad2.left_bumper) {
                bot.relicSlideMotor.setPower(0.5);
                telemetry.addData("Relic Slide", "ON");
            } else {
                bot.relicSlideMotor.setPower(0.0);
                telemetry.addData("Relic Slide", "OFF");
            }

            // Grabber stuff
            // 0.53994 = down
            if (gamepad2.dpad_up) {
                bot.relicRotater.setPosition(Range.clip(bot.relicRotater.getPosition() + 0.005, 0.4, 0.6));
            } else if (gamepad2.dpad_down) {
                bot.relicRotater.setPosition(Range.clip(bot.relicRotater.getPosition() - 0.005, 0.4, 0.6));
            } else if (gamepad2.dpad_left) {
                bot.relicGrabber.setPosition(Range.clip(bot.relicGrabber.getPosition(), 0.0, 1.0) + 0.05);
            } else if (gamepad2.dpad_right) {
                bot.relicGrabber.setPosition(Range.clip(bot.relicGrabber.getPosition(), 0.0, 1.0) - 0.05);
            }
            telemetry.addData("RelicRotater: ", bot.relicRotater.getPosition());

            // Drive base
            if (gamepad1.left_trigger > 0) { // Strafe left
                strafingPower = gamepad1.left_trigger;
                bot.leftFrontMotor.setPower(strafingPower);
                bot.leftBackMotor.setPower(-strafingPower);
                bot.rightFrontMotor.setPower(-strafingPower);
                bot.rightBackMotor.setPower(strafingPower);
                telemetry.addData("Drive", "Strafing left");
            } else if (gamepad1.right_trigger > 0) { // Strafe right
                strafingPower = gamepad1.right_trigger;
                bot.leftFrontMotor.setPower(-strafingPower);
                bot.leftBackMotor.setPower(strafingPower);
                bot.rightFrontMotor.setPower(strafingPower);
                bot.rightBackMotor.setPower(-strafingPower);
                telemetry.addData("Drive", "Strafing right");
            } else { // Tank driving
                bot.leftFrontMotor.setPower(lPower);
                bot.leftBackMotor.setPower(lPower);
                bot.rightFrontMotor.setPower(rPower);
                bot.rightBackMotor.setPower(rPower);
                telemetry.addData("Drive", "Left Power - " + lPower + "; Right Power - " + rPower);
            }
            telemetry.update();
            idle();
        }
    }

    public double cubic(double x) {
        return Math.pow(x, 3.0);
    }

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
}
