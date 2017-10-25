package org.firstinspires.ftc.teamcode.Comp_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
/**
 * Created by saeli on 10/9/2017.
 */
@Autonomous(name="Vuforia Practice", group="")
public class VuforiaPractice extends LinearOpMode {
    // oh boy
    VuforiaLocalizer vuforia;
    DcMotor leftFrontMotor, rightFrontMotor;
    Servo leftGrabServo, rightGrabServo;

    // these will have to be CALIBRATED
    final int DISTANCE_TO_LEFT = 5000; // encoder count
    final int DISTANCE_TO_CENTER = 5000; // encoder count
    final int DISTANCE_TO_RIGHT = 5000; // encoder count
    final int RIGHT_MOTOR_OFFSET = 500; // compensates for the turn
    final int TURN45 = 200; // how many counts to turn 45 degrees... if both motors do this in opposite directions,
    //should result in 90 degree turn?
    @Override
    public void runOpMode() throws InterruptedException {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AYjW+kn/////AAAAGckyQkdtk0g+vMt7+v21EQwSR82nxrrI34xlR+F75StLY+q3kjvWvgZiO0rBImulRIdCD4IjzWtqgZ8lPunOWuhUUi5eERTExNybyTwhn4GpdRr2XkcN+5uFD+5ZRoMfgx+z4RL4ONOLGWVMD30/VhwSM5vvkKB9C1VyGK0DyKcidSfxW8yhL1BKR2J0B5DtRtDW91hzalAEH2BfKE2+ee/F8f0HQ67DE5nnoVqrnT+THXWFb9W6OOBLszYdHTkUMtMV5U0RQxNuTBkeYGHtgcy17ULkQLY9Lnv0pqCLKdvlz4P3gtUAHPs/kr1cfzcaCS4iRY+ZlwxxLIKSazd0u4NSBjhH/f+zKJMaL/uVG2j4"; // get one
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);

        leftFrontMotor = hardwareMap.dcMotor.get("lFrontMotor");
        rightFrontMotor = hardwareMap.dcMotor.get("rFrontMotor");
        leftGrabServo = hardwareMap.servo.get("lServo");
        rightGrabServo = hardwareMap.servo.get("rServo");

        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Mode", "Waiting for start");
        telemetry.update();
        waitForStart();
        telemetry.addData("Mode", "Running");
        telemetry.update();

        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);

        relicTrackables.activate();
        RelicRecoveryVuMark seenMark=null;

        while (true && opModeIsActive()) { // will break somewhere... hopefully on seeing a recognizable trackable
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                telemetry.addData("Mark Seen = ", "NONE");
                seenMark = vuMark;
                break;
            }
            idle();
        }

        if (seenMark == RelicRecoveryVuMark.LEFT) {
            telemetry.addData("Mark Seen = ", "LEFT");
            leftFrontMotor.setTargetPosition(DISTANCE_TO_LEFT);
            rightFrontMotor.setTargetPosition(DISTANCE_TO_LEFT + RIGHT_MOTOR_OFFSET);
        } else if (seenMark == RelicRecoveryVuMark.CENTER) {
            telemetry.addData("Mark Seen = ", "CENTER");
            leftFrontMotor.setTargetPosition(DISTANCE_TO_CENTER);
            rightFrontMotor.setTargetPosition(DISTANCE_TO_CENTER + RIGHT_MOTOR_OFFSET);
        } else {
            telemetry.addData("Mark Seen = ", "RIGHT");
            leftFrontMotor.setTargetPosition(DISTANCE_TO_RIGHT);
            rightFrontMotor.setTargetPosition(DISTANCE_TO_RIGHT + RIGHT_MOTOR_OFFSET);
        }
        telemetry.update();
        /*
        double targetPower = 0.25;
        accelerate(leftFrontMotor, rightFrontMotor, 0.25); // may not be necessary
        rightFrontMotor.setPower(targetPower + 0.05); // drive in an arc to make rotation possible once near gate thing end
        while (opModeIsActive() && (leftFrontMotor.isBusy() || rightFrontMotor.isBusy())) {
            telemetry.addData("lPosition = ", leftFrontMotor.getCurrentPosition());
            telemetry.addData("rPosition = ", rightFrontMotor.getCurrentPosition());
            telemetry.update();
            idle();
        }
        leftFrontMotor.setPower(0.0);
        rightFrontMotor.setPower(0.0);
        int turnOffset = rightFrontMotor.getCurrentPosition() - leftFrontMotor.getCurrentPosition();

        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontMotor.setTargetPosition(TURN45 + turnOffset);
        rightFrontMotor.setTargetPosition(-1 * (TURN45 - turnOffset));
        accelerate(leftFrontMotor, rightFrontMotor, 0.25);
        while (opModeIsActive() && (leftFrontMotor.isBusy() || rightFrontMotor.isBusy())) {
            telemetry.addData("lPosition = ", leftFrontMotor.getCurrentPosition());
            telemetry.addData("rPosition = ", rightFrontMotor.getCurrentPosition());
            telemetry.update();
            idle();
        }

        leftFrontMotor.setPower(0.0);
        rightFrontMotor.setPower(0.0);
        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // now move far enough forward to place block
        // now release block
        */

    }

    public void accelerate(DcMotor l, DcMotor r, double targetPower) {
        double currPower;

        if (l.getPower() != r.getPower()) {
            currPower = (l.getPower() + r.getPower()) / 2;
        } else {
            currPower = l.getPower();
        }

        while (currPower < targetPower) {
            l.setPower(currPower);
            r.setPower(currPower);

            currPower += 0.01; // may be too slow
            idle();
        }
    }

}
