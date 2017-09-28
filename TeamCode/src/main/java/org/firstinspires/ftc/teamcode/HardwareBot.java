package org.firstinspires.ftc.teamcode;

import android.hardware.Sensor;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsAnalogOpticalDistanceSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static com.qualcomm.ftcrobotcontroller.R.id.cameraMonitorViewId;

/**
 * Created by abhin on 5/30/2017.
 */

public class HardwareBot {

    public DcMotor lMotor, rMotor, shooter;
    public Servo rBrow, lBrow, rMouth, lMouth;
    //public ColorSensor mrColor;
    public I2cDevice mrColor;
    public I2cDeviceSynch mrColorReader;
    public ModernRoboticsI2cRangeSensor mrRange;
    private GyroSensor gyro;
    public ModernRoboticsI2cGyro mrGyro;
    public AdafruitBNO055IMU adaGyro;
    public OpticalDistanceSensor leftOds, rightOds;

    HardwareMap lmap;

    public void init(HardwareMap map){

        lmap = map;

        // Motors
        lMotor = lmap.dcMotor.get("lMotor");
        rMotor = lmap.dcMotor.get("rMotor");
        //shooter = lmap.dcMotor.get("Shooter");
        rMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Sensors
        //mrColor = lmap.colorSensor.get("mrColor");
        mrColor = lmap.i2cDevice.get("mrColor");
        mrColorReader = new I2cDeviceSynchImpl(mrColor, I2cAddr.create8bit(0x3a),false);
        mrColorReader.engage();
        mrColorReader.write8(3,1);

        mrRange = lmap.get(ModernRoboticsI2cRangeSensor.class,"mrRange");
        gyro = lmap.gyroSensor.get("mrGyro");
        mrGyro = (ModernRoboticsI2cGyro)gyro;

        leftOds = lmap.opticalDistanceSensor.get("leftOds");
        rightOds = lmap.opticalDistanceSensor.get("rightOds");

        //adaGyro = lmap.get(AdafruitBNO055IMU.class, "adaGyro");

        // Servos
        rBrow = lmap.servo.get("rBrow");
        lBrow = lmap.servo.get("lBrow");
        rMouth = lmap.servo.get("rMouth");
        lMouth = lmap.servo.get("lMouth");

        //lBrow.setDirection(Servo.Direction.REVERSE);
        //lMouth.setDirection(Servo.Direction.REVERSE);

        // Initialize and Calibrate
        lBrow.setPosition(0.8f); // Neutral
        rBrow.setPosition(0.2f);
        //Mouth
        lMouth.setPosition(0.2f); // Up
        rMouth.setPosition(0.8f);

    }

}
