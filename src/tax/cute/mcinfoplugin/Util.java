package tax.cute.mcinfoplugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Util {
    public static byte[] getWebBytes(String url) throws IOException {
        URL u = new URL(url);
        HttpURLConnection http = (HttpURLConnection)u.openConnection();
        if(http.getResponseCode() != 200) return null;
        InputStream in = http.getInputStream();
        byte[] bytes = in.readAllBytes();
        in.close();
        return bytes;
    }
}
