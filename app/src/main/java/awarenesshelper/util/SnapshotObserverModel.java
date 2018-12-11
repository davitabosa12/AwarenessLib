package awarenesshelper.util;

import java.util.ArrayList;
import java.util.List;

import awarenesshelper.SnapshotType;

/**
 * Helps AwarenessActivities to manage snapshots that the developer want to observe
 */
public class SnapshotObserverModel {
    private boolean observingDetectingActivity = false;
    private boolean observingBeaconState = false;
    private boolean observingWeather = false;
    private boolean observingHeadphoneState = false;
    private boolean observingLocation = false;
    private boolean observingPlaces = false;
    private boolean observingTimeInterval = false;

    public SnapshotObserverModel() {

    }

    public boolean isObservingDetectingActivity() {
        return observingDetectingActivity;
    }

    public void setObservingDetectingActivity(boolean observingDetectingActivity) {
        this.observingDetectingActivity = observingDetectingActivity;
    }

    public boolean isObservingBeaconState() {
        return observingBeaconState;
    }

    public void setObservingBeaconState(boolean observingBeaconState) {
        this.observingBeaconState = observingBeaconState;
    }

    public boolean isObservingWeather() {
        return observingWeather;
    }

    public void setObservingWeather(boolean observingWeather) {
        this.observingWeather = observingWeather;
    }

    public boolean isObservingHeadphoneState() {
        return observingHeadphoneState;
    }

    public void setObservingHeadphoneState(boolean observingHeadphoneState) {
        this.observingHeadphoneState = observingHeadphoneState;
    }

    public boolean isObservingLocation() {
        return observingLocation;
    }

    public void setObservingLocation(boolean observingLocation) {
        this.observingLocation = observingLocation;
    }

    public boolean isObservingPlaces() {
        return observingPlaces;
    }

    public void setObservingPlaces(boolean observingPlaces) {
        this.observingPlaces = observingPlaces;
    }

    public boolean isObservingTimeInterval() {
        return observingTimeInterval;
    }

    public void setObservingTimeInterval(boolean observingTimeInterval) {
        this.observingTimeInterval = observingTimeInterval;
    }

    public List<SnapshotType> getCurrentObserving(){
        List<SnapshotType> resp = new ArrayList<>();
        if(observingBeaconState) resp.add(SnapshotType.BEACON_STATE);
        if(observingDetectingActivity) resp.add(SnapshotType.DETECTED_ACTIVITY);
        if(observingHeadphoneState) resp.add(SnapshotType.HEADPHONE_STATE);
        if(observingLocation) resp.add(SnapshotType.LOCATION);
        if(observingPlaces) resp.add(SnapshotType.PLACES);
        if(observingTimeInterval) resp.add(SnapshotType.TIME_INTERVAL);
        if(observingWeather) resp.add(SnapshotType.WEATHER);
        return resp;
    }

    public void unwatchAll() {
        observingWeather = false;
        observingTimeInterval = false;
        observingBeaconState = false;
        observingDetectingActivity = false;
        observingWeather = false;
        observingPlaces = false;
        observingHeadphoneState = false;
    }
}
