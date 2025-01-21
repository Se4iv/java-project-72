package hexlet.code.util;

import java.net.URL;
public class NormalizedData {
    public static String getNormalizedUrl(URL url) {
        var protocol = url.getProtocol();
        var colonsAndSlashes = "://";
        var host = url.getHost();
        var port = url.getPort();
        return port != -1 ? protocol + colonsAndSlashes + host + ":" + port : protocol + colonsAndSlashes + host;
    }
}
