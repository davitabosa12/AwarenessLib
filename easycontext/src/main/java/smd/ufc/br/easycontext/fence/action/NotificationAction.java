package smd.ufc.br.easycontext.fence.action;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.android.gms.awareness.fence.FenceState;

import smd.ufc.br.easycontext.R;
import smd.ufc.br.easycontext.fence.FenceAction;

public class NotificationAction extends FenceAction {
    String title, channel, text;
    int importance;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        if(importance < 0 || importance > 5){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.importance = NotificationManager.IMPORTANCE_DEFAULT;
            }
        }
    }

    public NotificationAction(String title, String channel, String text, int importance) {
        this.title = title;
        this.channel = channel;
        this.text = text;
        this.importance = importance;
        if(importance < 0 || importance > 5){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.importance = NotificationManager.IMPORTANCE_DEFAULT;
            }
        }
    }

    @Override
    public void doOperation(Context context, FenceState state, Bundle data) {
        if(state.getCurrentState() == FenceState.TRUE){
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentText(text)
                    .build();
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                nm.createNotificationChannel(new NotificationChannel(channel, channel, importance));
            }
            NotificationManagerCompat.from(context).notify(channel, (int) SystemClock.uptimeMillis(),notification);
        }
    }
}
