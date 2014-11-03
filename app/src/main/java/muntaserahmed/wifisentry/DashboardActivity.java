package muntaserahmed.wifisentry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class DashboardActivity extends Activity {

    ListView scanListView;

    WifiManager wifiManager;
    ArrayList<CustomScanResult> scanResults;

    SortSSID sortBySSID = new SortSSID();
    SortLevel sortByLevel = new SortLevel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        scanListView = (ListView) findViewById(R.id.scanListView);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        scanResults = scan();

        ArrayAdapter<CustomScanResult> arrayAdapter = new ArrayAdapter<CustomScanResult>(
                this,
                android.R.layout.simple_list_item_1,
                scanResults
        );

        scanListView.setAdapter(arrayAdapter);

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

        Collections.sort(scanResults, sortBySSID);

        if (scanResults.get(0).SSID == "" || scanResults.get(0).SSID == null) {
            scanResults.remove(0);
        }

        for (int i = 1; i < scanResults.size(); i++) {
            if ((scanResults.get(i).SSID.equals(scanResults.get(i - 1).SSID)) ||
                    (scanResults.get(0).SSID == "" || scanResults.get(0).SSID == null)) {
                scanResults.remove(i);
            }
        }

        for (ScanResult sr : scanResults) {
            CustomScanResult csr = new CustomScanResult(sr.SSID, sr.level);
            customScanResults.add(csr);
        }

        Collections.sort(customScanResults, sortByLevel);

        return customScanResults;
    }

}
