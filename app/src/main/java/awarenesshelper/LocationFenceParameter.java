package awarenesshelper;

/**
 * Created by davitabosa on 20/06/2018.
 */

class LocationFenceParameter implements FenceParameter {

    private double latitude;
    private double longitude;
    private double radius;
    private long dwellTimeMillis;
    public LocationFenceParameter(){

    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public long getDwellTimeMillis() {
        return dwellTimeMillis;
    }

    public void setDwellTimeMillis(long dwellTimeMillis) {
        this.dwellTimeMillis = dwellTimeMillis;
    }
}
