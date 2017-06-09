package org.firstinspires.ftc.teamcode;

import android.hardware.Sensor;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsAnalogOpticalDistanceSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by abhin on 5/30/2017.
 */

public class NewClassYas {

    public DcMotor lMotor, rMotor;
    public ColorSensor mrColor;
    public ModernRoboticsI2cRangeSensor mrRange;
    public ModernRoboticsI2cGyro mrGyro;
    public AdafruitBNO055IMU adaGyro;
    public OpticalDistanceSensor leftOds, rightOds;

    HardwareMap lmap;

    public void init(HardwareMap map){
        lmap = map;

        lMotor = lmap.dcMotor.get("lMotor");
        rMotor = lmap.dcMotor.get("rMotor");

        mrColor = lmap.colorSensor.get("mrColor");
        mrRange = lmap.get(ModernRoboticsI2cRangeSensor.class,"mrRange");
        mrGyro = lmap.get(ModernRoboticsI2cGyro.class,"mrGyro");

        leftOds = lmap.opticalDistanceSensor.get("leftOds");
        rightOds = lmap.opticalDistanceSensor.get("rightOds");



    }

}
