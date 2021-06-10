import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class NetworkHelper {
    private static int maxTransSize = 65535;

    //For writing large messages
    public static void write(byte[] data, OutputStream outputStream) throws IOException {
        outputStream.write(data);
    }

    //For sending small text message e.g. "ACK"
    public static void write(String msg, OutputStream outputStream) throws IOException {
        outputStream.write(msg.getBytes("UTF-8"));
    }

    public static String msgRead(InputStream inputStream) throws IOException {
        return new String(NetworkHelper.read(inputStream, 256), "UTF-8");
    }

    //For reading chunk of data
    public static byte[] read(InputStream inputStream, int filesize) throws IOException{
        byte[] bytes = new byte[3*filesize];
        int byteCount = inputStream.read(bytes);
        if (byteCount < 1000){
            System.out.println(new String(bytes,"UTF-8"));
        }
        byte[] tBytes = new byte[byteCount];

        System.arraycopy(bytes, 0, tBytes, 0, byteCount);
        return tBytes;
    }



}
