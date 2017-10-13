package org.firstinspires.ftc.teamcode.Testing_OpModes;

import android.graphics.drawable.GradientDrawable;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.bosch.NaiveAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by abhin on 10/12/2017.
 * A self balancing side to side movement
 */


public class IMUBalancing extends LinearOpMode {

    private final double COUNTS_PER_REVOLUTION = 2560;
    private final double WHEEL_DIAMETER = 4;
    private final double COUNTS_PER_INCH = COUNTS_PER_REVOLUTION/(WHEEL_DIAMETER*3.1415);
    private final double P_COEFFICENT = 0.1;

    DcMotor leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor, hDriveMotor, linearSlideMotor;
    Servo leftGrabServo, rightGrabServo;
    BNO055IMU imu;
    double lPower, rPower, hPower, forwardPower, turningPower;
    boolean buttonWasOffX = true;
    boolean buttonWasOffY = true;
    double leftPosition = 1.0;
    double rightPosition = 0.0;
    Orientation angles;


    @Override
    public void runOpMode() {
        leftFrontMotor = hardwareMap.dcMotor.get("lFrontMotor");
        rightFrontMotor = hardwareMap.dcMotor.get("rFrontMotor");
        leftBackMotor = hardwareMap.dcMotor.get("lBackMotor");
        rightBackMotor = hardwareMap.dcMotor.get("rBackMotor");
        hDriveMotor = hardwareMap.dcMotor.get("hDriveMotor");
        linearSlideMotor = hardwareMap.dcMotor.get("linearSlideMotor");

        leftGrabServo = hardwareMap.servo.get("lServo");
        rightGrabServo = hardwareMap.servo.get("rServo");

        hDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setDirection(DcMotor.Direction.REVERSE);

        leftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);

        //IMU Setup and Params
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        telemetry.addData("Mode", "Waiting for start...");
        telemetry.update();
        waitForStart();
        telemetry.addData("Mode", "Started");

        leftGrabServo.setPosition(leftPosition);
        rightGrabServo.setPosition(rightPosition);





    }
    /*
    * Method that drives the HD Drive motor using Encoders
    * Uses Gyro to correct any drifting due to the Torque applied by the H Drive Motor
    */

    public void encoderHDrive(int inches, double speed){
        // Check if the OpMode is running
        if(opModeIsActive()){

            int targetPosition = hDriveMotor.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
            hDriveMotor.setTargetPosition(targetPosition);

            hDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            hDriveMotor.setPower(speed);
            angles = imu.getAngularOrientation();
            float heading = angles.firstAngle;

            while (opModeIsActive() && hDriveMotor.isBusy()){
                double lPower = 0.0;
                double rPower = 0.0;
                // Check for heading and correct direction
                float error = getError(heading);
                double steering = getSteering(heading, P_COEFFICENT);
                lPower = lPower + steering;
                rPower = rPower - steering;

                rightBackMotor.setPower(rPower);
                rightFrontMotor.setPower(rPower);
                leftBackMotor.setPower(lPower);
                leftFrontMotor.setPower(lPower);
            }

            hDriveMotor.setPower(0);
            hDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public float getError(float initialHeading){
        float error;
        error = initialHeading - imu.getAngularOrientation().firstAngle;
        while (error > 180){
            error -= 360;
        }
        while (error <= -180){
            error += 360;
        }
        return error;
    }

    public double getSteering(float error, double pCoefficent){
        double steering;
        steering = Range.clip(error * pCoefficent, 1.0, -1.0);
        return steering;
    }
}
