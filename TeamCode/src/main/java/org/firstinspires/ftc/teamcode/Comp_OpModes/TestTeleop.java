package org.firstinspires.ftc.teamcode.Comp_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by saeli on 12/11/2017.
 */
@TeleOp(name = "TeleOpTest", group = "Tests")
public class TestTeleop extends LinearOpMode{
    DcMotor leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor, hDriveMotor, linearSlideMotor, relicSlide;
    Servo leftGrabServo, rightGrabServo, horizontalJewel, relicGrabber, relicRotater, verticalJewel;
    double lPower, rPower, hPower, forwardPower, turningPower, lTrigger, rTrigger;
    ColorSensor colorSensor;
    boolean buttonWasOffX = true;
    boolean buttonWasOffY = true;
    double leftPosition = 1.0;
    double rightPosition = 0.0;
    /*
    private enum stickConfig{
        TANK, // Uses the two sticks to control each side and the left and right triggers to control the H-Drive
        ARCADE // Uses the right stick to control forward and turning movements and the left x for H-Drive
    };
    */
    //stickConfig config = stickConfig.TANK;


    @Override
    public void runOpMode() {
        leftFrontMotor = hardwareMap.dcMotor.get("lFrontMotor");
        rightFrontMotor = hardwareMap.dcMotor.get("rFrontMotor");
        leftBackMotor = hardwareMap.dcMotor.get("lBackMotor");
        rightBackMotor = hardwareMap.dcMotor.get("rBackMotor");
        hDriveMotor = hardwareMap.dcMotor.get("hDriveMotor");
        linearSlideMotor = hardwareMap.dcMotor.get("linearSlideMotor");
        relicSlide = hardwareMap.dcMotor.get("relicSlide");

        leftGrabServo = hardwareMap.servo.get("lServo");
        rightGrabServo = hardwareMap.servo.get("rServo");
        verticalJewel = hardwareMap.servo.get("vJewel"); // continuous
        horizontalJewel = hardwareMap.servo.get("hJewel"); // 180
        relicRotater = hardwareMap.servo.get("rRotater");
        relicGrabber = hardwareMap.servo.get("rGrabber");
        verticalJewel = hardwareMap.servo.get("vJewel");
        horizontalJewel = hardwareMap.servo.get("hJewel");
        colorSensor = hardwareMap.get(ColorSensor.class, "sensor_color");
/*
        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
*/

        leftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontMotor.setDirection(DcMotor.Direction.REVERSE);
/*
        leftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
*/
        rightBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);

        /*leftBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);*/

        telemetry.addData("Mode", "Waiting for start...");
        telemetry.update();
        waitForStart();
        telemetry.addData("Mode", "Started");

        horizontalJewel.setPosition(0);
        verticalJewel.setPosition(0);
        leftGrabServo.setPosition(leftPosition);
        rightGrabServo.setPosition(rightPosition);
        horizontalJewel.setPosition(0.0);
        relicRotater.setPosition(0.6);
        relicGrabber.setPosition(0.0);

        while (opModeIsActive()){
            if(gamepad1.a){
                horizontalJewel.setPosition(0.5);
            }
            if(gamepad1.b){
                horizontalJewel.setPosition(1);
            }
            if (gamepad1.y){
                verticalJewel.setPosition(0.5);
            }
            if (gamepad1.x){
                verticalJewel.setPosition(1);
            }
        }
    }
}
