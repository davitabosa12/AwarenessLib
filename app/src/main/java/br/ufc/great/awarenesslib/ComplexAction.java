package br.ufc.great.awarenesslib;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.awareness.fence.FenceState;

import awarenesshelper.FenceAction;

public class ComplexAction implements FenceAction {

    public ComplexAction(){

    }
    @Override
    public void doOperation(Context context, FenceState state) {

        switch(state.getCurrentState()){
            case FenceState.TRUE:
                Toast.makeText(context,"You are moving.", Toast.LENGTH_LONG).show();
                break;
            case FenceState.FALSE:
                Toast.makeText(context,"You aren't moving", Toast.LENGTH_LONG).show();
                break;
            case FenceState.UNKNOWN:
                Toast.makeText(context,"FenceState is unknown", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
