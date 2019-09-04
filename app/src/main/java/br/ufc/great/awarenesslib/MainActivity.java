package br.ufc.great.awarenesslib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.awareness.fence.FenceQueryResponse;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Set;

import smd.ufc.br.easycontext.EasyContext;
import smd.ufc.br.easycontext.fence.FenceManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_registered_fences).setOnClickListener(this);
        EasyContext ec = EasyContext.init(this);
        FenceManager manager = ec.getFenceManager();
        Log.d("MainActivity", "onCreate: " + manager.getRegisteredFences().toString());
        manager.getRegisteredFences().addOnSuccessListener(new OnSuccessListener<FenceQueryResponse>() {
            @Override
            public void onSuccess(FenceQueryResponse fenceQueryResponse) {
                Set<String> keys = fenceQueryResponse.getFenceStateMap().getFenceKeys();
                Log.d("MainActivity", "onSuccess: " + keys);
                boolean allRegistrado = false;
                for(String key : keys){
                    if(key.equals("all")){
                        allRegistrado = true;
                        break;
                    }
                }
                if(allRegistrado){
                    Log.d("MainActivity", "onCreate: All Registrado.");
                } else {
                    Log.e("MainActivity", "onCreate: All NAO Registrado.");
                }
            }
        });

        //Log.d("MainActivity", ec.getFenceList().toString());
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
