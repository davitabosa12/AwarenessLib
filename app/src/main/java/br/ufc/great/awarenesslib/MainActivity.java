package br.ufc.great.awarenesslib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import java.util.ArrayList;

import awarenesshelper.AndFence;
import awarenesshelper.Configurator;
import awarenesshelper.DAMethod;
import awarenesshelper.DetectedActivityFence;
import awarenesshelper.DetectedActivityParameter;
import awarenesshelper.FenceManager;
import awarenesshelper.HeadphoneFence;
import awarenesshelper.HeadphoneMethod;
import br.ufc.great.awarenesslib.ActivityRegisterContract.ActivityRegisterEntry;

public class MainActivity extends AppCompatActivity {

    ArrayList<Pair<String, DateTime>> data;
    LinearLayout ll;
    BroadcastReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Configurator.init(this);
        data = new ArrayList<>();
        JodaTimeAndroid.init(this);
        ll = findViewById(R.id.linearScroll);

        updateData();


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateData();
            }
        };
        registerReceiver(receiver, new IntentFilter("UPDATE_AWARENESSLIB"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        FenceManager manager = FenceManager.getInstance(this);
        manager.unregisterAll();

    }

    private void updateData(){
        data.clear();
        ActivityRegisterDbHelper helper = new ActivityRegisterDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {BaseColumns._ID,
                ActivityRegisterEntry.COLUMN_NAME_ACTIVITY,
                ActivityRegisterEntry.COLUMN_NAME_DATE};

        String selection = "1 = 1";
        String sortOrder = ActivityRegisterEntry.COLUMN_NAME_DATE + " DESC";
        Cursor cursor = db.query(ActivityRegisterEntry.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                sortOrder);

        while(cursor.moveToNext()){
            String activity = cursor.getString(cursor.getColumnIndexOrThrow(ActivityRegisterEntry.COLUMN_NAME_ACTIVITY));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(ActivityRegisterEntry.COLUMN_NAME_DATE));
            DateTime dateTime = new DateTime(date);
            data.add(new Pair<String, DateTime>(activity, dateTime));
        }
        db.close();
        ll.removeAllViews();
        for(Pair<String, DateTime> pair : data){

            TextView textView = new TextView(this);
            textView.setText(pair.first + "---->>" + pair.second.toString());
            ll.addView(textView);
        }

    }
}
