package muntaserahmed.wifisentry;


public class CustomScanResult {

    public String SSID;
    public int level;

    public CustomScanResult() {
        this.SSID = null;
        this.level = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomScanResult)) return false;

        CustomScanResult that = (CustomScanResult) o;

        if (SSID != null ? !SSID.equals(that.SSID) : that.SSID != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return SSID != null ? SSID.hashCode() : 0;
    }

    public CustomScanResult(String SSID, int level) {
        this.SSID = SSID;
        this.level = level;
    }



    public String toString() {
        return "SSID: " + this.SSID + " Strength: " + this.level;
    }

}
