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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        RequestParams requestParams = new RequestParams();
        requestParams.put("lightid", "1");

        // pass in values from server edittext
        RestClient.post("10.0.1.56", "test", requestParams, new JsonHttpResponseHandler() {
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
    }

}
