package br.ufc.great.awarenesslib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ActivityRegisterDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ActivityRegister.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ActivityRegisterContract.ActivityRegisterEntry.TABLE_NAME + " (" +
                    ActivityRegisterContract.ActivityRegisterEntry._ID + " INTEGER PRIMARY KEY," +
                    ActivityRegisterContract.ActivityRegisterEntry.COLUMN_NAME_DATE + " TEXT," +
                    ActivityRegisterContract.ActivityRegisterEntry.COLUMN_NAME_ACTIVITY + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ActivityRegisterContract.ActivityRegisterEntry.TABLE_NAME;

    public ActivityRegisterDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
