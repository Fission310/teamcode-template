package org.firstinspires.ftc.teamcode.hardware.mechanisms;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.opmode.teleop.Controls;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.stuyfission.fissionlib.command.Command;
import com.stuyfission.fissionlib.command.CommandSequence;
import com.stuyfission.fissionlib.input.GamepadStatic;
import com.stuyfission.fissionlib.util.Mechanism;

@Config
public class Scoring extends Mechanism {

    private Claw claw = new Claw(opMode);
    private Drivetrain drive = new Drivetrain(opMode);
    private Slides slides = new Slides(opMode);

    public static double SCORE_DELAY = 0.4;

    private State state = State.INTAKE;

    private enum State {
        INTAKE,
        SCORING
    }

    public Scoring(LinearOpMode opMode) {
        this.opMode = opMode;
    }


    private Command scoreCommand = () -> claw.open();
    private Command retractSlidesCommand = () -> slides.low();
    private CommandSequence scoreSequence = new CommandSequence()
            .addCommand(scoreCommand)
            .addWaitCommand(SCORE_DELAY)
            .addCommand(retractSlidesCommand)
            .build();

    @Override
    public void init(HardwareMap hwMap) {
        claw.init(hwMap);
        drive.init(hwMap);
        slides.init(hwMap);
    }

    @Override
    public void telemetry(Telemetry telemetry) {
        slides.telemetry(telemetry);
        telemetry.addData("State", state);
        telemetry.update();
    }

    @Override
    public void loop(Gamepad gamepad) {
        drive.loop(gamepad);

        slides.update();

        switch (state) {
            case INTAKE:
                claw.loop(gamepad);
                break;
            case SCORING:
                if (GamepadStatic.isButtonPressed(gamepad, Controls.SCORE)) {
                    scoreSequence.trigger();
                }
                break;
        }
    }
}
