package muntaserahmed.wifisentry;

import android.content.Context;
import com.loopj.android.http.*;
import org.apache.http.HttpEntity;

public class RestClient {

    private static final String PROTOCOL = "http";
    private static final int PORT = 5000;

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String host, String endpoint, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(host, endpoint), params, responseHandler);
    }

    public static void post(Context context, String host, String endpoint, HttpEntity entity,
                            String contentType, AsyncHttpResponseHandler responseHandler) {
        client.post(context, getAbsoluteUrl(host, endpoint), entity, contentType, responseHandler);
    }

    private static String getAbsoluteUrl(String host, String endpoint) {
//        return PROTOCOL + "://" + host + ":" + Integer.toString(PORT) + "/" + endpoint;
        return "http://postcatcher.in/catchers/545405ee48e70e0200000761";
    }

}
