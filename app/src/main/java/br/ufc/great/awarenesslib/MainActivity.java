package br.ufc.great.awarenesslib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import smd.ufc.br.easycontext.EasyContext;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_registered_fences).setOnClickListener(this);
        EasyContext ec = EasyContext.init(this);
        Log.d("MainActivity", ec.getFenceList().toString());
        ec.watchAll();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_registered_fences:{
                Intent i = new Intent(this, RegisteredFencesActivity.class);
                startActivity(i);
            }

        }
    }
}
