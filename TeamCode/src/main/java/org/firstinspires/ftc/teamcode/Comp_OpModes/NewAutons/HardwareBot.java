package org.firstinspires.ftc.teamcode.Comp_OpModes.NewAutons;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by saeli on 10/23/2017.
 */

public class HardwareBot {
    public VuforiaLocalizer vuforia;
    public VuforiaTrackables relicTrackables;
    VuforiaTrackable relicTemplate;
    public DcMotor leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor, hDriveMotor, linearSlideMotor;
    public Servo leftGrabServo, rightGrabServo, verticalJewel, horizontalJewel, relicRotater;
    public BNO055IMU imu;
    public HardwareMap lmap;
    public ColorSensor colorSensor;

    public void init(HardwareMap map) {
        lmap = map;
        //Trolled
        // Color sensor
        colorSensor = lmap.get(ColorSensor.class, "sensor_color");

        // Motors
        leftFrontMotor = lmap.dcMotor.get("lFrontMotor");
        rightFrontMotor = lmap.dcMotor.get("rFrontMotor");
        leftBackMotor = lmap.dcMotor.get("lBackMotor");
        rightBackMotor = lmap.dcMotor.get("rBackMotor");
        hDriveMotor = lmap.dcMotor.get("hDriveMotor");
        linearSlideMotor = lmap.dcMotor.get("linearSlideMotor");
        leftGrabServo = lmap.servo.get("lServo");
        rightGrabServo = lmap.servo.get("rServo");
        verticalJewel = lmap.servo.get("vJewel");
        horizontalJewel = lmap.servo.get("hJewel");
        relicRotater = lmap.servo.get("rRotater");

        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);


        leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackMotor.setDirection(DcMotor.Direction.REVERSE);

        leftBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Vuforia
        int cameraMonitorViewId = lmap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", lmap.appContext.getPackageName());
        VuforiaLocalizer.Parameters vufParameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        vufParameters.vuforiaLicenseKey = "AYjW+kn/////AAAAGckyQkdtk0g+vMt7+v21EQwSR82nxrrI34xlR+F75StLY+q3kjvWvgZiO0rBImulRIdCD4IjzWtqgZ8lPunOWuhUUi5eERTExNybyTwhn4GpdRr2XkcN+5uFD+5ZRoMfgx+z4RL4ONOLGWVMD30/VhwSM5vvkKB9C1VyGK0DyKcidSfxW8yhL1BKR2J0B5DtRtDW91hzalAEH2BfKE2+ee/F8f0HQ67DE5nnoVqrnT+THXWFb9W6OOBLszYdHTkUMtMV5U0RQxNuTBkeYGHtgcy17ULkQLY9Lnv0pqCLKdvlz4P3gtUAHPs/kr1cfzcaCS4iRY+ZlwxxLIKSazd0u4NSBjhH/f+zKJMaL/uVG2j4"; // get one
        vufParameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(vufParameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);

        // IMU
        BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuParameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        imuParameters.loggingEnabled      = true;
        imuParameters.loggingTag          = "IMU";
        imuParameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = lmap.get(BNO055IMU.class, "imu");
        imu.initialize(imuParameters);
    }
}
