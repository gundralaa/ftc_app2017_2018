package org.firstinspires.ftc.teamcode.Testing_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by abhin on 10/2/2017.
 */
@TeleOp(name = "RevRoboticsTest", group = "")
public class RevRoboticsTest extends LinearOpMode {

    DcMotor leftMotor, rightMotor;
    double lPower, rPower;


    @Override
    public void runOpMode() {
        leftMotor = hardwareMap.dcMotor.get("lMotor");
        rightMotor = hardwareMap.dcMotor.get("rMotor");

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Mode", "Waiting for start...");
        telemetry.update();
        waitForStart();
        telemetry.addData("Mode", "Started");

        while (opModeIsActive()) {
            lPower = steadyAcceleration(Range.clip(gamepad1.left_stick_y, -1.0, 1.0));
            rPower = steadyAcceleration(Range.clip(gamepad1.right_stick_y, -1.0, 1.0));
            telemetry.addData("LeftMotorPower", lPower);
            telemetry.addData("RightMotorPower", rPower);
            telemetry.update();

            leftMotor.setPower(lPower);
            rightMotor.setPower(rPower);
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
