# Java Face Detection

A simple Java application using JavaCV for real-time face detection via your webcam. Features a user-friendly Swing UI with live bounding boxes around detected faces and the ability to take screenshots.

## Features

- **Real-Time Detection:** Detects faces instantly from your webcam feed.
- **Bounding Boxes:** Highlights detected faces with red rectangles.
- **Camera Flip:** Option to mirror the camera view.
- **Screenshot:** Capture and save snapshots of the current view.
- **Easy-to-Use UI:** Start, stop, and screenshot buttons for seamless interaction.

## Installation

1. **Clone the Repository**
    ```bash
    git clone https://github.com/mattpapp/Face-Detection.git
    cd Face-Detection
    ```

2. **Build the Project**
    Ensure you have Maven installed, then run:
    ```bash
    mvn clean install
    ```

3. **Run the Application**
    ```bash
    mvn exec:java -Dexec.mainClass="com.example.facerecognition.FaceDetectionApp"
    ```
    *Or use your IDE to run the `FaceDetectionApp` class.*

## Usage

1. **Start Webcam Feed:** Click the **"Start"** button.
2. **Enable Camera Flip:** Check the **"Flip Camera"** box if needed.
3. **Detect Faces:** Red boxes will appear around detected faces.
4. **Take a Screenshot:** Click the **"Screenshot"** button to save the current frame.
5. **Stop Webcam Feed:** Click the **"Stop"** button when done.

## Acknowledgements

- [OpenCV](https://opencv.org/) for powerful computer vision tools.
- [JavaCV](https://github.com/bytedeco/javacv) for Java bindings to OpenCV.
