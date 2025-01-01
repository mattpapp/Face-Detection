package com.example.facerecognition;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;

import java.util.ArrayList;
import java.util.List;

public class FaceDetector {

    private final CascadeClassifier faceCascade;

    public FaceDetector() {
        // Load Haar cascade from resources
        String cascadePath = getClass()
                .getResource("/haarcascades/haarcascade_frontalface_alt.xml")
                .getPath();
        this.faceCascade = new CascadeClassifier(cascadePath);
    }

    /**
     * Detects faces in a grayscale Mat frame.
     * @param grayFrame the grayscale image
     * @return a list of Rect bounding boxes of faces
     */
    public List<Rect> detectFaces(Mat grayFrame) {
        RectVector faces = new RectVector();
        faceCascade.detectMultiScale(grayFrame, faces);

        List<Rect> faceList = new ArrayList<>();
        for (int i = 0; i < faces.size(); i++) {
            faceList.add(faces.get(i));
        }
        return faceList;
    }

    /**
     * Utility to draw rectangles around detected faces on the original (color) Mat.
     */
    public void drawFaceBoxes(Mat colorFrame, List<Rect> faces) {
        Scalar redColor = new Scalar(0, 0, 255, 0); // (B, G, R, alpha)
        for (Rect face : faces) {
            rectangle(colorFrame, face, redColor, 2, 8, 0);
        }
    }
}