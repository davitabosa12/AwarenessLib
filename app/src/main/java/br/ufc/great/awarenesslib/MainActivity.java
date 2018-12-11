package br.ufc.great.awarenesslib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.SnapshotClient;
import com.google.android.gms.awareness.snapshot.DetectedActivityResponse;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import java.util.ArrayList;

import awarenesshelper.Configurator;

import awarenesshelper.FenceManager;

import awarenesshelper.SnapshotType;
import awarenesshelper.activity.AwarenessAppCompatActivity;
import br.ufc.great.awarenesslib.ActivityRegisterContract.ActivityRegisterEntry;

public class MainActivity extends AwarenessAppCompatActivity implements View.OnClickListener {

    ArrayList<Pair<String, DateTime>> data;
    LinearLayout ll;
    BroadcastReceiver receiver;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        data = new ArrayList<>();
        JodaTimeAndroid.init(this);
        ll = findViewById(R.id.linearScroll);
        btn = findViewById(R.id.btn_probability);
        btn.setOnClickListener(this);

        setSnapshots(SnapshotType.PLACES, SnapshotType.TIME_INTERVAL);
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

    @Override
    public void onClick(View v) {
        String  place = getMostProbablePlace().toString();
        Toast.makeText(this, "Voce esta em " + place, Toast.LENGTH_SHORT).show();

    }
}
