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

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ServerActivity extends Activity {

    EditText redValText;
    EditText greenValText;
    EditText blueValText;
    EditText intensityValText;

    EditText serverUrl;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        serverUrl = (EditText) findViewById(R.id.server_url);
        sendButton = (Button) findViewById(R.id.send_button);

        redValText = (EditText) findViewById(R.id.red_edit);
        greenValText = (EditText) findViewById(R.id.green_edit);
        blueValText = (EditText) findViewById(R.id.blue_edit);
        intensityValText = (EditText) findViewById(R.id.intensity_edit);
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
        String baseUrl = serverUrl.getText().toString();
        if(v.getId() == R.id.send_button) {
            try {
                sendLightInstructions(baseUrl);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendLightInstructions(String baseUrl) throws JSONException {

        JSONObject mainObj = new JSONObject();
        JSONArray lightArray = new JSONArray();
        JSONObject lightObject = new JSONObject();

        lightObject.put("lightId", 1);
        lightObject.put("red", Integer.parseInt(redValText.getText().toString()));
        lightObject.put("green", Integer.parseInt(greenValText.getText().toString()));
        lightObject.put("blue", Integer.parseInt(blueValText.getText().toString()));
        lightObject.put("intensity", Integer.parseInt(intensityValText.getText().toString()));

        lightArray.put(lightObject);

        mainObj.put("lights", lightArray);
        mainObj.put("propagate", true);

        try {
            StringEntity jsonEntity = new StringEntity(mainObj.toString());
            jsonEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            // pass in values from server edittext
            RestClient.post(getApplicationContext(), baseUrl, "rpi", jsonEntity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Toast.makeText(getApplicationContext(), "JSONObject", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Toast.makeText(getApplicationContext(), "JSONArray", Toast.LENGTH_SHORT).show();
                }
            });

            Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_SHORT).show();

        } catch (UnsupportedEncodingException e) {
            Log.d("EXCEPTION:", "ENCODING");
        }
    }

}
