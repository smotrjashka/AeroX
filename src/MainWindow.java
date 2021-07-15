import org.bytedeco.javacv.CanvasFrame;

public class MainWindow {

    public static final String HD_VIDEO_STREAM_PATH = "rtsp://192.168.8.1:8554/video13";

    public MainWindow() {

    }

    public void show(){

        System.out.println("start gui");
        CanvasFrame canvasFrame = new CanvasFrame("cam1");
        canvasFrame.setVisible(true);

        VideoFlow hdVideoStream = new VideoFlow(HD_VIDEO_STREAM_PATH, 1920, 1080);

        VideoFrame videoFrame = new VideoFrame(hdVideoStream);
        System.out.println("show images");
        videoFrame.show(canvasFrame);
        System.out.println("gui finish");
    }
}
