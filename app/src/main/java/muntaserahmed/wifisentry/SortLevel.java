package muntaserahmed.wifisentry;

import java.util.Comparator;
import android.net.wifi.ScanResult;

public class SortLevel implements Comparator<Object> {

    @Override
    public int compare(Object obj1, Object obj2) {
        CustomScanResult sr1 = (CustomScanResult) obj1;
        CustomScanResult sr2 = (CustomScanResult) obj2;

        if (sr1.level > sr2.level) {
            return -1;
        }
        return 1;
    }

}
