package muntaserahmed.wifisentry;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class DashboardActivity extends Activity {

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

        scanListView = (ListView) findViewById(R.id.scanListView);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        refresh();
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
        if (id == R.id.action_settings) {
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
            CustomScanResult csr = new CustomScanResult(sr.SSID, sr.level);
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
                android.R.layout.simple_list_item_1,
                scanResults
        );
        scanListView.setAdapter(arrayAdapter);
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
                    String lat = response.getString("lat");
                    String lon = response.getString("lon");
                    Toast.makeText(getApplicationContext(), lat + ", " + lon, Toast.LENGTH_SHORT).show();
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

}
