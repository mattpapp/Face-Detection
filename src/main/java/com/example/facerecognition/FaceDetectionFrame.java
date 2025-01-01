package com.example.facerecognition;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_core.flip;

public class FaceDetectionFrame extends JFrame {

    private final CameraService cameraService;
    private final FaceDetector faceDetector;

    private final JPanel videoPanel;
    private final JButton startButton;
    private final JButton stopButton;
    private final JButton screenshotButton;
    private final JLabel faceCountLabel;

    // For converting between Frame and Mat
    private final OpenCVFrameConverter.ToMat converter;
    // Use the Swing Timer explicitly
    private javax.swing.Timer timer;

    public FaceDetectionFrame() {
        super("Face Detection App");

        this.cameraService = new CameraService();
        this.faceDetector = new FaceDetector();
        this.converter = new OpenCVFrameConverter.ToMat();

        // UI Setup
        videoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // We'll draw the webcam image here
            }
        };
        videoPanel.setPreferredSize(new Dimension(640, 480));

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        screenshotButton = new JButton("Screenshot");
        faceCountLabel = new JLabel("Faces detected: 0");

        // Panel to hold buttons
        JPanel controlsPanel = new JPanel(new FlowLayout());
        controlsPanel.add(startButton);
        controlsPanel.add(stopButton);
        controlsPanel.add(screenshotButton);
        controlsPanel.add(faceCountLabel);

        // Layout for the frame
        setLayout(new BorderLayout());
        add(videoPanel, BorderLayout.CENTER);
        add(controlsPanel, BorderLayout.SOUTH);

        // Wire button actions
        startButton.addActionListener(this::onStart);
        stopButton.addActionListener(this::onStop);
        screenshotButton.addActionListener(this::onScreenshot);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // center on screen
    }

    private void onStart(ActionEvent e) {
        try {
            cameraService.start();
            // Create (or re-create) the Timer if needed
            if (timer == null) {
                // Fire every 30ms => ~33 FPS
                timer = new javax.swing.Timer(30, this::grabAndProcessFrame);
            }
            timer.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onStop(ActionEvent e) {
        if (timer != null) {
            timer.stop();
        }
        cameraService.stop();
        faceCountLabel.setText("Faces detected: 0");
        // Clear the videoPanel
        Graphics g = videoPanel.getGraphics();
        if (g != null) {
            g.clearRect(0, 0, videoPanel.getWidth(), videoPanel.getHeight());
        }
    }

    private void onScreenshot(ActionEvent e) {
        // Take a screenshot of the current content of videoPanel
        BufferedImage screenshot = new BufferedImage(
                videoPanel.getWidth(),
                videoPanel.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2 = screenshot.createGraphics();
        videoPanel.paint(g2); // paint current panel content onto the BufferedImage
        g2.dispose();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        File outputFile = new File("screenshot_" + timestamp + ".png");
        try {
            ImageIO.write(screenshot, "png", outputFile);
            System.out.println("Screenshot saved to: " + outputFile.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Called by Swing Timer ~every 30ms to grab a frame, detect faces, and paint the result.
     */
    private void grabAndProcessFrame(ActionEvent e) {
        if (!cameraService.isRunning()) return;

        try {
            Frame frame = cameraService.grabFrame();
            if (frame == null) return;

            // Convert Frame -> Mat
            Mat matColor = converter.convert(frame);
            if (matColor == null || matColor.empty()) return;

            // Flip horizontally (mirror effect)
            flip(matColor, matColor, 1);

            // Convert to grayscale for detection
            Mat matGray = new Mat();
            cvtColor(matColor, matGray, COLOR_BGR2GRAY);

            // Detect faces
            List<Rect> faces = faceDetector.detectFaces(matGray);
            faceDetector.drawFaceBoxes(matColor, faces);

            // Update label
            faceCountLabel.setText("Faces detected: " + faces.size());

            // Convert processed Mat to a BufferedImage
            BufferedImage image = matToBufferedImage(matColor);

            // Paint it on videoPanel
            Graphics g = videoPanel.getGraphics();
            if (g != null && image != null) {
                g.drawImage(image, 0, 0, videoPanel.getWidth(), videoPanel.getHeight(), null);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Utility method: Convert a Mat to BufferedImage by bridging through Frame.
     */
    private BufferedImage matToBufferedImage(Mat mat) {
        Frame frame = converter.convert(mat);
        if (frame == null) return null;
        return Java2DFrameUtils.toBufferedImage(frame);
    }

    /**
     * Static helper class for Frame -> BufferedImage
     */
    private static class Java2DFrameUtils extends org.bytedeco.javacv.Java2DFrameConverter {
        /**
         * Provide a convenient static method.
         */
        public static BufferedImage toBufferedImage(Frame frame) {
            try (Java2DFrameConverter converter = new Java2DFrameConverter()) {
                return converter.getBufferedImage(frame);
            }
        }
    }
}