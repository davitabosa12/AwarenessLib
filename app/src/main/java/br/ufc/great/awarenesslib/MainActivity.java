package br.ufc.great.awarenesslib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import awarenesshelper.Configurator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Configurator.init(this);

    }
}
