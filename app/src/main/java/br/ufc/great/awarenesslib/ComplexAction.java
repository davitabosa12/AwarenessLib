package br.ufc.great.awarenesslib;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.awareness.fence.FenceState;

import awarenesshelper.FenceAction;

public class ComplexAction implements FenceAction {

    TextView txvResult;
    public ComplexAction(TextView txv){
        this.txvResult = txv;
    }
    @Override
    public void doOperation(Context context, FenceState state) {

        switch(state.getCurrentState()){
            case FenceState.TRUE:
                txvResult.setText("You are moving.");
                //Toast.makeText(context,"You are moving.", Toast.LENGTH_LONG).show();
                break;
            case FenceState.FALSE:
                txvResult.setText("You aren't moving.");
                //Toast.makeText(context,"You aren't moving", Toast.LENGTH_LONG).show();
                break;
            case FenceState.UNKNOWN:
                txvResult.setText("FenceState is unknown");
                //Toast.makeText(context,"FenceState is unknown", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
