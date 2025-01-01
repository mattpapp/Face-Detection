package com.example.facerecognition;

import javax.swing.*;

public class FaceDetectionApp {

    public static void main(String[] args) {
        // For nicer GUI on some systems
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignore) {}

        SwingUtilities.invokeLater(() -> {
            FaceDetectionFrame frame = new FaceDetectionFrame();
            frame.setVisible(true);
        });
    }
}