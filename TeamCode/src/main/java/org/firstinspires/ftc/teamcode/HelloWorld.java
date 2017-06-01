package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by abhin on 5/30/2017.
 */
@TeleOp(name="Hello World",group="Exercises")
public class HelloWorld extends LinearOpMode {
    DcMotor rMotor, lMotor;
    float leftY, rightY;

    @Override

    public void runOpMode() throws InterruptedException {
        rMotor = hardwareMap.dcMotor.get("rMotor");
        lMotor = hardwareMap.dcMotor.get("lMotor");
        rMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();{
            while (opModeIsActive()){

                leftY = gamepad1.left_stick_y;
                rightY = gamepad1.right_stick_y;

                lMotor.setPower(Range.clip(leftY, -1.0, 1.0));
                rMotor.setPower(Range.clip(rightY, -1.0, 1.0));

                telemetry.addData("Mode", "running");
                telemetry.addData("sticks", "  left=" + leftY + "  right=" + rightY);
                telemetry.update();

                idle();
            }
        }

    }
}
