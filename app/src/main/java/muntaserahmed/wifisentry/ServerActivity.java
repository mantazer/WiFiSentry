package muntaserahmed.wifisentry;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        Toast.makeText(getApplicationContext(), "Server Saved", Toast.LENGTH_SHORT).show();
    }


//    public void onSendBtnClicked(View v) {
//        String baseUrl = serverUrl.getText().toString();
//        if(v.getId() == R.id.save_button) {
//            try {
//                sendLightInstructions(baseUrl);
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    public void sendLightInstructions(String baseUrl) throws JSONException {
//
//        JSONObject mainObj = new JSONObject();
//        JSONArray lightArray = new JSONArray();
//        JSONObject lightObject = new JSONObject();
//
//        lightObject.put("lightId", 1);
//        lightObject.put("red", Integer.parseInt(redValText.getText().toString()));
//        lightObject.put("green", Integer.parseInt(greenValText.getText().toString()));
//        lightObject.put("blue", Integer.parseInt(blueValText.getText().toString()));
//        lightObject.put("intensity", Double.parseDouble(intensityValText.getText().toString()));
//
//        lightArray.put(lightObject);
//
//        mainObj.put("lights", lightArray);
//        mainObj.put("propagate", true);
//
//        try {
//            StringEntity jsonEntity = new StringEntity(mainObj.toString());
//            jsonEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//
//            // pass in values from server edittext
//            RestClient.post(getApplicationContext(), baseUrl, "rpi", jsonEntity, "application/json", new JsonHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    Toast.makeText(getApplicationContext(), "JSONObject", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                    Toast.makeText(getApplicationContext(), "JSONArray", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_SHORT).show();
//
//        } catch (UnsupportedEncodingException e) {
//            Log.d("EXCEPTION:", "ENCODING");
//        }
//    }

}
