package org.firstinspires.ftc.teamcode.Testing_OpModes;

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
    DcMotor leftMotor, rightMotor;
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
        parameters.vuforiaLicenseKey = ""; // get one
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);

        leftMotor = hardwareMap.dcMotor.get("lMotor");
        rightMotor = hardwareMap.dcMotor.get("rMotor");
        leftGrabServo = hardwareMap.servo.get("lServo");
        rightGrabServo = hardwareMap.servo.get("rServo");

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Mode", "Waiting for start");
        telemetry.update();
        waitForStart();
        telemetry.addData("Mode", "Running");
        telemetry.update();

        leftMotor.setPower(0);
        rightMotor.setPower(0);

        relicTrackables.activate();
        RelicRecoveryVuMark seenMark;

        while (true) { // will break somewhere... hopefully on seeing a recognizable trackable
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                seenMark = vuMark;
                break;
            }
            idle();
        }

        if (seenMark == RelicRecoveryVuMark.LEFT) {
            telemetry.addData("Mark Seen = ", "LEFT");
            leftMotor.setTargetPosition(DISTANCE_TO_LEFT);
            rightMotor.setTargetPosition(DISTANCE_TO_LEFT + RIGHT_MOTOR_OFFSET);
        } else if (seenMark == RelicRecoveryVuMark.CENTER) {
            telemetry.addData("Mark Seen = ", "CENTER");
            leftMotor.setTargetPosition(DISTANCE_TO_CENTER);
            rightMotor.setTargetPosition(DISTANCE_TO_CENTER + RIGHT_MOTOR_OFFSET);
        } else {
            telemetry.addData("Mark Seen = ", "RIGHT");
            leftMotor.setTargetPosition(DISTANCE_TO_RIGHT);
            rightMotor.setTargetPosition(DISTANCE_TO_RIGHT + RIGHT_MOTOR_OFFSET);
        }
        telemetry.update();

        double targetPower = 0.25;
        accelerate(leftMotor, rightMotor, 0.25); // may not be necessary
        rightMotor.setPower(targetPower + 0.05); // drive in an arc to make rotation possible once near gate thing end
        while (opModeIsActive() && (leftMotor.isBusy() || rightMotor.isBusy())) {
            telemetry.addData("lPosition = ", leftMotor.getCurrentPosition());
            telemetry.addData("rPosition = ", rightMotor.getCurrentPosition());
            telemetry.update();
            idle();
        }
        leftMotor.setPower(0.0);
        rightMotor.setPower(0.0);
        int turnOffset = rightMotor.getCurrentPosition() - leftMotor.getCurrentPosition();

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setTargetPosition(TURN45 + turnOffset);
        rightMotor.setTargetPosition(-1 * (TURN45 - turnOffset));
        accelerate(leftMotor, rightMotor, 0.25);
        while (opModeIsActive() && (leftMotor.isBusy() || rightMotor.isBusy())) {
            telemetry.addData("lPosition = ", leftMotor.getCurrentPosition());
            telemetry.addData("rPosition = ", rightMotor.getCurrentPosition());
            telemetry.update();
            idle();
        }

        leftMotor.setPower(0.0);
        rightMotor.setPower(0.0);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // now move far enough forward to place block
        // now release block

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
