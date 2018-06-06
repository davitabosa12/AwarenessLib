package br.ufc.great.awarenesslib;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.awareness.fence.FenceState;

import java.util.TooManyListenersException;

import awarenesshelper.FenceAction;

/**
 * Created by davitabosa on 28/05/2018.
 */

public class MyCustomAction implements FenceAction {
    public MyCustomAction(){}
    @Override
    public void doOperation(Context context, FenceState state) {
        String msg = "Headphone está ";
        switch (state.getCurrentState()){
            case FenceState.TRUE:
                msg += "plugado.";
                break;
            case FenceState.FALSE:
                msg += "desplugado.";
                break;
            case FenceState.UNKNOWN:
                msg = "não foi possivel obter estado do headphone.";
                break;
        }
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
