package awarenesshelper.util;

import android.location.Location;

import com.google.android.gms.awareness.state.BeaconState;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;

import java.io.Serializable;
import java.util.List;

/**
 * Class tha contains context bundled context information.
 * Non-observed context information will be null.
 */
public class ContextBundle implements Serializable {
    DetectedActivity detectedActivity;
    List<DetectedActivity> probableActivities;
    Location location;
    Weather weather;
    HeadphoneState headphoneState;
    TimeIntervals timeInterval;
    Place place;
    BeaconState beaconState;

    ContextBundle(DetectedActivity detectedActivity,
                         List<DetectedActivity> probableActivities,
                         Location location,
                         Weather weather,
                         HeadphoneState headphoneState,
                         TimeIntervals timeInterval,
                         Place place,
                         BeaconState beaconState) {
        this.detectedActivity = detectedActivity;
        this.probableActivities = probableActivities;
        this.location = location;
        this.weather = weather;
        this.headphoneState = headphoneState;
        this.timeInterval = timeInterval;
        this.place = place;
        this.beaconState = beaconState;
    }

    public DetectedActivity getDetectedActivity() {
        return detectedActivity;
    }

    public List<DetectedActivity> getProbableActivities() {
        return probableActivities;

    }

    public Location getLocation() {
        return location;
    }

    public Weather getWeather() {
        return weather;
    }

    public HeadphoneState getHeadphoneState() {
        return headphoneState;
    }

    public TimeIntervals getTimeInterval() {
        return timeInterval;
    }

    public Place getPlace() {
        return place;
    }

    public BeaconState getBeaconState() {
        return beaconState;
    }



    public static class Builder {

        DetectedActivity detectedActivity;
        List<DetectedActivity> probableActivities;
        Location location;
        Weather weather;
        HeadphoneState headphoneState;
        TimeIntervals timeInterval;
        List<PlaceLikelihood> places;
        Place place;
        BeaconState beaconState;
        public Builder(){

        }

        public Builder setDetectedActivity(DetectedActivity detectedActivity) {
            this.detectedActivity = detectedActivity;
            return this;
        }

        public Builder setProbableActivities(List<DetectedActivity> probableActivities) {
            this.probableActivities = probableActivities;
            return this;
        }

        public Builder setLocation(Location location) {
            this.location = location;
            return this;
        }

        public Builder setWeather(Weather weather) {
            this.weather = weather;
            return this;
        }

        public Builder setHeadphoneState(HeadphoneState headphoneState) {
            this.headphoneState = headphoneState;
            return this;
        }

        public Builder setTimeInterval(TimeIntervals timeInterval) {
            this.timeInterval = timeInterval;
            return this;
        }

        public Builder setPlace(Place place) {
            this.place = place;
            return this;
        }

        public Builder setBeaconState(BeaconState beaconState) {
            this.beaconState = beaconState;
            return this;
        }

        public ContextBundle build(){
            return new ContextBundle(detectedActivity,
                    probableActivities,
                    location,
                    weather,
                    headphoneState,
                    timeInterval,
                    place,
                    beaconState);
        }
    }
}
