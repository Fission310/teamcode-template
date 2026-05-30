package org.firstinspires.ftc.teamcode.hardware.mechanisms;

import org.firstinspires.ftc.teamcode.opmode.teleop.Controls;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.stuyfission.fissionlib.input.GamepadStatic;
import com.stuyfission.fissionlib.util.Mechanism;

@Config
public class Claw extends Mechanism {

    private Servo servo;

    public static double OPEN_POS = 0.5; // 0-1
    public static double CLOSE_POS = 0.8;

    public Claw(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    @Override
    public void init(HardwareMap hwMap) {
        servo = hwMap.get(Servo.class, "clawServo");

        open();
    }

    public void open() {
        servo.setPosition(OPEN_POS);
    }

    public void close() {
        servo.setPosition(CLOSE_POS);
    }

    @Override
    public void loop(Gamepad gamepad) {
        if (GamepadStatic.isButtonPressed(gamepad, Controls.SCORE)) {
            open();
        }
        if (GamepadStatic.isButtonPressed(gamepad, Controls.GRAB)) {
            close();
        }
    }
}
