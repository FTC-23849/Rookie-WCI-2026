package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class RedAuto extends LinearOpMode {

    public static double GATE_CLOSED = 0.0;
    public static double GATE_OPENED = 0.3;
    public static double SHOOTER_SPEED = 0.9;

    ElapsedTime shooterTimer = new ElapsedTime();

    DcMotor LF;
    DcMotor LB;
    DcMotor RF;
    DcMotor RB;
    DcMotor intakeMotor;
    DcMotor upshooterMotor;
    DcMotor downshooterMotor;

    Servo gate;

    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d startPose = new Pose2d(-55.8,43.1, Math.toRadians(135));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);
        LF = hardwareMap.dcMotor.get("LF");
        LB = hardwareMap.dcMotor.get("LB");
        RF = hardwareMap.dcMotor.get("RF");
        RB = hardwareMap.dcMotor.get("RB");
        intakeMotor = hardwareMap.dcMotor.get("intakeMotor");
        upshooterMotor = hardwareMap.dcMotor.get("upshooterMotor");
        downshooterMotor = hardwareMap.dcMotor.get("downshooterMotor");

        GoBildaPinpointDriver pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        LF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        gate = hardwareMap.get(Servo.class, "gate");
        //TODO:test if robot drives correctly

        RF.setDirection(DcMotor.Direction.REVERSE);
        RB.setDirection(DcMotor.Direction.REVERSE);

        upshooterMotor.setDirection(DcMotor.Direction.REVERSE);
        downshooterMotor.setDirection(DcMotor.Direction.REVERSE);

        upshooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        downshooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        gate.setDirection(Servo.Direction.REVERSE);

        pinpoint.recalibrateIMU();
        sleep(1000);

        gate.setPosition(GATE_CLOSED);

        telemetry.addData("FINISHED", true);
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        TrajectoryActionBuilder scorePreloads = drive.actionBuilder(startPose)
                .strafeToSplineHeading(new Vector2d(-28,28), Math.toRadians(140));

        Actions.runBlocking(new SequentialAction(

                new ParallelAction(
                        scorePreloads.build(),
                        new setShooter(SHOOTER_SPEED)
                ),
                new setGate(GATE_OPENED),
                new setIntake(1.0),
                new SleepAction(1.8),
                new setGate(GATE_CLOSED)

        ));

        drive.updatePoseEstimate();
        drive.localizer.update();

        TrajectoryActionBuilder prepIntake1 = drive.actionBuilder(drive.localizer.getPose())
                .strafeToSplineHeading(new Vector2d(-10,12), Math.toRadians(90));

        Actions.runBlocking(new SequentialAction(

                prepIntake1.build()

        ));

        drive.updatePoseEstimate();
        drive.localizer.update();


        TrajectoryActionBuilder Intake1 = drive.actionBuilder(drive.localizer.getPose())
                .strafeToSplineHeading(new Vector2d(-10,55), Math.toRadians(90));

        Actions.runBlocking(new SequentialAction(

                Intake1.build()

        ));

        drive.updatePoseEstimate();
        drive.localizer.update();


        TrajectoryActionBuilder Shoot2 = drive.actionBuilder(drive.localizer.getPose())
                .strafeToSplineHeading(new Vector2d(-28,28), Math.toRadians(140));

        Actions.runBlocking(new SequentialAction(

                Shoot2.build(),

                new setGate(GATE_OPENED),
                new SleepAction(1.5),
                new setGate(GATE_CLOSED),
                new setShooter(0),
                new setIntake(0)

        ));

        drive.updatePoseEstimate();
        drive.localizer.update();

        TrajectoryActionBuilder Park = drive.actionBuilder(drive.localizer.getPose())
                .strafeToConstantHeading(new Vector2d(-12,45));

        Actions.runBlocking(new SequentialAction(

                Park.build()

        ));

        drive.updatePoseEstimate();
        drive.localizer.update();

    }

    public class setShooter implements Action {

        double power;

        public setShooter(double power){
            this.power = power;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {

            upshooterMotor.setPower(power);
            downshooterMotor.setPower(power);

            return false;
        }
    }

    public class setIntake implements Action {

        double power;

        public setIntake(double power) {
            this.power = power;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {


            intakeMotor.setPower(power);

            return false;
        }
    }

    public class setGate implements Action {

        double postion;

        public setGate(double postion) {
            this.postion = postion;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {


            gate.setPosition(postion);

            return false;
        }
    }

}
