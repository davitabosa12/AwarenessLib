package br.ufc.great.awarenesslib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.location.DetectedActivity;

import awarenesshelper.Configurator;

public class ReconActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recon);
        Configurator.init(ReconActivity.this);

    }
}
