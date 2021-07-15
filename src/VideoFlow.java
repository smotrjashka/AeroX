public class VideoFlow {
    private final String VIDEO_STREAM_PATH;
    private int height;
    private int width;

    public VideoFlow(final String path) {
        VIDEO_STREAM_PATH = path;
        height = 640;
        width = 480;
    }

    public VideoFlow(final String VIDEO_STREAM_PATH, final int height, final int width) {
        this.VIDEO_STREAM_PATH = VIDEO_STREAM_PATH;
        this.height = height;
        this.width = width;
    }

    public String getVIDEO_STREAM_PATH() {
        return VIDEO_STREAM_PATH;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
