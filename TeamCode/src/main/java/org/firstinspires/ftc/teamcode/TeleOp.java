package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.sun.tools.javac.comp.Todo;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp")
public class TeleOp extends LinearOpMode {

    public static double GATE_CLOSED = 0.0;
    public static double GATE_OPENED = 0.3;
    public static double SHOOTER_SPEED = 1.0;
    public static boolean SHOOTER_ON = false;

    @Override
    public void runOpMode() throws InterruptedException {

        //double check that the motor names are accurate
        DcMotor LF = hardwareMap.dcMotor.get("LF");
        DcMotor LB = hardwareMap.dcMotor.get("LB");
        DcMotor RF = hardwareMap.dcMotor.get("RF");
        DcMotor RB = hardwareMap.dcMotor.get("RB");
        DcMotor intakeMotor = hardwareMap.dcMotor.get("intakeMotor");
        DcMotor upshooterMotor = hardwareMap.dcMotor.get("upshooterMotor");
        DcMotor downshooterMotor = hardwareMap.dcMotor.get("downshooterMotor");

        LF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Servo gate;
        gate = hardwareMap.get(Servo.class, "gate");
        //TODO:test if robot drives correctly

        RF.setDirection(DcMotor.Direction.REVERSE);
        RB.setDirection(DcMotor.Direction.REVERSE);

        upshooterMotor.setDirection(DcMotor.Direction.REVERSE);
        downshooterMotor.setDirection(DcMotor.Direction.REVERSE);

        upshooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        downshooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        gate.setDirection(Servo.Direction.REVERSE);


        waitForStart();
        //driving code below
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y  = -gamepad1.left_stick_y;
            //multiply the line below by 1.1 if needed bc it might be necessary but idk
            double x  =  gamepad1.left_stick_x;
            double rx =  gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

            double LFpower = (y + x + rx) / denominator;
            double LBpower = (y - x + rx) / denominator;
            double RFpower = (y - x - rx) / denominator;
            double RBpower = (y + x - rx) / denominator;

            LF.setPower(LFpower);
            LB.setPower(LBpower);
            RF.setPower(RFpower);
            RB.setPower(RBpower);

            //Intake
            if (gamepad1.right_trigger > 0.1) {

                intakeMotor.setPower(1.0);
                gate.setPosition(GATE_CLOSED);

            } else if (gamepad1.a) {

                intakeMotor.setPower(-1.0);
                gate.setPosition(GATE_CLOSED);

            }
            else if (gamepad1.left_trigger > 0.1) {

                intakeMotor.setPower(1.0);
                gate.setPosition(GATE_OPENED);

            }
            else {

                intakeMotor.setPower(0.0);
                gate.setPosition(GATE_CLOSED);

            }

            //Shooter

            if (gamepad1.leftBumperWasPressed()) {

                SHOOTER_ON = !SHOOTER_ON;

            } else {

                upshooterMotor.setPower(0.0);
                downshooterMotor.setPower(0.0);

            }

            if (SHOOTER_ON) {

                upshooterMotor.setPower(SHOOTER_SPEED);
                downshooterMotor.setPower(SHOOTER_SPEED);

            }



//            final double PUSH_POS = 0.8;
//            final double REST_POS = 0.2;
//
//            shooterServo.setPosition(REST_POS);
//
//            telemetry.addData("Status", "Initialized");
//            telemetry.update();
//
//            if (gamepad1.right_bumper) {
//                pusherMotor.setPower(1.0);
//            } else {
//                pusherMotor.setPower(0.0);
//            }
//
//
//            if (gamepad1.a) {
//                shooterServo.setPosition(PUSH_POS);
//            } else {
//                shooterServo.setPosition(REST_POS);
//            }



                }
            }

        }



