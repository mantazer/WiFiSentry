package muntaserahmed.wifisentry;

import android.app.Activity;
import android.content.SharedPreferences;
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

    public static final String SERVER_PREFERENCES = "savedServer";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    EditText serverUrl;
    Button saveButton;

    Button redButton;
    Button greenButton;
    Button blueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        preferences = getSharedPreferences(SERVER_PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();

        serverUrl = (EditText) findViewById(R.id.server_url);
        saveButton = (Button) findViewById(R.id.save_button);

        redButton = (Button) findViewById(R.id.redButton);
        greenButton = (Button) findViewById(R.id.greenButton);
        blueButton = (Button) findViewById(R.id.blueButton);
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

    public void onSaveBtnClicked(View v) {
        String ip = serverUrl.getText().toString();
        editor.putString("serverIp", ip);
        editor.commit();
        Toast.makeText(getApplicationContext(), "Server saved", Toast.LENGTH_SHORT).show();
    }

    public void onTestBtnClicked(View v) throws JSONException {

        if (!preferences.contains("serverIp")) {
            Toast.makeText(getApplicationContext(), "Add a server first", Toast.LENGTH_SHORT).show();
            return;
        }

        String serverAddress = preferences.getString("serverIp", null);

        JSONObject mainObj = new JSONObject();
        JSONArray lightArray = new JSONArray();
        JSONObject lightObject = new JSONObject();

        lightObject.put("lightId", 1);
        lightObject.put("intensity", 1);

        switch (v.getId()) {
            case R.id.redButton:
                lightObject.put("red", 231);
                lightObject.put("green", 76);
                lightObject.put("blue", 60);
                break;
            case R.id.greenButton:
                lightObject.put("red", 46);
                lightObject.put("green", 204);
                lightObject.put("blue",113);
                break;
            case R.id.blueButton:
                lightObject.put("red", 52);
                lightObject.put("green", 152);
                lightObject.put("blue", 219);
                break;
            default:
                lightObject.put("red", 255);
                lightObject.put("green", 255);
                lightObject.put("blue", 255);
                break;
        }

        lightArray.put(lightObject);
        mainObj.put("lights", lightArray);
        mainObj.put("propagate", true);

        try {
            StringEntity jsonEntity = new StringEntity(mainObj.toString());
            jsonEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            RestClient.post(getApplicationContext(), serverAddress, "rpi", jsonEntity, "application/json", new JsonHttpResponseHandler() {
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
