package org.firstinspires.ftc.teamcode.hardware.mechanisms;

import com.pedropathing.follower.Follower;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.stuyfission.fissionlib.util.Mechanism;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Config
public class Drivetrain extends Mechanism {

    private Follower follower;

    public Drivetrain(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    @Override
    public void init(HardwareMap hwMap) {
        follower = Constants.createFollower(hwMap);
    }

    @Override
    public void loop(Gamepad gamepad) {
        follower.setTeleOpDrive(
                    -gamepad.left_stick_y,
                    -gamepad.left_stick_x,
                    -gamepad.right_stick_x,
                    true // Robot Centric
            );
        follower.update();
    }
}
