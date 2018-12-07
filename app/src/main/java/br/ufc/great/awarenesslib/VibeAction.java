package br.ufc.great.awarenesslib;

import android.content.Context;
import android.os.Vibrator;

import com.google.android.gms.awareness.fence.FenceState;

import awarenesshelper.FenceAction;

/**
 * Created by davitabosa on 20/08/2018.
 */

public class VibeAction implements FenceAction {
    @Override
    public void doOperation(Context context, FenceState state) {
        if(state.getCurrentState() == FenceState.TRUE){
            Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            if(vibrator != null)
                vibrator.vibrate(500);
        }
    }
}
