package muntaserahmed.wifisentry;

import java.util.Comparator;
import android.net.wifi.ScanResult;

public class SortSSID implements Comparator<Object> {

    @Override
    public int compare(Object obj1, Object obj2) {
        ScanResult sr1 = (ScanResult) obj1;
        ScanResult sr2 = (ScanResult) obj2;
        return sr1.SSID.compareTo(sr2.SSID);
    }

}
