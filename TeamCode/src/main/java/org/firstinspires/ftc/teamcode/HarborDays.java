package org.firstinspires.ftc.teamcode;

import android.media.AudioManager;
import android.media.SoundPool;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhin on 9/1/2017.
 */
@TeleOp(name = "CompleteDemo", group = "")
public class HarborDays extends LinearOpMode {

    HardwareBot bot;
    float stickR, stickL;
    final float POWERMIN = -0.5f;
    final float POWERMAX = 0.5f;
    double[] values = {0.00, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24, 0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};
    int indexL, indexR;
    double powerL, powerR;
    VuforiaLocalizer vuforia;
    boolean redCurr, redPrev;
    public SoundPool moo;
    public int mooId;
    public boolean wasDown = true;

    @Override
    public void runOpMode() throws InterruptedException {

        bot = new HardwareBot();
        bot.init(hardwareMap);

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = "AYjW+kn/////AAAAGckyQkdtk0g+vMt7+v21EQwSR82nxrrI34xlR+F75StLY+q3kjvWvgZiO0rBImulRIdCD4IjzWtqgZ8lPunOWuhUUi5eERTExNybyTwhn4GpdRr2XkcN+5uFD+5ZRoMfgx+z4RL4ONOLGWVMD30/VhwSM5vvkKB9C1VyGK0DyKcidSfxW8yhL1BKR2J0B5DtRtDW91hzalAEH2BfKE2+ee/F8f0HQ67DE5nnoVqrnT+THXWFb9W6OOBLszYdHTkUMtMV5U0RQxNuTBkeYGHtgcy17ULkQLY9Lnv0pqCLKdvlz4P3gtUAHPs/kr1cfzcaCS4iRY+ZlwxxLIKSazd0u4NSBjhH/f+zKJMaL/uVG2j4";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        //parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables faces = this.vuforia.loadTrackablesFromAsset("ArtsWalk");
        VuforiaTrackable sadTarget = faces.get(0);
        sadTarget.setName("sad");  // Stones

        VuforiaTrackable happyTarget = faces.get(1);
        happyTarget.setName("happy");  // Chips

        VuforiaTrackable angryTarget = faces.get(2);
        angryTarget.setName("angry");

        /** For convenience, gather together all the trackable objects in one easily-iterable collection */
        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(faces);

        redCurr = false;
        redPrev = false;
        bot.mrColorReader.write8(3, 0);
        moo = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mooId = moo.load(hardwareMap.appContext, R.raw.cow_moo1, 1);

        telemetry.addLine("Initialization Complete, Waiting for Start");
        telemetry.update();

        waitForStart();

        faces.activate();

        while (opModeIsActive()) {

            if (!wasDown && gamepad1.dpad_down) {
                wasDown = true;
                moo.play(mooId, 1, 1, 1, 0, 1);
            }
            if (!gamepad1.dpad_down) {
                wasDown = false;
            }
            telemetry.addData("wasDown: ", wasDown);


            redCurr = isRed(bot.mrColorReader);
            if (redCurr || (redPrev && !(redCurr))) EmotionRecognition.angryFace(bot);

            if (redPrev && !(redCurr)) { // charge
                bot.lMotor.setPower(-0.5);
                bot.rMotor.setPower(-0.5);
                sleep(2000);
                bot.lMotor.setPower(0.0);
                bot.rMotor.setPower(0.0);
            }
            redPrev = redCurr;

            stickL = gamepad1.left_stick_y;
            stickR = gamepad1.right_stick_y;

            //powerR = stickR*stickR;
            //powerL = stickL*stickL;

            indexL = (int) Range.clip(Math.abs(stickL) * 16, 0, 16);
            indexR = (int) Range.clip(Math.abs(stickR) * 16, 0, 16);

            if (stickL < 0) {
                powerL = -values[indexL];
                //powerL = -powerL;
            } else {
                powerL = values[indexL];
            }

            if (stickR < 0) {
                powerR = -values[indexR];
                //powerR = -powerR;
            } else {
                powerR = values[indexR];
            }

            bot.lMotor.setPower(Range.clip(powerL, POWERMIN, POWERMAX));
            bot.rMotor.setPower(Range.clip(powerR, POWERMIN, POWERMAX));

            if (gamepad1.a) {
                EmotionRecognition.happyFace(bot);
            }
            if (gamepad1.b) {
                EmotionRecognition.sadFace(bot);
            }
            if (gamepad1.x) {
                EmotionRecognition.neutralFace(bot);
            }
            if (gamepad1.y) {
                EmotionRecognition.angryFace(bot);
            }
            /**

             if (gamepad1.right_trigger > 0.5){
             bot.shooter.setPower(1.0);
             } else {
             bot.shooter.setPower(0.0);
             }
             **/
            for (VuforiaTrackable trackable : allTrackables) {

                telemetry.addData(trackable.getName(), ((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible() ? "Visible" : "Not Visible");
                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                    switch (trackable.getName()) {
                        case ("sad"):
                            EmotionRecognition.sadFace(bot);
                            break;
                        case ("angry"):
                            EmotionRecognition.angryFace(bot);
                            break;
                        case ("happy"):
                            EmotionRecognition.happyFace(bot);
                            break;
                    }
                }

            }
            telemetry.update();
            idle();

        }
    }

    public boolean isRed(I2cDeviceSynch colorReader) {
        byte[] cache = colorReader.read(0x04, 1);  //Read is 0x04
        int colorNumber = cache[0] & 0xff;
        telemetry.addData("Color Number: ", colorNumber);

        if (colorNumber == 10 || colorNumber == 11) {
            return true;
        } else {
            return false;
        }
    }
}
