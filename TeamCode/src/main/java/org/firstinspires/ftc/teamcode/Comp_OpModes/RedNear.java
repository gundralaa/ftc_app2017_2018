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
    double distInInches;
    Orientation angles;

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
        //Move Forward On Ramp
        angles = bot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        runToTarget(bot, 3.0, 0.25);

        sleep(1000);

        runToTarget(bot, 1.0, 0.25);

        sleep(1000);

        //Set Encoders to Different Mode
        bot.leftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bot.rightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bot.leftBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bot.rightBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Set Power of Motors
        bot.leftFrontMotor.setPower(0.4);
        bot.leftBackMotor.setPower(0.4);
        bot.rightBackMotor.setPower(0.4);
        bot.rightFrontMotor.setPower(0.4);

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

        //TODO Use The GYRO to Correct the Heading or The Z Angle of The Robot When it Goes Off The Ramp

        //If the seenMark is null then set to a default
        if(seenMark == RelicRecoveryVuMark.UNKNOWN){
            seenMark = RelicRecoveryVuMark.RIGHT;
        }

        //Encoder Drive Forward based on the seen Mark
        switch (seenMark){
            case RIGHT:
                distInInches = 6.0;
                //6 inches
                runToTarget(bot,distInInches,0.25);
                break;
            case CENTER:
                distInInches = 8.0;
                // 8 inches
                runToTarget(bot, distInInches, 0.25);
                break;
            case LEFT:
                distInInches = 8.0;
                // 8 inches
                runToTarget(bot, distInInches, 0.25);
                break;
        }

        //Turn 90 based on IMU
        turn90(bot);

        //TODO Drive Forward a constant Distance Encoder Drive
        runToTarget(bot,6.00,0.25);

        //TODO Release the Glyph
        releaseGlyph(bot);

    }

    public void turn90(HardwareBot bot) {
        double turnPower = 0.3;

        angles = bot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double initialAngle = angles.firstAngle;
        bot.leftFrontMotor.setPower(turnPower);
        bot.leftBackMotor.setPower(turnPower);
        bot.rightBackMotor.setPower(-1 * turnPower);
        bot.rightFrontMotor.setPower(-1 * turnPower);
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
        bot.leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bot.rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

}
