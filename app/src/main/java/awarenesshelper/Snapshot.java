package awarenesshelper;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;


import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.SnapshotClient;

import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.PlaceLikelihood;

import java.util.List;

public class Snapshot {

    private SnapshotClient client;
    private static Snapshot instance;

    private Snapshot(Context context){
        this.client = Awareness.getSnapshotClient(context);

    }

    private Snapshot(Activity activity){
        this.client = Awareness.getSnapshotClient(activity);
    }

    static Snapshot getInstance(Context context){
        if(instance == null){
            instance = new Snapshot(context);
        }
        return instance;
    }



	public DetectedActivity getDetectedActivity() {

        DetectedActivity detectedActivity;
        try {
            detectedActivity = client.getDetectedActivity().getResult().getActivityRecognitionResult().getMostProbableActivity();
            return detectedActivity;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
	}

	public HeadphoneState getHeadphoneState() {

        HeadphoneState state;
        try{
            state = client.getHeadphoneState().getResult().getHeadphoneState();
            return state;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
	}

	@SuppressLint("MissingPermission")
    public Location getLocation() {
		Location location;
		try{
		    location = client.getLocation().getResult().getLocation();
		    return location;
        }catch (Exception e){
		    e.printStackTrace();
        }

        return null;
	}

	@SuppressLint("MissingPermission")
    public List<PlaceLikelihood> getPlaces() {
        List<PlaceLikelihood> places;
        try{
            places = client.getPlaces().getResult().getPlaceLikelihoods();
            return places;
        } catch (Exception e){
            e.printStackTrace();
        }
		return null;
	}

	@SuppressLint("MissingPermission")
    public TimeIntervals getTimeIntervals() {
		TimeIntervals intervals;
		try{
		    intervals = client.getTimeIntervals().getResult().getTimeIntervals();
		    return intervals;
        } catch (Exception e){
		    e.printStackTrace();
        }
        return null;
	}

	@SuppressLint("MissingPermission")
    public Weather getWeather() {
		Weather weather;
		try{
		    weather = client.getWeather().getResult().getWeather();
		    return weather;
        } catch (Exception e){
		    e.printStackTrace();
        }
        return null;
	}

}
