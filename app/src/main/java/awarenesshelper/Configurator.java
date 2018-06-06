package awarenesshelper;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.FenceClient;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import br.ufc.great.awarenesslib.MyCustomAction;

public class Configurator {

    private static FenceClient fenceClient;
    private static List<Fence> fences;
    private static Context context;
    private static Configurator instance;

    private Configurator(Context context){
        this.context = context;
        fenceClient = Awareness.getFenceClient(context);
        fences = new ArrayList<Fence>();
        readJSON();

    }

    private void readJSON() {
        Log.d("AwarenessHelper", "JSON lido..");
        Fence headphoneFence = new Fence("headphoneFence",FenceType.HEADPHONE, new MyCustomAction());
        fences.add(headphoneFence);
    }

	public static Configurator init(Context context) {
        if(instance == null){
            instance = new Configurator(context);

        }

        return instance;
	}

    public static Configurator init(Activity activity){
	    if(instance == null){
	        instance = new Configurator(activity.getApplicationContext());
        }
        for(Fence f : fences)
            registerFence(f);
		return instance;
	}

    private static void registerFence(Fence fence) {
        final FenceAction theFenceAction = fence.getAction();
        BroadcastReceiver myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                theFenceAction.doOperation(context, FenceState.extract(intent));
            }
        };
        //TODO: Filtros de Intent devem ser gerados ou o usuario determina??
        String filter = "SHOULD_BE_GENERATED";
        Intent i = new Intent(filter);
        PendingIntent pi = PendingIntent.getBroadcast(context,0,i,PendingIntent.FLAG_CANCEL_CURRENT);
        context.registerReceiver(myReceiver,new IntentFilter(filter));

        Awareness.getFenceClient(context).updateFences(new FenceUpdateRequest.Builder().addFence(fence.getName(),fence.getMethod(),pi).build())
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("AwarenessHelper", "Fence registration successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("AwarenessHelper", "Fence registration failed: " + e.getMessage());
            }
        });
	}

}
