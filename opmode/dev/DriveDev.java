package org.firstinspires.ftc.teamcode.opmode.dev;

import org.firstinspires.ftc.teamcode.hardware.mechanisms.Drivetrain;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "DriveTrain test", group = "dev")
public class DriveDev extends LinearOpMode {

    private Drivetrain drivetrain = new Drivetrain(this);

    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain.init(hardwareMap);

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            drivetrain.loop(gamepad1);
        }
    }
}
