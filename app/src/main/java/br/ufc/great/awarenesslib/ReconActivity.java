package br.ufc.great.awarenesslib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;

import java.util.HashMap;
import java.util.Map;

import awarenesshelper.Configurator;
import awarenesshelper.FenceAction;

public class ReconActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recon);
        TextView textView = findViewById(R.id.txv_result);
        Map<String, FenceAction> actions = new HashMap<>();
        actions.put("complex_action", new ComplexAction(textView));
        Configurator.init(ReconActivity.this, actions);

    }
}
