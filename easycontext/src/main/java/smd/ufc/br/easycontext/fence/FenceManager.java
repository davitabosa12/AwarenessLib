package smd.ufc.br.easycontext.fence;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.FenceClient;
import com.google.android.gms.awareness.fence.FenceQueryRequest;
import com.google.android.gms.awareness.fence.FenceQueryResponse;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import smd.ufc.br.easycontext.GeneralReceiver;

/**
 * Created by davitabosa on 08/08/2018.
 */

public class FenceManager implements OnCompleteListener<FenceQueryResponse> {
    private static FenceManager instance;
    private FenceClient client;
    private List<String> registeredFences;
    private Context context;


    private FenceManager(Context ctx){
        this.context = ctx;
        this.client = Awareness.getFenceClient(context);
        registeredFences = new ArrayList<>();
        client.queryFences(FenceQueryRequest.all()).addOnCompleteListener(this);


    }

    public static FenceManager getInstance(Context ctx){
        if(instance == null){
            instance = new FenceManager(ctx);
        }
        return instance;
    }
    public boolean isFenceRegistered(Fence fence){
        if(registeredFences.indexOf(fence.getName()) < 0){
            //fence isn't registered
            return false;
        }
        return true;
    }

    @Deprecated
    public Task registerFence(Fence fence){
        //check if fence is already registered.
        if(isFenceRegistered(fence)){

            Log.d("AwarenessLib", "Fence " + fence.getName() + "is already registered.");
            return null;
        }
        else{
            final FenceAction theFenceAction = fence.getAction();
            BroadcastReceiver myReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    theFenceAction.doOperation(context, FenceState.extract(intent), intent.getBundleExtra("user_provided"));
                }
            };

            String filter = fence.getName();
            Intent i = new Intent(filter);
            PendingIntent pi = PendingIntent.getBroadcast(context, new Random().nextInt(),i,PendingIntent.FLAG_CANCEL_CURRENT);
            context.registerReceiver(myReceiver,new IntentFilter(filter));

            final String fenceName = fence.getName();

            Task t = client.updateFences(new FenceUpdateRequest.Builder().addFence(fence.getName(),fence.getRule().getAwarenessFence(),pi).build());
            return t;
        }

    }

    public Task registerFence2(Fence fence, @Nullable Bundle extras){
        Intent i = new Intent(context, GeneralReceiver.class);
        String actionName = fence.getAction().getClass().getName();
        i.putExtra("actionName", actionName);
        i.putExtra("user_provided", extras);
        //pending intent to trigger GeneralReceiver
        PendingIntent pi = PendingIntent.getBroadcast(context, new Random().nextInt(), i , PendingIntent.FLAG_CANCEL_CURRENT);
        //register info to Awareness API
        return client.updateFences(new FenceUpdateRequest.Builder().addFence(fence.getName(), fence.getRule().getAwarenessFence(), pi).build());
    }


    @Nullable
    public Task unregisterFence(final Fence fence){
            return unregisterFence(fence.getName());
    }

    public Task unregisterFence(final String fenceString){
        final Task t = Awareness.getFenceClient(context).updateFences(new FenceUpdateRequest.Builder().removeFence(fenceString).build());

        t.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Log.d("AwarenessLib", "Fence removal successful: " + fenceString);
                registeredFences.remove(fenceString);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("AwarenessLib", "Fence removal failed for " + fenceString + ": " + t.getException().getMessage());
            }
        });
        return t;
    }

    public void unregisterAll(){

        final Iterator<String> it = registeredFences.iterator();
        while(it.hasNext()){
            final String f  = it.next();
            final Task t = unregisterFence(f);
            t.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("AwarenessLib", "Error removing fence " + f + ". " + t.getException().getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    it.remove();
                }
            });

        }
    }


    @Override
    public void onComplete(@NonNull Task<FenceQueryResponse> task) {
        Set<String> fromApi = task.getResult().getFenceStateMap().getFenceKeys();
        registeredFences.clear();
        registeredFences.addAll(fromApi);
    }
}
