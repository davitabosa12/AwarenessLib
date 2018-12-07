package br.ufc.great.awarenesslib;

import android.provider.BaseColumns;

public class ActivityRegisterContract {
    private ActivityRegisterContract() {
    }

    public static class ActivityRegisterEntry implements BaseColumns {
        public static final String TABLE_NAME = "ActivityRegister";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_ACTIVITY = "activity";
    }
}
