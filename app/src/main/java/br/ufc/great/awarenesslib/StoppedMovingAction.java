package br.ufc.great.awarenesslib;

import android.content.Context;

import com.google.android.gms.awareness.fence.FenceState;

import awarenesshelper.FenceAction;

public class StoppedMovingAction implements FenceAction {
    @Override
    public void doOperation(Context context, FenceState state) {
        if(state.getCurrentState() == FenceState.TRUE){

        }
    }
}
