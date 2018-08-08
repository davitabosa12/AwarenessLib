package br.ufc.great.awarenesslib;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

import awarenesshelper.Configurator;
import awarenesshelper.FenceAction;

public class MainActivity extends AppCompatActivity {

    Button skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.intent_data);
        Map<String, FenceAction> actions = new HashMap<>();
        actions.put("action1", new MyCustomAction());
        Configurator.init(MainActivity.this, actions);

        skip = findViewById(R.id.btn_recon);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReconActivity.class));
            }
        });

    }
}
