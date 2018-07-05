package awarenesshelper;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.FenceClient;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import br.ufc.great.awarenesslib.R;

public class Configurator {

    private FenceClient fenceClient;
    private static ArrayList<Fence> registredFences;
    private Activity activity;
    private Service service;
    private static Context context;
    private static Configurator instance;
    private static String activityName;
    private static ArrayList<AwarenessActivity> activities;

    private Configurator(Activity activity){
        this.activity = activity;
        registredFences = new ArrayList<>();
        context = activity.getApplicationContext();
        this.fenceClient = Awareness.getFenceClient(context);
        activityName = activity.getClass().getName();


        Log.d("AwarenessLib",activityName);

        try {
            readJSON();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Configurator(Service service){
        this.service = service;
        registredFences = new ArrayList<>();
        context = service.getApplicationContext();
        this.fenceClient = Awareness.getFenceClient(context);
        activityName = service.getClass().getName();
        Log.d("AwarenessLib",activityName);

        try {
            readJSON();
        } catch (IOException e) {
            Log.e("AwarenessLib", "Error reading JSON");
            e.printStackTrace();
        }
    }

    /**
     * Getter for the Activity which this Configurator is bound to
     * @return null if it's bound by a service, otherwise the bound activity
     *
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * Getter for the Service which this Configurator is bound to
     * @return null if it's bound to an activity, otherwise the bound service
     */
    public Service getService() {
        return service;
    }

    private void readJSON() throws IOException {
        Gson g = new Gson();
        InputStream is = context.getResources().openRawResource(R.raw.configuration);
        InputStreamReader reader = new InputStreamReader(is);

        JsonReader jsonReader = g.newJsonReader(reader); //fazer um novo reader de json

        jsonReader.beginObject(); //espera um inicio de objeto

        if(jsonReader.nextName().equals("activities")){
            activities = parseActivitiesList(jsonReader);
        }
        if(activities == null || activities.isEmpty()){
            Log.d("AwarenessLib","No activities");
        }
        Log.d("AwarenessHelper", "JSON read..");
    }
    //TODO: Criar parser externo
    private ArrayList<AwarenessActivity> parseActivitiesList(JsonReader jsonReader) throws IOException {
        ArrayList<AwarenessActivity> activitiesList = new ArrayList<>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()){
            AwarenessActivity ac = parseActivity(jsonReader);
            if(ac == null){

                continue;
            }
            else{
                activitiesList.add(ac);
            }


        }
        jsonReader.endArray();
        return activitiesList;
    }


    @Nullable
    private AwarenessActivity parseActivity(JsonReader jsonReader) throws IOException {
        ArrayList<Fence> fence = null;
        AwarenessActivity awarenessActivity = null;
        jsonReader.beginObject();
            jsonReader.nextName(); // "name" tag
            String jsonActivityName = jsonReader.nextString();
                jsonReader.nextName(); // "fences" tag
                fence = parseFenceList(jsonReader);
                awarenessActivity = new AwarenessActivity(jsonActivityName,fence);
        jsonReader.endObject();
        return awarenessActivity;
    }

    private ArrayList<Fence> parseFenceList(JsonReader jsonReader) throws IOException {
        ArrayList<Fence> fencesList = new ArrayList<>();
        jsonReader.beginArray(); //inicio "fences" array
            while(jsonReader.hasNext()){
                fencesList.add(parseFence(jsonReader));
            }
        jsonReader.endArray();
        return fencesList;


    }

    private Fence parseFence(JsonReader jsonReader) throws IOException{
        String fenceName = null, fenceAction = null;
        FenceType fenceType = null;
        FenceParameter params = null;
        FenceMethod fenceMethod = null;
        FenceAction action = null;
        jsonReader.beginObject(); //inicio objeto fence
            while(jsonReader.hasNext()){
                String tag = jsonReader.nextName();
                switch(tag){
                    case "fenceName":
                        fenceName = jsonReader.nextString();
                        Log.d("AwarenessLib", "Action name: " + fenceName);
                        break;
                    case "fenceAction":

                        try {
                            String actionName = jsonReader.nextString();
                            Log.d("AwarenessLib", "Action name: " + actionName);
                            action = ((FenceAction) Class.forName(actionName).newInstance());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "fenceMethod":
                        String theMethod = jsonReader.nextString();
                        Log.d("AwarenessLib", "Method name: " + theMethod);
                        fenceMethod = FenceMethod.valueFor(theMethod);

                        break;
                    case "fenceType":
                        String type = jsonReader.nextString();
                        Log.d("AwarenessLib", "Type name: " + type);
                        try{
                            fenceType = FenceType.valueFor(type);
                        } catch (IllegalArgumentException e){
                            Log.e("AwarenessLib", "FenceType not supported: " + type);
                            e.printStackTrace();
                        }
                        break;
                    case "params":
                        switch(fenceType){
                            case DETECTED_ACTIVITY:
                                params = parseDetectedActivityParams(jsonReader);
                                break;
                            case LOCATION:
                                params = parseLocationParams(jsonReader);
                                break;
                            case HEADPHONE:
                                params = parseHeadphoneParams(jsonReader);
                                break;
                            default:
                                Log.e("AwarenessLib","Fence type unknown.");
                                break;
                        }

                        break;
                    default:
                        Log.w("AwarenessLib","Unknown tag while reading fence object: " + tag);
                        break;
                }

            }
        jsonReader.endObject();
        return new Fence(fenceName,fenceType,action,fenceMethod,params);
    }

    private FenceParameter parseDetectedActivityParams(JsonReader jsonReader) throws IOException {
        DetectedActivityFenceParameter params = new DetectedActivityFenceParameter();

        jsonReader.beginObject();
            while(jsonReader.hasNext()){
                String tag = jsonReader.nextName();
                switch(tag){
                    case "activityTypes":{
                        //activityTypes is an array with multiple activity detection types
                        jsonReader.beginArray();
                            while(jsonReader.hasNext()){
                                int next = jsonReader.nextInt();
                                Log.d("AwarenessLib", "DetectedActivity Read: " + next);
                                params.addActivityType(next);
                            }
                            jsonReader.endArray();
                    }
                }
            }
            jsonReader.endObject();
            return params;

    }

    private HeadphoneFenceParameter parseHeadphoneParams(JsonReader jsonReader) throws IOException {
        HeadphoneFenceParameter params = new HeadphoneFenceParameter();
        jsonReader.beginObject();
        while(jsonReader.hasNext()){
            String tag = jsonReader.nextName();
            switch(tag){
                case "headphoneState":
                    int state = jsonReader.nextInt();
                    params.setHeadphoneState(state);
                    break;
                default:
                    Log.w("AwarenessLib", "Unknown tag while reading Headphone Fence parameters: " + tag);
                    break;
            }
        }
        jsonReader.endObject();

        return params;
    }
    private LocationFenceParameter parseLocationParams(JsonReader jsonReader) throws IOException {
        LocationFenceParameter params = new LocationFenceParameter();
        double latitude,longitude,radius;
        long dwellTimeMillis;
        jsonReader.beginObject(); //
        while(jsonReader.hasNext()){
            String tag = jsonReader.nextName();
            switch(tag){
                case "latitude":
                    latitude = jsonReader.nextDouble();
                    break;
                case "longitude":
                    longitude = jsonReader.nextDouble();
                    break;
                case "radius":
                    radius = jsonReader.nextDouble();
                    break;
                case "dwellTimeMillis":
                    dwellTimeMillis = jsonReader.nextLong();
                    break;
                default:
                    Log.w("AwarenessLib","Unknown tag while reading Location Fence parameters: " + tag);
                    break;
            }
        }
        jsonReader.endObject();

        return params;
    }

    public static Configurator init(Service service){
        if(instance == null){
            Log.d("AwarenessLib",service.getClass().getName());
            instance = new Configurator(service);
        }
        else{
            activityName = service.getClass().getName();
        }

        //unregister fences
        for (Fence f: registredFences) {
            unregisterFence(f);
        }
        for(AwarenessActivity a : activities){
            if(a.getName().equals(activityName)){
                ArrayList<Fence> fs = a.getFences();
                for(Fence f : fs){
                    registerFence(f);
                }
            }
        }

        return instance;
    }
    public static Configurator init(Activity activity){
	    if(instance == null){
	        Log.d("AwarenessLib",activity.getClass().getName());
	        instance = new Configurator(activity);
        }
        else{
            activityName = activity.getClass().getName();
        }
        //unregister fences
        for (Fence f: registredFences) {
            unregisterFence(f);
        }
        for(AwarenessActivity a : activities){
            if(a.getName().equals(activityName)){
                ArrayList<Fence> fs = a.getFences();
                for(Fence f : fs){
                    registerFence(f);
                }
            }
        }
		return instance;
	}



    private static void registerFence(final Fence fence) {
        final FenceAction theFenceAction = fence.getAction();
        BroadcastReceiver myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                theFenceAction.doOperation(context, FenceState.extract(intent));
            }
        };

        String filter = fence.getName();
        Intent i = new Intent(filter);
        PendingIntent pi = PendingIntent.getBroadcast(context,0,i,PendingIntent.FLAG_CANCEL_CURRENT);
        context.registerReceiver(myReceiver,new IntentFilter(filter));

        final String fenceName = fence.getName();

        Awareness.getFenceClient(context).updateFences(new FenceUpdateRequest.Builder().addFence(fence.getName(),fence.getMethod(),pi).build())
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("AwarenessHelper", "Fence registration successful: "  + fenceName);
                registredFences.add(fence);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("AwarenessHelper", "Fence registration failed for " + fenceName + ": " + e.getMessage());
            }
        });
	}

	private static void unregisterFence(final Fence fence){
        final String fenceName = fence.getName();
        Awareness.getFenceClient(context).updateFences(new FenceUpdateRequest.Builder().removeFence(fence.getName()).build())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("AwarenessLib", "Fence removal successful: " + fenceName);
                        registredFences.remove(fence);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("AwarenessLib", "Fence removal failed for "+ fenceName + ": " + e.getMessage());
                }
        });
    }

}
