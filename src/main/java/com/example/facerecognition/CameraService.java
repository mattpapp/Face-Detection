package com.example.facerecognition;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;

/**
 * Handles webcam capture start/stop, returning frames for processing.
 */
public class CameraService {

    private FrameGrabber grabber;
    private boolean running;

    public CameraService() {
        // By default, use the first camera (index 0)
        grabber = new OpenCVFrameGrabber(0);
        this.running = false;
    }

    /**
     * Start capturing from the webcam.
     */
    public void start() throws FrameGrabber.Exception {
        if (!running) {
            grabber.start();
            running = true;
        }
    }

    /**
     * Grab a single frame from the webcam.
     */
    public Frame grabFrame() throws FrameGrabber.Exception {
        if (running) {
            return grabber.grab();
        }
        return null;
    }

    /**
     * Stop capturing and release the webcam.
     */
    public void stop() {
        if (running) {
            try {
                grabber.stop();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
        running = false;
    }

    /**
     * Is the camera currently capturing frames?
     */
    public boolean isRunning() {
        return running;
    }
}