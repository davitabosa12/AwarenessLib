package smd.ufc.br.easycontext;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import smd.ufc.br.easycontext.fence.Fence;
import smd.ufc.br.easycontext.fence.FenceManager;

public class EasyContext {
    private static String TAG = "EasyContext";
    private static EasyContext instance;
    private FenceManager fenceManager;
    private Map<String, Fence> fenceList = new HashMap();
    private EasyContext(Context context){
        try {
            Configuration configuration;
            configuration = Configuration.readJSON(context);
            for(Fence fence : configuration.getFenceList()){
                fenceList.put(fence.getName(), fence);
            }
            fenceManager = FenceManager.getInstance(context);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static EasyContext init(Context context){
        if (instance == null) {
            instance = new EasyContext(context);
        }
        return instance;
    }

    public boolean watch(final String fenceName){
        Fence fence = fenceList.get(fenceName);
        if (fence != null) {
            fenceManager.registerFence(fence, null).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: could not register fence " + fenceName, e);
                }
            });
            return true;
        }
        return false;
    }

    public void watchAll(){
        Set<String> keys = fenceList.keySet();
        for (String fenceName : keys){
            watch(fenceName);
        }
    }
    public void unwatchAll(){
        Set<String> keys = fenceList.keySet();
        for (String fenceName : keys){
            unwatch(fenceName);
        }
    }

    public void unwatch(String fenceName){
        Fence fence = fenceList.get(fenceName);
        if (fence != null) {
            fenceManager.unregisterFence(fence);
        }
    }

    public FenceManager getFenceManager() {
        return fenceManager;
    }

    public Map<String, Fence> getFenceList() {
        return fenceList;
    }
}
