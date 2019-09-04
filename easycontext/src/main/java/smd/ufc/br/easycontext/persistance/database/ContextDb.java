package smd.ufc.br.easycontext.persistance.database;

import android.content.Context;
import android.content.SharedPreferences;

public class ContextDb {
    SharedPreferences sharedPreferences;
    Context context;
    public ContextDb(Context context){
        this.context = context;

    }
}
