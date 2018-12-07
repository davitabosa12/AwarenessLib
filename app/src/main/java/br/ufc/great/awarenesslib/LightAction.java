package br.ufc.great.awarenesslib;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.android.gms.awareness.fence.FenceState;

import awarenesshelper.FenceAction;

/**
 * Created by davitabosa on 20/08/2018.
 */

public class LightAction implements FenceAction {
    private final String message;
    private final Context context;
    private Notification notification;

    public LightAction(Context context, String message){
        this.message = message;
        this.context = context;
         notification = new NotificationCompat.Builder(context)
                .setContentTitle("Message")
                .setContentText(message)
                .build();
    }
    @Override
    public void doOperation(Context context, FenceState state) {
        switch (state.getCurrentState()){
            case FenceState.TRUE:
                NotificationManagerCompat.from(context).notify(777,notification);
                break;
            case FenceState.FALSE:
                break;
        }
    }
}
