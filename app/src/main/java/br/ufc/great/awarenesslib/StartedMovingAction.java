package br.ufc.great.awarenesslib;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.awareness.fence.FenceState;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import awarenesshelper.FenceAction;

public class StartedMovingAction implements FenceAction {

        public static final String TAG = "StartedMovingAction";
        @Override
        public void doOperation(Context context, FenceState state) {
            if(state.getCurrentState() == FenceState.TRUE){
                ActivityRegisterDbHelper helper = new ActivityRegisterDbHelper(context);
                SQLiteDatabase db = helper.getWritableDatabase();
                JodaTimeAndroid.init(context);
                DateTime now = DateTime.now();
                ContentValues values = new ContentValues();
                values.put(ActivityRegisterContract.ActivityRegisterEntry.COLUMN_NAME_DATE, now.toString());
                values.put(ActivityRegisterContract.ActivityRegisterEntry.COLUMN_NAME_ACTIVITY, ActivityRegister.STARTED);

                long result = db.insert(ActivityRegisterContract.ActivityRegisterEntry.TABLE_NAME, null, values);

                if(result < 0){
                    Log.e(TAG, "doOperation: failed inserting row");
                }

            }
    }
}
