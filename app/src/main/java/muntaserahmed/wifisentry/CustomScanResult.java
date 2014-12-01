package muntaserahmed.wifisentry;


import android.os.Parcel;
import android.os.Parcelable;

public class CustomScanResult implements Parcelable {

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

    public CustomScanResult(Parcel in) {
        SSID = in.readString();
        level = in.readInt();
        normalizedLevel = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(SSID);
        out.writeInt(level);
        out.writeInt(normalizedLevel);
    }

    public static final Parcelable.Creator<CustomScanResult> CREATOR
            = new Parcelable.Creator<CustomScanResult>() {
        public CustomScanResult createFromParcel(Parcel in) {
            return new CustomScanResult(in);
        }

        public CustomScanResult[] newArray(int size) {
            return new CustomScanResult[size];
        }
    };

}
