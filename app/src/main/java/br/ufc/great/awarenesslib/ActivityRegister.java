package br.ufc.great.awarenesslib;

import android.content.Context;
import android.content.SharedPreferences;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import java.sql.Time;

public class ActivityRegister {
    public final String TAG = "ActivityRegister";
    public final String PREF_FILE = "activity_register";
    Context context;
    SharedPreferences preferences;
    public ActivityRegister(Context context){
        this.context = context;
        this.preferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        JodaTimeAndroid.init(context);
    }

    public void registerStart(){
        DateTime now = DateTime.now();


    }

    public void registerStop(){

    }
}
