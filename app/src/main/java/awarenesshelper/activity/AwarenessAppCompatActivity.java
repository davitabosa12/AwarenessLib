package awarenesshelper.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.SnapshotClient;
import com.google.android.gms.awareness.state.BeaconState;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;

import java.util.Collection;
import java.util.List;

import awarenesshelper.Configurator;
import awarenesshelper.SnapshotType;
import awarenesshelper.util.ContextBundle;
import awarenesshelper.util.SnapshotObserverModel;

public class AwarenessAppCompatActivity extends AppCompatActivity {
    private static final String TAG = "AwarenessActivity";


    private SnapshotClient client;
    private SnapshotObserverModel observationZone;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configurator.init(this);
        client = Awareness.getSnapshotClient(this);
        observationZone = new SnapshotObserverModel();
    }

    /**
     * Initialize snapshots that you want to use
     * @param snapshots
     */
    public final void setSnapshots(SnapshotType... snapshots) {
        for (int i = 0; i < snapshots.length; i++) {
            observeSnapshot(snapshots[i]);
        }
    }

    private final void observeSnapshot(SnapshotType snapshot) {
        switch (snapshot) {
            case PLACES:
                observationZone.setObservingPlaces(true);
                break;
            case WEATHER:
                observationZone.setObservingWeather(true);
                break;
            case LOCATION:
                observationZone.setObservingLocation(true);
                break;
            case BEACON_STATE:
                observationZone.setObservingBeaconState(true);
                break;
            case TIME_INTERVAL:
                observationZone.setObservingTimeInterval(true);
                break;
            case HEADPHONE_STATE:
                observationZone.setObservingHeadphoneState(true);
                break;
            case DETECTED_ACTIVITY:
                observationZone.setObservingDetectingActivity(true);
                break;
        }
    }

    public final List<PlaceLikelihood> getPlaces() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            SecurityException se = new SecurityException("Missing permission to ACCESS_FINE_LOCATION");
            Log.e(TAG, "getPlaces: Permissions to ACCESS_FINE_LOCATION are missing. " +
                    "Consider calling AwarenessActivity#requestPermissions, " +
                    "and then get the result by overriding onPermissionsResult.", se);
            return null;
        }
        return client.getPlaces().getResult().getPlaceLikelihoods();
    }

    public final Place getMostProbablePlace(){
        List<PlaceLikelihood> places = getPlaces();
        //try to get the most probable...
        PlaceLikelihood mostProbablePlace = null;
        float maxLikelihood = Float.MIN_VALUE;
        for(PlaceLikelihood likelihood : places){
            float currentLikelihood = likelihood.getLikelihood();
            if(currentLikelihood > maxLikelihood){
                maxLikelihood = currentLikelihood;
                mostProbablePlace = likelihood;
            }
        }

        return mostProbablePlace.getPlace();
    }

    public final Weather getWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            SecurityException se = new SecurityException("Missing permission to ACCESS_FINE_LOCATION");
            Log.e(TAG, "getPlaces: Permissions to ACCESS_FINE_LOCATION are missing. " +
                    "Consider calling AwarenessActivity#requestPermissions, " +
                    "and then get the result by overriding onPermissionsResult.", se);
            return null;
        }
        return client.getWeather().getResult().getWeather();
    }

    public final Location getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            SecurityException se = new SecurityException("Missing permission to ACCESS_FINE_LOCATION");
            Log.e(TAG, "getPlaces: Permissions to ACCESS_FINE_LOCATION are missing. " +
                    "Consider calling AwarenessActivity#requestPermissions, " +
                    "and then get the result by overriding onPermissionsResult.", se);
            return null;
        }
        return client.getLocation().getResult().getLocation();
    }

    public final BeaconState getBeaconState(Collection<BeaconState.TypeFilter> filters) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            SecurityException se = new SecurityException("Missing permission to ACCESS_FINE_LOCATION");
            Log.e(TAG, "getPlaces: Permissions to ACCESS_FINE_LOCATION are missing. " +
                    "Consider calling AwarenessActivity#requestPermissions, " +
                    "and then get the result by overriding onPermissionsResult.", se);
            return null;
        }
        return client.getBeaconState(filters).getResult().getBeaconState();
    }

    public final TimeIntervals getTimeInterval(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            SecurityException se = new SecurityException("Missing permission to ACCESS_FINE_LOCATION");
            Log.e(TAG, "getPlaces: Permissions to ACCESS_FINE_LOCATION are missing. " +
                    "Consider calling AwarenessActivity#requestPermissions, " +
                    "and then get the result by overriding onPermissionsResult.", se);
            return null;
        }
        return client.getTimeIntervals().getResult().getTimeIntervals();
    }

    public final HeadphoneState getHeadphoneState(){
        return client.getHeadphoneState().getResult().getHeadphoneState();
    }

    public final DetectedActivity getMostProbableActivity(){
        return client.getDetectedActivity().getResult().getActivityRecognitionResult().getMostProbableActivity();
    }

    public final List<DetectedActivity> getProbableActivities(){
        return client.getDetectedActivity().getResult().getActivityRecognitionResult().getProbableActivities();
    }

    /**
     * @return a ContextBundle with the information of current observing contexts
     */
    public ContextBundle getCurrentContext(){
        ContextBundle.Builder builder = new ContextBundle.Builder();
        for(SnapshotType snapshot : observationZone.getCurrentObserving()){
            switch (snapshot) {
                case PLACES:
                    builder.setPlace(getMostProbablePlace());
                    break;
                case WEATHER:
                    builder.setWeather(getWeather());
                    break;
                case LOCATION:
                    builder.setLocation(getLocation());
                    break;
                case BEACON_STATE:
                    break;
                case TIME_INTERVAL:
                    builder.setTimeInterval(getTimeInterval());
                    break;
                case HEADPHONE_STATE:
                    builder.setHeadphoneState(getHeadphoneState());
                    break;
                case DETECTED_ACTIVITY:
                    builder.setDetectedActivity(getMostProbableActivity());
                    builder.setProbableActivities(getProbableActivities());
                    break;
            }
        }

        return builder.build();
    }
}
