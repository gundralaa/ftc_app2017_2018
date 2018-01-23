package org.firstinspires.ftc.teamcode.Comp_OpModes.NewAutons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Comp_OpModes.HardwareBot;

/**
 * Created by abhin on 10/23/2017.
 */
@Autonomous(name = "RedNear",group = "Auton")
public class RedFar extends LinearOpMode {
    RelicRecoveryVuMark seenMark = RelicRecoveryVuMark.UNKNOWN;
    ElapsedTime runtime = new ElapsedTime();
    double timeOutS = 5.0;
    double distInInches;
    Orientation angles;

    @Override
    public void runOpMode() throws InterruptedException {
        //TODO HardwareBot Initialization
        org.firstinspires.ftc.teamcode.Comp_OpModes.HardwareBot bot = new org.firstinspires.ftc.teamcode.Comp_OpModes.HardwareBot();
        bot.init(hardwareMap);
        // set servos to close upon initialization
        //TODO Calibrate the Light Sensor
        telemetry.addData("Status: ","Initilization Complete");
        telemetry.update();

        waitForStart();
        //TODO Vuforia Trackables Activate
        telemetry.addData("Status: ","Start reached");
        telemetry.update();
        bot.leftGrabServo.setPosition(0.5);
        bot.rightGrabServo.setPosition(0.5);
        bot.relicTrackables.activate();
        telemetry.addData("Status: ","Trackables activated");
        telemetry.update();
        //Loop For a certain amount of time until the Image is Seen
        runtime.reset();
        telemetry.addData("Runtime: ", runtime.seconds());
        telemetry.update();
        while ((seenMark == RelicRecoveryVuMark.UNKNOWN && runtime.seconds() < timeOutS) && opModeIsActive()) { // will break somewhere... hopefully on seeing a recognizable trackable
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(bot.relicTemplate);
            seenMark = vuMark;
            telemetry.addData("Runtime: ", runtime.seconds());
            telemetry.update();
            idle();
        }
        telemetry.addData("VUMARK", seenMark.name());
        telemetry.update();
        angles = bot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        bot.linearSlideMotor.setPower(0.5);
        sleep(400);
        bot.linearSlideMotor.setPower(0.0);

        sleep(500);
        //Move Forward On Ramp
        // angles used to be here

        runToTarget(bot, 3.0, 0.4);

        sleep(1000);

        runToTarget(bot, 1.0, 0.4);

        sleep(1000);

        //Set Encoders to Different Mode
        bot.leftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bot.rightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bot.leftBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bot.rightBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Set Power of Motors
        // 0.4 does work for this
        bot.leftFrontMotor.setPower(0.25);
        bot.leftBackMotor.setPower(0.25);
        bot.rightBackMotor.setPower(0.25);
        bot.rightFrontMotor.setPower(0.25);

        // Get Current Y Axis Angle
        angles = bot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        // Loop While Angle is More Negative the Zero
        while (angles.secondAngle < -2 && opModeIsActive()) {
            angles = bot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            idle();
        }

        // Stop Motors
        bot.leftFrontMotor.setPower(0.0);
        bot.leftBackMotor.setPower(0.0);
        bot.rightBackMotor.setPower(0.0);
        bot.rightFrontMotor.setPower(0.0);

        // Set Encoder Mode Again
        bot.leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bot.rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bot.leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bot.rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sleep(200);

        //Use The GYRO to Correct the Heading or The Z Angle of The Robot When it Goes Off The Ramp
        angles = bot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("FirstAngle: ", angles.firstAngle);
        telemetry.update();

        double lpower, rpower;
        if (angles.firstAngle < 0) {
            lpower = -0.25;
        } else if (angles.firstAngle > 0) {
            lpower = 0.25;
        } else { // no angle change
            lpower = 0.0;
        }
        rpower = -1 * lpower;

        bot.leftFrontMotor.setPower(lpower);
        bot.leftBackMotor.setPower(lpower);
        bot.rightBackMotor.setPower(rpower);
        bot.rightFrontMotor.setPower(rpower);

        if (angles.firstAngle < 0) {
            while (angles.firstAngle < 0) {
                angles = bot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                telemetry.addData("FirstAngle: ", angles.firstAngle);
                telemetry.update();
                idle();
            }

        } else if (angles.firstAngle > 0) {
            while (angles.firstAngle < 0) {
                angles = bot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                telemetry.addData("FirstAngle: ", angles.firstAngle);
                telemetry.update();
                idle();
            }

        }
        bot.leftFrontMotor.setPower(0.0);
        bot.leftBackMotor.setPower(0.0);
        bot.rightBackMotor.setPower(0.0);
        bot.rightFrontMotor.setPower(0.0);

        //TODO Drive Forward a constant Distance Encoder Drive
        runToTarget(bot,5.00,0.25);

        //Turn 90 based on IMU
        turnXDegrees(bot, 90);


        //If the seenMark is null then set to a default
        if(seenMark == RelicRecoveryVuMark.UNKNOWN){
            seenMark = RelicRecoveryVuMark.RIGHT;
        }

        //Encoder Drive Forward based on the seen Mark
        switch (seenMark){
            case RIGHT:
                distInInches = 5.0;
                //6 inches
                runToTarget(bot,distInInches,0.25);
                break;
            case CENTER:
                distInInches = 13.0;
                // 14 inches
                runToTarget(bot, distInInches, 0.25);
                break;
            case LEFT:
                distInInches = 21.0;
                // 22 inches
                runToTarget(bot, distInInches, 0.25);
                break;
        }

        //Turn 90 based on IMU
        turnXDegrees(bot, -90);

        //TODO Drive Forward a constant Distance Encoder Drive
        runToTarget(bot,6.00,0.25);

        //TODO Release the Glyph
        releaseGlyph(bot);

        runToTarget(bot,5.0,0.25);
        sleep(200);

        bot.leftFrontMotor.setPower(-0.25);
        bot.leftBackMotor.setPower(-0.25);
        bot.rightBackMotor.setPower(-0.25);
        bot.rightFrontMotor.setPower(-0.25);
        sleep(100);
        bot.leftFrontMotor.setPower(0);
        bot.leftBackMotor.setPower(0);
        bot.rightBackMotor.setPower(0);
        bot.rightFrontMotor.setPower(0);
        sleep(100);
        bot.leftFrontMotor.setPower(0.25);
        bot.leftBackMotor.setPower(0.25);
        bot.rightBackMotor.setPower(-0.25);
        bot.rightFrontMotor.setPower(-0.25);
        sleep(500);
        bot.leftFrontMotor.setPower(-0.25);
        bot.leftBackMotor.setPower(-0.25);
        bot.rightBackMotor.setPower(0.25);
        bot.rightFrontMotor.setPower(0.25);
        sleep(500);

        bot.leftFrontMotor.setPower(-0.25);
        bot.leftBackMotor.setPower(-0.25);
        bot.rightBackMotor.setPower(-0.25);
        bot.rightFrontMotor.setPower(-0.25);
        sleep(400);
        bot.leftFrontMotor.setPower(0);
        bot.leftBackMotor.setPower(0);
        bot.rightBackMotor.setPower(0);
        bot.rightFrontMotor.setPower(0);
    }

    public void turnXDegrees(org.firstinspires.ftc.teamcode.Comp_OpModes.HardwareBot bot, int degrees) {
        double turnPower = 0.3;

        angles = bot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double initialAngle = angles.firstAngle;
        double targetAngle = initialAngle + degrees;
        bot.leftFrontMotor.setPower(turnPower);
        bot.leftBackMotor.setPower(turnPower);
        bot.rightBackMotor.setPower(-1 * turnPower);
        bot.rightFrontMotor.setPower(-1 * turnPower);
        while (angles.firstAngle > targetAngle && opModeIsActive()) {
            angles = bot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            idle();
        }
        bot.leftBackMotor.setPower(0.0);
        bot.leftFrontMotor.setPower(0.0);
        bot.rightBackMotor.setPower(0.0);
        bot.rightFrontMotor.setPower(0.0);
    }


    public void releaseGlyph(org.firstinspires.ftc.teamcode.Comp_OpModes.HardwareBot bot) {
        bot.leftGrabServo.setPosition(0.7);
        bot.rightGrabServo.setPosition(0.3);
    }

    public static double inchesToEncoder(double inches) {
        return (1120 / (4 * Math.PI)) * inches;
    }

    public void runToTarget(HardwareBot bot, double inches, double power) {
        int count = (int)Math.floor(inchesToEncoder(inches));

        // only using front motors for encoders
        bot.leftFrontMotor.setTargetPosition(bot.leftFrontMotor.getCurrentPosition() + (count));
        bot.rightFrontMotor.setTargetPosition(bot.rightFrontMotor.getCurrentPosition() + count);
        bot.leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bot.rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        bot.leftFrontMotor.setPower(power);
        bot.leftBackMotor.setPower(power);
        bot.rightBackMotor.setPower(power);
        bot.rightFrontMotor.setPower(power);

        while ((bot.leftFrontMotor.isBusy() && bot.rightFrontMotor.isBusy()) && opModeIsActive()) {
            telemetry.addData("Right Front Encoder: ", bot.rightFrontMotor.getCurrentPosition());
            telemetry.addData("Left Front Encoder: ", bot.leftFrontMotor.getCurrentPosition());
            telemetry.update();

        }
        bot.leftBackMotor.setPower(0.0);
        bot.leftFrontMotor.setPower(0.0);
        bot.rightBackMotor.setPower(0.0);
        bot.rightFrontMotor.setPower(0.0);

        bot.leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bot.rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}
