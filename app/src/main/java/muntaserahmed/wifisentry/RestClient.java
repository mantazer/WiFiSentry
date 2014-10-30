package muntaserahmed.wifisentry;

import com.loopj.android.http.*;

/**
 * Created by muntaserahmed on 10/26/14.
 */

public class RestClient {

    private static final String PROTOCOL = "http";
    private static final int PORT = 5000;

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String host, String endpoint, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(host, endpoint), params, responseHandler);
    }

    public static void post(String host, String endpoint, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(host, endpoint), params, responseHandler);
    }

    private static String getAbsoluteUrl(String host, String endpoint) {
//        return PROTOCOL + "://" + host + Integer.toString(PORT) + "/" + endpoint;
        return "http://postcatcher.in/catchers/5451ab0a42bfb30200000c33";
    }

}
