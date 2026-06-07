package org.firstinspires.ftc.teamcode.hardware.mechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.stuyfission.fissionlib.util.Mechanism;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.PIDController;

@Config
public class Pivot extends Mechanism {
    // degrees
    public static double VERTICAL_POS = 90;
    public static double MIDDLE_POS = 45;
    public static double HORIZONTAL_POS = 0;

    public static double TOP = 90; // degrees
    public static int TICKS_PER_REV = 1000;

    public static double KP = 0.012;
    public static double KI = 0.05;
    public static double KD = 0.0003;
    public static double KF = 0.0035;

    public static double target = 0;
    public static double power = 0;

    private PIDController controller;

    private Slides slides;

    private final DcMotorEx[] motors = new DcMotorEx[2];

    public Pivot(LinearOpMode opMode, Slides slides) {
        this.opMode = opMode;
        this.slides = slides;
    }

    @Override
    public void init(HardwareMap hwMap) {
        controller = new PIDController(KP, KI, KD);

        motors[0] = hwMap.get(DcMotorEx.class, "pivotLeftMotor");
        motors[1] = hwMap.get(DcMotorEx.class, "pivotRightMotor");

        motors[0].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motors[1].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motors[0].setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER); // might be wrong RunMode
        motors[1].setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        motors[0].setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motors[1].setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        motors[0].setDirection(DcMotorEx.Direction.REVERSE);
        motors[1].setDirection(DcMotorEx.Direction.FORWARD);

        verticalPos();
    }

    public void verticalPos() {
        setTarget(VERTICAL_POS);
    }

    public void middlePos() {
        setTarget(MIDDLE_POS);
    }

    public void horizontalPos() {
        setTarget(HORIZONTAL_POS);
    }

    public void setTarget(double target) {
        Pivot.target = target;
    }

    public double getPosition() {
        return (double) motors[0].getCurrentPosition() / TICKS_PER_REV * 360; // degrees
    }

    private double feedForward(double position) {
        double slidesLength = slides.getPosition();
        double theta = (TOP - position) / 360 * 2 * Math.PI; // convert to radians
        return Math.sin(theta) * slidesLength;
    }

    public void update() {
        controller.setConstants(KP, KI, KD);
        controller.setTarget(target);
        double position = getPosition();
        power = controller.calculate(position) + feedForward(position) * KF;
        motors[0].setPower(power);
        motors[1].setPower(power);
    }

    @Override
    public void telemetry(Telemetry telemetry) {
        telemetry.addData("pivot position", getPosition());
        telemetry.addData("pivot target position", target);
        telemetry.addData("pivot power", power);
    }
}