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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class DashboardActivity extends Activity {

    WifiManager wifiManager;
    ArrayList<ScanResult> scanResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        scanResults = scan();
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

    public ArrayList<ScanResult> scan() throws IllegalStateException {
        boolean scanSuccess = wifiManager.startScan();
        if (scanSuccess) {
            ArrayList<ScanResult> scanResults = (ArrayList) wifiManager.getScanResults();
            SortScanResults sort = new SortScanResults();
            Collections.sort(scanResults, sort);

//            for (ScanResult sr : scanResults) {
//                Toast.makeText(getApplicationContext(), sr.SSID, Toast.LENGTH_SHORT).show();
//            }

            return scanResults;
        }
        else {
            Log.d("EXCEPTION: ", "SCAN FAILED");
            throw new IllegalStateException();
        }
    }


}
