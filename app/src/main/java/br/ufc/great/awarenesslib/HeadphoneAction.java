package br.ufc.great.awarenesslib;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.awareness.fence.FenceState;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import smd.ufc.br.easycontext.fence.FenceAction;

/**
 * Created by davitabosa on 20/08/2018.
 */

public class HeadphoneAction extends FenceAction {
    Context context;

    private Notification notification;
    private static final String TAG = "HeadphoneAction";
    @Override
    public void doOperation(Context context, FenceState state, Bundle data) {
        this.context = context;
        notification = new NotificationCompat.Builder(context, "awarenesslib")
                .setContentTitle("Plugado")
                .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                .setContentText("Headphone Plugado")
                .build();
        switch (state.getCurrentState()){
            case FenceState.TRUE:
                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    nm.createNotificationChannel(new NotificationChannel("awarenesslib", "Regular", NotificationManager.IMPORTANCE_HIGH));
                }
                NotificationManagerCompat.from(context).notify(777,notification);
                saveHistory(true);
                break;
            case FenceState.FALSE:
                saveHistory(false);
                break;
        }
    }

    private void saveHistory(Boolean connected){
        ActivityRegisterDbHelper helper = new ActivityRegisterDbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        JodaTimeAndroid.init(context);
        DateTime now = DateTime.now();
        ContentValues values = new ContentValues();
        values.put(ActivityRegisterContract.ActivityRegisterEntry.COLUMN_NAME_DATE, now.toString());
        if(connected)
            values.put(ActivityRegisterContract.ActivityRegisterEntry.COLUMN_NAME_ACTIVITY, ActivityRegister.HEADPHONE_CONNECTED);
        else
            values.put(ActivityRegisterContract.ActivityRegisterEntry.COLUMN_NAME_ACTIVITY, ActivityRegister.HEADPHONE_DISCONNECTED);

        long result = db.insert(ActivityRegisterContract.ActivityRegisterEntry.TABLE_NAME, null, values);

        if(result < 0){
            Log.e(TAG, "doOperation: failed inserting row");
        }
        Intent update = new Intent("UPDATE_AWARENESSLIB");
        context.sendBroadcast(update);
    }
}
