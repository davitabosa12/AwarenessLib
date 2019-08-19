package smd.ufc.br.easycontext;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class SnapshotWatcherService extends Service {
    Snapshot snapshot;
    SnapshotSerializer serializer;
    public SnapshotWatcherService() {
        snapshot = Snapshot.getInstance(getApplicationContext());
        serializer = SnapshotSerializer.getSerializer(SnapshotSerializer.JSON);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BroadcastReceiver getContext = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int contextsToWatch = intent.getIntExtra("contextsToWatch",0);
                if((contextsToWatch & SnapshotWatcher.DETECTED_ACTIVITY) == SnapshotWatcher.DETECTED_ACTIVITY){
                    snapshot.getDetectedActivity();
                }
            }
        };
        registerReceiver(getContext,new IntentFilter("CONTEXT_AQUISITION"));

        return 0;
    }
}
