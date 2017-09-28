package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhin on 6/9/2017.
 */
@Autonomous(name = "VuforiaDemo", group = "Tests")
public class EmotionRecognition extends LinearOpMode {

    HardwareBot bot;
    boolean isHappy, isSad, isAngry, isNeutral;
    String state = "Neutral";
    VuforiaLocalizer vuforia;

    @Override
    public void runOpMode() throws InterruptedException{

        bot = new HardwareBot();

        bot.init(hardwareMap);

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(com.qualcomm.ftcrobotcontroller.R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AYjW+kn/////AAAAGckyQkdtk0g+vMt7+v21EQwSR82nxrrI34xlR+F75StLY+q3kjvWvgZiO0rBImulRIdCD4IjzWtqgZ8lPunOWuhUUi5eERTExNybyTwhn4GpdRr2XkcN+5uFD+5ZRoMfgx+z4RL4ONOLGWVMD30/VhwSM5vvkKB9C1VyGK0DyKcidSfxW8yhL1BKR2J0B5DtRtDW91hzalAEH2BfKE2+ee/F8f0HQ67DE5nnoVqrnT+THXWFb9W6OOBLszYdHTkUMtMV5U0RQxNuTBkeYGHtgcy17ULkQLY9Lnv0pqCLKdvlz4P3gtUAHPs/kr1cfzcaCS4iRY+ZlwxxLIKSazd0u4NSBjhH/f+zKJMaL/uVG2j4";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables faces = this.vuforia.loadTrackablesFromAsset("ArtsWalk");
        VuforiaTrackable sadTarget = faces.get(0);
        sadTarget.setName("sad");  // Stones

        VuforiaTrackable happyTarget  = faces.get(1);
        happyTarget.setName("happy");  // Chips

        VuforiaTrackable angryTarget = faces.get(2);
        angryTarget.setName("angry");

        /** For convenience, gather together all the trackable objects in one easily-iterable collection */
        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(faces);

        telemetry.addLine("Initialization Complete, Waiting for Start");
        telemetry.update();

        waitForStart();

        faces.activate();

        while (opModeIsActive()){
            for(VuforiaTrackable trackable : allTrackables){

                telemetry.addData(trackable.getName(),((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible() ? "Visible" : "Not Visible");
                if(((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()){
                    switch (trackable.getName()){
                        case ("sad"):
                            sadFace(bot);
                            break;
                        case ("angry"):
                            angryFace(bot);
                            break;
                        case ("happy"):
                            happyFace(bot);
                            break;
                    }
                }

            }
            telemetry.update();
            idle();


        }

    }
    public static void happyFace(HardwareBot lbot){
        // Brow
        lbot.lBrow.setPosition(0.8f); // Neutral
        lbot.rBrow.setPosition(0.2f);
        //Mouth
        lbot.lMouth.setPosition(0.2f); // Up
        lbot.rMouth.setPosition(0.8f);
    }
    public static void sadFace(HardwareBot lbot){
        // Brow
        lbot.lBrow.setPosition(0.6f); // Down
        lbot.rBrow.setPosition(0.4f);
        // Mouth
        lbot.lMouth.setPosition(1.0f); //Down
        lbot.rMouth.setPosition(0.0f);
    }
    public static void angryFace(HardwareBot lbot){
        // Brow
        lbot.lBrow.setPosition(1.0f); // Up
        lbot.rBrow.setPosition(0.0f);
        // Mouth
        lbot.lMouth.setPosition(1.0f); // Down
        lbot.rMouth.setPosition(0.0f);
    }
    public static void neutralFace(HardwareBot lbot){
        // Brow
        lbot.lBrow.setPosition(0.8f); // Neutral
        lbot.rBrow.setPosition(0.2f);
        // Mouth
        lbot.lMouth.setPosition(0.5f); // Neutral
        lbot.rMouth.setPosition(0.5f);
    }
}
