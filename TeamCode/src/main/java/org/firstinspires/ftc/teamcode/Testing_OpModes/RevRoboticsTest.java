package org.firstinspires.ftc.teamcode.Testing_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by abhin on 10/2/2017.
 */
@TeleOp(name = "RevRoboticsTest", group = "")
public class RevRoboticsTest extends LinearOpMode {

    DcMotor lMotor, rMotor;
    double lPower, rPower;


    @Override
    public void runOpMode() throws InterruptedException {
        lMotor = hardwareMap.dcMotor.get("lMotor");
        rMotor = hardwareMap.dcMotor.get("rMotor");

        lMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while (opModeIsActive()){
            lPower = gamepad1.left_stick_y;
            rPower = gamepad1.right_stick_y;
            telemetry.addData("Left Motor Power", lPower);
            telemetry.addData("Right Motor Power", rPower);
            telemetry.update();

            lMotor.setPower(lPower);
            rMotor.setPower(rPower);
        }
    }
}
