package muntaserahmed.wifisentry;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;


public class DashboardActivity extends Activity {

    SharedPreferences preferences;

    ActionBar actionBar;
    ListView scanListView;

    WifiManager wifiManager;
    ConnectivityManager connManager;
    NetworkInfo netInfo;

    ArrayList<CustomScanResult> scanResults;
    ArrayAdapter<CustomScanResult> arrayAdapter;

    SortLevel sortByLevel = new SortLevel();

    final String ipApiUrl = "http://ip-api.com/json/";
    final String icanhazipUrl = "http://icanhazip.com/";

    String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        preferences = getSharedPreferences(ServerActivity.SERVER_PREFERENCES, MODE_PRIVATE);
        final String serverAddress = preferences.getString("serverIp", null);

        scanListView = (ListView) findViewById(R.id.scanListView);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        scanListView.setBackgroundColor(getResources().getColor(R.color.greensea));
        refresh();

        scanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                CustomScanResult item = (CustomScanResult) adapterView.getItemAtPosition(i);
                int level = item.level;

                if (serverAddress != null) {

                    JSONObject requestObj = constructJSONObject(level);

                    try {
                        StringEntity jsonEntity = new StringEntity(requestObj.toString());
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

                } else {
                    Toast.makeText(getApplicationContext(), "Add a server first", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_cardboard) {
            int strongestLevel = convertRange(calcStopLight(getStrongest()));
            Intent intent = new Intent(this, VRActivity.class);
            intent.putExtra("strongestLevel", strongestLevel);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_server) {
            Intent intent = new Intent(this, ServerActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_refresh) {
            refresh();
        }
        else if (id == R.id.action_place) {
            getLocation();
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<CustomScanResult> scan() throws IllegalStateException {
        boolean scanSuccess = wifiManager.startScan();
        if (scanSuccess) {
            ArrayList<ScanResult> scanResults = (ArrayList) wifiManager.getScanResults();
            ArrayList<CustomScanResult> sanitizedResults = sanitizeResults(scanResults);
            return sanitizedResults;
        }
        else {
            Log.d("EXCEPTION: ", "SCAN FAILED");
            throw new IllegalStateException();
        }
    }

    public ArrayList<CustomScanResult> sanitizeResults(ArrayList<ScanResult> scanResults) {

        ArrayList<CustomScanResult> customScanResults = new ArrayList<CustomScanResult>();

        for (ScanResult sr : scanResults) {
            int normalizedLevel = convertRange(calcStopLight(sr.level));
            CustomScanResult csr = new CustomScanResult(sr.SSID, sr.level, normalizedLevel);
            if (!csr.SSID.equals("")) {
                customScanResults.add(csr);
            }
        }

        HashSet noDupes = new HashSet();
        noDupes.addAll(customScanResults);
        customScanResults.clear();

        customScanResults.addAll(noDupes);
        Collections.sort(customScanResults, sortByLevel);

        return customScanResults;
    }

    public void refresh() {
        scanResults = scan();

        arrayAdapter = new ArrayAdapter<CustomScanResult>(
                this,
                R.layout.row,
                scanResults
        );
        scanListView.setAdapter(arrayAdapter);
    }

    public int getStrongest() {
        return scanResults.get(0).level;
    }

    public void getLocation() {
        connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        netInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

//        if (!netInfo.isConnected()) {
//            Toast.makeText(getApplicationContext(), "You are not connected!", Toast.LENGTH_SHORT).show();
//            return;
//        }

//        Toast.makeText(getApplicationContext(), "Connected!", Toast.LENGTH_SHORT).show();

        setIp();
        String urlWithParam = ipApiUrl + getIp();

        RestClient.get(urlWithParam, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String status = response.getString("status");
                    double lat = Double.parseDouble(response.getString("lat"));
                    double lon = Double.parseDouble(response.getString("lon"));
                    String mTitle = "WiFi Location";
                    String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lon + " (" + mTitle + ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                    startActivity(intent);
                } catch (JSONException e) {
                    Log.d("JSONException", "Failed to parse");
                }
            }

        });

    }

    public void setIp() {
        RestClient.get(icanhazipUrl, null, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ipAddress = responseString;
            }
        });
    }

    public String getIp() {
        return ipAddress;
    }

    public int calcStopLight(int level) {
        double inverseLevel = (double) (level * -1); // Make positive
        int percentLevel = (int) (inverseLevel * 0.32) + 1; // 1% of light is 0.32
        return 32 - percentLevel;
    }

    public int convertRange(int lightId) {
        int newVal = (((lightId - 0) * (100 - 0)) / (32 - 0)) + 0;
        return newVal;
    }

    public JSONObject constructJSONObject(int level) {
        int stopLight = calcStopLight(level);

        JSONObject mainObj = new JSONObject();
        JSONArray lightArray = new JSONArray();
        JSONObject lightObject = new JSONObject();
        JSONObject offLightObject = new JSONObject();

        try {
            lightObject.put("lightId", 1); // Make beginning light up
            lightObject.put("intensity", 1);
            lightObject.put("red", 46);
            lightObject.put("green", 204);
            lightObject.put("blue", 113);

            offLightObject.put("lightId", stopLight); // Turn off the rest where it should stop
            offLightObject.put("intensity", 0);
            offLightObject.put("red", 46);
            offLightObject.put("green", 204);
            offLightObject.put("blue", 113);

            lightArray.put(lightObject);
            lightArray.put(offLightObject);
            mainObj.put("lights", lightArray);
            mainObj.put("propagate", true);
        } catch (JSONException e) {
            Log.d("EXCEPTION: ", "JSONException constructJSONObject");
        }

        return mainObj;
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

}
