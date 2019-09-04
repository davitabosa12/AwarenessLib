package br.ufc.great.awarenesslib;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.awareness.fence.FenceState;

import java.util.TooManyListenersException;

import smd.ufc.br.easycontext.fence.FenceAction;

/**
 * Created by davitabosa on 28/05/2018.
 */

public class MyCustomAction extends FenceAction {
    TextView text;
    public MyCustomAction(TextView text){
        this.text = text;
    }
    @Override
    public void doOperation(Context context, FenceState state, Bundle data) {
        switch (state.getCurrentState()){
            case FenceState.TRUE:
                text.setText("Connected");
                text.setTextColor(context.getResources().getColor(R.color.awareGreen));
                break;
            case FenceState.FALSE:
                text.setText("Disconnected");
                text.setTextColor(context.getResources().getColor(R.color.awareRed));
                break;
            case FenceState.UNKNOWN:
                text.setText("Unknown");
                text.setTextColor(context.getResources().getColor(R.color.awareGray));
                break;
        }
    }
}
