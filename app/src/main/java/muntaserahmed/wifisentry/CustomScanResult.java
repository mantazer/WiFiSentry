package muntaserahmed.wifisentry;


public class CustomScanResult {

    public String SSID;
    public int level;

    public CustomScanResult() {
        this.SSID = null;
        this.level = 0;
    }

    public CustomScanResult(String SSID, int level) {
        this.SSID = SSID;
        this.level = level;
    }

    public String toString() {
        return "SSID: " + this.SSID + " Strength: " + this.level;
    }

}
