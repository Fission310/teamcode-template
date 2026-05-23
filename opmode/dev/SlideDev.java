package org.firstinspires.ftc.teamcode.opmode.dev;

import org.firstinspires.ftc.teamcode.hardware.mechanisms.Slides;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Slides test", group = "dev")
public class SlideDev extends LinearOpMode {

    private Slides slides = new Slides(this);

    @Override
    public void runOpMode() throws InterruptedException {
        slides.init(hardwareMap);
        
        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            slides.loop(gamepad1);
        }
    }
}
