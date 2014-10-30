package muntaserahmed.wifisentry;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ServerActivity extends Activity {

    EditText serverUrl;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        serverUrl = (EditText) findViewById(R.id.server_url);
        sendButton = (Button) findViewById(R.id.send_button);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.server, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSendBtnClicked(View v) {
        if(v.getId() == R.id.send_button) {

            try {
                sendLightInstructions();
                Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // add parameter handling
    public void sendLightInstructions() throws JSONException {

        // Testing
        JSONObject mainObj = new JSONObject();
        JSONArray lightArray = new JSONArray();
        JSONObject lightObject = new JSONObject();

        lightObject.put("lightId", "1");
        lightObject.put("red", "1");
        lightObject.put("blue", "1");
        lightObject.put("green", "1");
        lightObject.put("intensity", "1");

        lightArray.put(lightObject);

        mainObj.put("lights", lightArray);
        mainObj.put("propagate", "true");

        try {
            StringEntity jsonEntity = new StringEntity(mainObj.toString());
            jsonEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            // pass in values from server edittext
            RestClient.post(getApplicationContext(), "10.0.1.56", "test", jsonEntity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If response is JSONObject instead of JSONArray
                    Log.d("RESPONSE", "OBJECT");
                    Toast.makeText(getApplicationContext(), "JSONObject", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("RESPONSE", "ARRAY");
                    Toast.makeText(getApplicationContext(), "JSONArray", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            Log.d("EXCEPTION", "ENCODING");
        }
    }

}
