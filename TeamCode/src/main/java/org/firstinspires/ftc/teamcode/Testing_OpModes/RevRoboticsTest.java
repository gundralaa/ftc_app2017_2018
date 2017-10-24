package org.firstinspires.ftc.teamcode.Testing_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Comp_OpModes.HardwareBot;
import org.firstinspires.ftc.teamcode.Resources.Functions;

/**
 * Created by abhin on 10/2/2017.
 */
@TeleOp(name = "RevRoboticsTest", group = "")
public class RevRoboticsTest extends LinearOpMode {

    final double GREY_THRESHOLD = 0.1;
    RelicRecoveryVuMark seenMark;
    ElapsedTime runtime = new ElapsedTime();
    double timeOutS = 5.0;
    double distInInches;

    @Override
    public void runOpMode() throws InterruptedException {

        //TODO HardwareBot Initialization
        HardwareBot bot = new HardwareBot();
        bot.init(hardwareMap);
        // set servos to close upon initialization
        bot.leftGrabServo.setPosition(1.0);
        bot.rightGrabServo.setPosition(0.0);

        //TODO Calibrate the Light Sensor
        telemetry.addData("Status: ","Initilization Complete");
        telemetry.update();

        waitForStart();
        //TODO Vuforia Trackables Activate
        //Loop For a certain amount of time until the Image is Seen
        runtime.reset();

        //TODO Turn 90 based on IMU
        turn90(bot);

        //TODO Drive Forward a constant Distance Encoder Drive
        Functions.runToTarget(bot,6.00,0.25,this);

        //TODO Release the Glyph
        releaseGlyph(bot);

    }

    public void turn90(HardwareBot bot) {
        double turnPower = 0.5;

        Orientation angles = bot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double initialAngle = angles.firstAngle;
        bot.leftFrontMotor.setPower(-1 *turnPower);
        bot.leftBackMotor.setPower(-1 *turnPower);
        bot.rightBackMotor.setPower(turnPower);
        bot.rightFrontMotor.setPower(turnPower);
        while (angles.firstAngle > -90 && opModeIsActive()) {
            angles = bot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            idle();
        }
        bot.leftBackMotor.setPower(0.0);
        bot.leftFrontMotor.setPower(0.0);
        bot.rightBackMotor.setPower(0.0);
        bot.rightFrontMotor.setPower(0.0);
    }


    public void releaseGlyph(HardwareBot bot) {
        bot.leftGrabServo.setPosition(0.7);
        bot.rightGrabServo.setPosition(0.3);
    }
}
