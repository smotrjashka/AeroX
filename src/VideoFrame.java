import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Date;

public class VideoFrame {
    private VideoFlow hdVideoFlow;
    FFmpegFrameGrabber grabberHD;
    private static volatile Thread playThread;
    private CanvasFrame canvasFrame;

    private static final double SC16 = (double) 0x7FFF + 0.4999999999999999;
    Java2DFrameConverter paintConverter = new Java2DFrameConverter();


    public VideoFrame(VideoFlow videoFlow) {
        this.hdVideoFlow = videoFlow;
        grabberHD = new FFmpegFrameGrabber(hdVideoFlow.getVIDEO_STREAM_PATH());
    }

    public void show(CanvasFrame canvasFrame) {
        System.out.println("video frame show");
        this.canvasFrame = canvasFrame;
        playThread = new Thread(() -> runVideo(hdVideoFlow));

        playThread.start();
    }

    protected void stop(){
        try {
            grabberHD.stop();
            grabberHD.release();
        } catch (Exception e) {
            System.out.println("problem with stop");
            e.printStackTrace();
        }
    }

    protected void runVideo(VideoFlow videoFlow){
            System.out.println("thread started");
            try {
                System.out.println("thread try started");
                grabberHD.setImageHeight(videoFlow.getHeight());
                grabberHD.setImageWidth(videoFlow.getWidth());
               /* grabberHD.setVideoCodec();
                grabberHD.setVideoBitrate();*/
                grabberHD.setFrameRate(30);
                grabberHD.start();
                long millis = System.currentTimeMillis();
                System.out.println("millis start " + millis);
                System.out.println("inf " + grabberHD.getVideoCodecName() + " " + grabberHD.getVideoBitrate());

                while (!Thread.interrupted()) {
                    Frame frame = grabberHD.grab();
                    BufferedImage image = paintConverter.convert(frame);

                  /*  if (frame == null) {
                        System.out.println("frame is null, break!");    //TODO wait for more that 1 null frame
                        break;
                    }else*/ if (frame.image != null) {
                        canvasFrame.showImage(image);


                    } else if (frame.samples != null) {
                        System.out.println("image null but sample not null");
                        FloatBuffer channelSamplesFloatBuffer = (FloatBuffer) frame.samples[0];
                        channelSamplesFloatBuffer.rewind();

                        ByteBuffer outBuffer = ByteBuffer.allocate(channelSamplesFloatBuffer.capacity() * 2);

                        for (int i = 0; i < channelSamplesFloatBuffer.capacity(); i++) {

                            // Could be replaced with: short val = (short) (channelSamplesFloatBuffer.get(i) * Short.MAX_VALUE);
                            short val = (short) ((double) channelSamplesFloatBuffer.get(i) * SC16);
                            outBuffer.putShort(val);
                        }
                    }

                    System.out.println("diff " + (System.currentTimeMillis() - millis));
                    millis = System.currentTimeMillis();
                }

                VideoFrame.this.stop();

            } catch (Exception e) {
                System.out.println("video play exception for " + videoFlow.getVIDEO_STREAM_PATH());
                e.printStackTrace();
            }
    }

}
