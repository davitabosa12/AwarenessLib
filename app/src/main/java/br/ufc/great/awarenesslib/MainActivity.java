package br.ufc.great.awarenesslib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.location.DetectedActivity;

import awarenesshelper.AndFence;
import awarenesshelper.DAMethod;
import awarenesshelper.DetectedActivityFence;
import awarenesshelper.DetectedActivityParameter;
import awarenesshelper.FenceManager;
import awarenesshelper.HeadphoneFence;
import awarenesshelper.HeadphoneMethod;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HeadphoneFence pluggedIn = new HeadphoneFence("plugged",HeadphoneMethod.HEADPHONE_PLUGGING_IN,new VibeAction(),null);
        DetectedActivityFence walking = new DetectedActivityFence("walking", DAMethod.DA_STARTING, new VibeAction(),
                new DetectedActivityParameter.Builder().addActivityType(DetectedActivity.WALKING).build());

        AndFence and = new AndFence("composition",new LightAction(this,"hello world"),pluggedIn,walking);

        FenceManager.getInstance(this).registerFence(pluggedIn);
        FenceManager.getInstance(this).registerFence(walking);
        FenceManager.getInstance(this).registerFence(and);


    }

    @Override
    protected void onStop() {
        super.onStop();
        FenceManager manager = FenceManager.getInstance(this);
        manager.unregisterAll();
    }
}
