package org.firstinspires.ftc.teamcode.Testing_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by abhin on 10/2/2017.
 */
@TeleOp(name = "RevRoboticsTest", group = "")
public class RevRoboticsTest extends LinearOpMode {

    DcMotor leftMotor, rightMotor,liftMotor;
    Servo leftGrabServo, rightGrabServo;
    double lPower, rPower;
    boolean buttonWasOffX = true;
    boolean buttonWasOffY = true;
    double leftPosition = 1.0;
    double rightPosition = 0.0;


    @Override
    public void runOpMode() {
        leftMotor = hardwareMap.dcMotor.get("lMotor");
        rightMotor = hardwareMap.dcMotor.get("rMotor");
        liftMotor = hardwareMap.dcMotor.get("linearSlideMotor");

        leftGrabServo = hardwareMap.servo.get("lServo");
        rightGrabServo = hardwareMap.servo.get("rServo");


        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Mode", "Waiting for start...");
        telemetry.update();
        waitForStart();
        telemetry.addData("Mode", "Started");
        leftGrabServo.setPosition(leftPosition);
        rightGrabServo.setPosition(rightPosition);

        while (opModeIsActive()) {
            lPower = steadyAcceleration(Range.clip(gamepad1.left_stick_y, -1.0, 1.0));
            rPower = steadyAcceleration(Range.clip(gamepad1.right_stick_y, -1.0, 1.0));
            telemetry.addData("LeftMotorPower", lPower);
            telemetry.addData("RightMotorPower", rPower);


            leftMotor.setPower(lPower);
            rightMotor.setPower(rPower);

            if(buttonWasOffX && gamepad1.x){
                buttonWasOffX = false;
                leftPosition += 0.1;
                rightPosition -= 0.1;
            }
            if (!gamepad1.x){
                buttonWasOffX = true;
            }
            if (buttonWasOffY && gamepad1.y){
                buttonWasOffY = false;
                leftPosition -= 0.1;
                rightPosition += 0.1;
            }
            if (!gamepad1.y){
                buttonWasOffY = true;
            }

            if(leftPosition <= 1.0 && leftPosition >= -1.0){
                leftGrabServo.setPosition(leftPosition);
            }

            if (rightPosition <= 1.0 && rightPosition >= -1.0){
                rightGrabServo.setPosition(rightPosition);
            }

            if (gamepad1.a){
              liftMotor.setPower(1.0);
            } else if (gamepad1.b){
                liftMotor.setPower(-1.0);
            } else{
                liftMotor.setPower(0.0);
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

    public double cubic(double x) {
        return Math.pow(x, 3.0);
    }

    public double linear(double x) {
        return x * 0.25;
    }
}
