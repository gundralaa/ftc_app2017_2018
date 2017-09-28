package org.firstinspires.ftc.teamcode.Testing_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.Harbor_Days.HardwareBot;
import org.firstinspires.ftc.teamcode.Harbor_Days.EmotionRecognition;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by abhin on 8/31/2017.
 */
@Autonomous(name="Red Charge", group="Tests" )
public class RedCharge extends LinearOpMode {
    HardwareBot bot;
    I2cDeviceSynch colorReader;
    DcMotor leftMotor, rightMotor;

    boolean seeRed;
    boolean chargeSet;

    @Override
    public void runOpMode() throws InterruptedException {

        bot = new HardwareBot();

        bot.init(hardwareMap);
        colorReader = bot.mrColorReader;
        leftMotor = bot.lMotor;
        rightMotor = bot.rMotor;

        int colorNumber;
        byte[] cache;

        chargeSet = false;

        waitForStart();
        colorReader.write8(3, 0);

        while (opModeIsActive()) {

            seeRed = isRed(colorReader);

            // if red is seen, wait until red is not seen, then execute the charge
            if (seeRed) {
                EmotionRecognition.angryFace(bot);
            }
            else {
                EmotionRecognition.neutralFace(bot);
            }

            while (seeRed) {
                chargeSet = true;
                seeRed = isRed(colorReader);
            }

            // execute charge if charge is set
            if (chargeSet) {
                leftMotor.setPower(-0.5);
                rightMotor.setPower(-0.5);
                sleep(2000);
                leftMotor.setPower(0);
                rightMotor.setPower(0);
            }
            chargeSet = false;
            idle();
        }


    }

    public boolean isRed(I2cDeviceSynch colorReader) {
        byte[] cache = colorReader.read(0x04, 1);  //Read is 0x04
        int colorNumber = cache[0] & 0xff;
        telemetry.addData("Color Number: ", colorNumber);
        telemetry.update();

        if (colorNumber == 10 || colorNumber == 11) {
            return true;
        }
        else {
            return false;
        }

    }

}
