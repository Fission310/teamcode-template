package org.firstinspires.ftc.teamcode.hardware.mechanisms;

import java.util.ArrayList;
import java.util.List;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.stuyfission.fissionlib.util.Mechanism;

public class Limelight extends Mechanism {
    public static int PIPELINE = 0;
    private Limelight3A limelight;
    private ArrayList<Element> elements = new ArrayList<>();
    private Color targetColor;

    public class Element {
        public List<Double> topLeft;
        public List<Double> topRight;
        public List<Double> bottomLeft;
        public List<Double> bottomRight;
        public Color color;

        public Element(List<Double> topLeft, List<Double> topRight, List<Double> bottomLeft, List<Double> bottomRight, Color color) {
            this.topLeft = topLeft;
            this.topRight = topRight;
            this.bottomLeft = bottomLeft;
            this.bottomRight = bottomRight;
            this.color = color;
        }
    }

    public enum Color {
        RED, BLUE
    }

    public Limelight(LinearOpMode opMode, Color targetColor) {
        this.opMode = opMode;
        this.targetColor = targetColor;
    }

    @Override
    public void init(HardwareMap hwMap) {
        limelight = hwMap.get(Limelight3A.class, "limelight");

        limelight.setPollRateHz(100);

        limelight.pipelineSwitch(PIPELINE);
        limelight.start();
    }

    public void update() {
        LLResult result = limelight.getLatestResult();
        if (result == null)
            return;

        List<LLResultTypes.DetectorResult> detections = result.getDetectorResults();

        elements = new ArrayList<>();

        for (LLResultTypes.DetectorResult detection : detections) {
            Color color = switch (detection.getClassId()) {
                case 0 -> Color.BLUE;
                case 1 -> Color.RED;
                default -> null;
            };

            if (color != targetColor) {
                continue;
            }

            List<List<Double>> corners = detection.getTargetCorners();
            if (corners == null || corners.size() < 4) {
                continue;
            }

            elements.add(new Element(corners.get(0), corners.get(1), corners.get(2), corners.get(3), color));
        }
    }

    @Override
    public void telemetry(Telemetry telemetry) {
        telemetry.addData("Number of game elements detected", elements.size());
        telemetry.update();
    }
}