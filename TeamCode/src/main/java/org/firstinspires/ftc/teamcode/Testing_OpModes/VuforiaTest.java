package org.firstinspires.ftc.teamcode.Testing_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Resources.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhin on 10/8/2017.
 */

public class VuforiaTest extends LinearOpMode {

    VuforiaLocalizer vuforia;

    @Override
    public void runOpMode() throws InterruptedException {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = Strings.VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicVuMark = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable templateVuMark = relicVuMark.get(0);
        templateVuMark.setName("TemplateVuMark");

        waitForStart();
        relicVuMark.activate();

        while (opModeIsActive()){
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(templateVuMark);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN){
                telemetry.addData("VuMark", "%s is visible", vuMark);
                telemetry.update();
            }
            idle();

        }
    }
}
