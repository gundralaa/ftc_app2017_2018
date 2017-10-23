package org.firstinspires.ftc.teamcode.Comp_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Resources.Functions;

/**
 * Created by abhin on 10/23/2017.
 */
@Autonomous(name = "RedNear",group = "Auton")
public class RedNear extends LinearOpMode {
    final double GREY_THRESHOLD = 0.1;
    RelicRecoveryVuMark seenMark;
    ElapsedTime runtime = new ElapsedTime();
    double timeOutS = 5.0;
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
        while (seenMark != RelicRecoveryVuMark.UNKNOWN || runtime.seconds() < timeOutS) { // will break somewhere... hopefully on seeing a recognizable trackable
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(bot.relicTemplate);
            seenMark = vuMark;
            idle();
        }

        //TODO Drive until the light sensor sees a large change in light
        Functions.setMotors(bot, 0.75);
        while (bot.colorSensor.alpha() > GREY_THRESHOLD){
            telemetry.addData("Alpha", bot.colorSensor.alpha());
            telemetry.update();
        }
        Functions.setMotors(bot, 0.0);

        //TODO If the seenMark is null then set to a default
        if(seenMark == )

        //TODO Encoder Drive Forward based on the seen Mark

        //TODO Turn 90 based on IMU

        //TODO Drive Forward a constant Distance Encoder Drive

        //TODO Release the Glyph

    }

    public void turn90(HardwareBot bot) {
        double turnPower = 0.25;

        Orientation angles = bot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double initialAngle = angles.firstAngle;
        bot.leftFrontMotor.setPower(turnPower);
        bot.leftBackMotor.setPower(turnPower);
        bot.rightBackMotor.setPower(-1 *turnPower);
        bot.leftBackMotor.setPower(-1 *turnPower);
        while (angles.firstAngle < initialAngle + 90 && opModeIsActive()) {
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
