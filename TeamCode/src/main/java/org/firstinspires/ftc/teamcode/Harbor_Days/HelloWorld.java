package org.firstinspires.ftc.teamcode.Harbor_Days;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by abhin on 5/30/2017.
 */
@TeleOp(name="Hello World",group="Exercises")
public class HelloWorld extends LinearOpMode {
    DcMotor rMotor, lMotor, shooter;
    Servo rMouth, lMouth, rBrow, lBrow;
    float leftY, rightY;
    float rBrowPos = 0.0f;
    float lBrowPos = 1.0f;
    float rMouthPos = 0.0f;
    float lMouthPos = 1.0f;

    @Override

    public void runOpMode() throws InterruptedException {
        rMotor = hardwareMap.dcMotor.get("rMotor");
        lMotor = hardwareMap.dcMotor.get("lMotor");
        shooter = hardwareMap.dcMotor.get("Shooter");

        rBrow = hardwareMap.servo.get("rBrow");
        lBrow = hardwareMap.servo.get("lBrow");
        rMouth = hardwareMap.servo.get("rMouth");
        lMouth = hardwareMap.servo.get("lMouth");
        rMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        while (opModeIsActive()){

            leftY = gamepad1.left_stick_y;
            rightY = gamepad1.right_stick_y;

            lMotor.setPower(Range.clip(leftY, -1.0, 1.0));
            rMotor.setPower(Range.clip(rightY, -1.0, 1.0));

            if(gamepad1.x){
                shooter.setPower(1.0);
            }
            else {
                shooter.setPower(0.0);
            }
            if (gamepad1.a) { //Happy
                rBrowPos = 0.2f;
                lBrowPos = 0.8f;
                rMouthPos = 0.8f;
                lMouthPos = 0.2f;
            }
            if (gamepad1.b) { //Sad
                rBrowPos = 0.4f;
                lBrowPos = 0.6f;
                rMouthPos = 0.0f;
                lMouthPos = 1.0f;
            }
            if (gamepad1.y) { //Angry
                rBrowPos = 0.0f;
                lBrowPos = 1.0f;
                rMouthPos = 0.0f;
                lMouthPos = 1.0f;
            }
            if (gamepad1.dpad_up) {

                rMouthPos += 0.1;
            }

            rBrow.setPosition(rBrowPos);
            lBrow.setPosition(lBrowPos);
            lMouth.setPosition(lMouthPos);
            rMouth.setPosition(rMouthPos);

            telemetry.addData("Mode", "running");
            telemetry.addData("sticks", "  left=" + leftY + "  right=" + rightY);
            telemetry.addData("Servos", " rBrow = " + rBrowPos + "lBrow = " + lBrowPos + "rMouth = " + rMouthPos + "lMouth = " + lMouthPos);
            telemetry.update();

            idle();
        }

    }
}
