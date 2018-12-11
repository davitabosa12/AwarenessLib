package awarenesshelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.SnapshotClient;

public class AwarenessActivity extends Activity {

    private SnapshotClient client;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configurator.init(this);
        client = Awareness.getSnapshotClient(this);
        /*client.getBeaconState();
        client.getDetectedActivity();
        client.getHeadphoneState();
        client.getLocation();
        client.getPlaces();
        client.getTimeIntervals();
        client.getWeather();*/
    }

    /**
     * Initialize snapshots that you want to use
     * @param snapshots
     */
    public final void setSnapshots(int ...snapshots){

    }


}
