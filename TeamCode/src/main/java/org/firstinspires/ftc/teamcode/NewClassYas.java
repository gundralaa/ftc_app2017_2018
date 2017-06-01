package org.firstinspires.ftc.teamcode;

import android.hardware.Sensor;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsAnalogOpticalDistanceSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by abhin on 5/30/2017.
 */

public class NewClassYas {

    public DcMotor lMotor, rMotor;
    public ModernRoboticsI2cColorSensor mrColor;
    public ModernRoboticsI2cRangeSensor mrRange;
    public ModernRoboticsI2cGyro mrGyro;
    public AdafruitBNO055IMU adaGyro;
    public ModernRoboticsAnalogOpticalDistanceSensor leftOds, rightOds;

    HardwareMap lmap;

    public void init(HardwareMap map){
        lmap = map;



    }

}
