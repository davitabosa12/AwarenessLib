package awarenesshelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by davitabosa on 08/08/2018.
 */

public class SnapshotWatcher {
    public static final int HEADPHONE = 1;
    public static final int DETECTED_ACTIVITY = 1 << 1;
    public static final int TIME_INTERVALS = 1 << 2;
    public static final int LOCATION = 1 << 3;
    public static final int PLACES = 1 << 4;
    public static final int WEATHER = 1 << 5;
    public static final int ALL = 111111;

    private int contextsToWatch;
    private AlarmManager manager;
    private Context context;
    private long millisToWait;

    /**
     * Default constructor of SnapshotWatcher. Watches every context by default every minute.
     */
    public SnapshotWatcher(Context ctx){
        this.contextsToWatch = ALL;
        this.context = ctx;
        //TODO: Replace AlarmManager with Jetpack Tasks
        this.manager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        this.millisToWait = 60*1000;
    }

    /**
     * Start watching the values of sensors/contexts. Logs to a file
     */
    public void watch(){
        Intent theService = new Intent(context,SnapshotWatcherService.class);
        context.stopService(theService);

        Intent i = new Intent("CONTEXT_AQUISITION");
        i.putExtra("contextsToWatch",contextsToWatch);
        PendingIntent pi = PendingIntent.getBroadcast(context,0, i, PendingIntent.FLAG_CANCEL_CURRENT);

        manager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),millisToWait,pi);
        context.startService(theService);
    }

}
