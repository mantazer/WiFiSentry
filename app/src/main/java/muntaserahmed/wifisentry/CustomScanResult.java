package muntaserahmed.wifisentry;


public class CustomScanResult {

    public String SSID;
    public int level;
    public int normalizedLevel;

    public CustomScanResult() {
        this.SSID = null;
        this.level = 0;
        this.normalizedLevel = 0;
    }

    public CustomScanResult(String SSID, int level, int normalizedLevel) {
        this.SSID = SSID;
        this.level = level;
        this.normalizedLevel = normalizedLevel;
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



    public String toString() {
        return this.SSID + ": " + this.normalizedLevel;
    }

}
