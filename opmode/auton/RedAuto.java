package org.firstinspires.ftc.teamcode.opmode.auton;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.stuyfission.fissionlib.command.AutoCommandMachine;
import com.stuyfission.fissionlib.command.Command;
import com.stuyfission.fissionlib.command.CommandSequence;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.teamcode.hardware.mechanisms.Claw;
import org.firstinspires.ftc.teamcode.hardware.mechanisms.Slides;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Red auto", group = "Red")
public class RedAuto extends LinearOpMode {

    private Follower follower;
    private Claw claw = new Claw(this);
    private Slides slides = new Slides(this);

    private final Pose startPose = new Pose(22, 122, Math.toRadians(324));
    private final Pose scorePose = new Pose(60, 84, Math.toRadians(135));
    private final Pose parkPose = new Pose(60, 105);

    private PathChain scorePreload;
    private PathChain park;

    private Command scorePathCommand = () -> follower.followPath(scorePreload, true);
    private Command parkPathCommand = () -> follower.followPath(park, true);
    private Command raiseSlidesCommand = () -> slides.high();
    private Command releaseCommand = () -> claw.open();

    private CommandSequence scorePathSequence = new CommandSequence().addCommand(scorePathCommand).build();
    private CommandSequence scoreSequence = new CommandSequence()
        .addCommand(raiseSlidesCommand)
        .addWaitCommand(2)
        .addCommand(releaseCommand)
        .build();
    private CommandSequence parkPathSequence = new CommandSequence().addCommand(parkPathCommand).build();

    private AutoCommandMachine commandMachine = new AutoCommandMachine()
            .addCommandSequence(scorePathSequence)
            .addCommandSequence(scoreSequence)
            .addCommandSequence(parkPathSequence)
            .build();

    public void buildPaths() {
        scorePreload = follower.pathBuilder()
                .addPath(new BezierLine(startPose, scorePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();

        park = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, parkPose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading())
                .build();
    }

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        follower = Constants.createFollower(hardwareMap);

        claw.init(hardwareMap);
        slides.init(hardwareMap);

        buildPaths();
        follower.setStartingPose(startPose);

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            follower.update();
            slides.update();

            commandMachine.run(follower.isBusy());

            telemetry.addData("x", follower.getPose().getX());
            telemetry.addData("y", follower.getPose().getY());
            telemetry.addData("heading", follower.getPose().getHeading());
            telemetry.update();
        }
    }
}
