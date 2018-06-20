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
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Configurator {

    private static FenceClient fenceClient;
    private static List<Fence> fences;
    private static Context context;
    private static Configurator instance;

    private Configurator(Context context){
        this.context = context;
        fenceClient = Awareness.getFenceClient(context);
        fences = new ArrayList<Fence>();
        try {
            readJSON();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readJSON() throws IOException {
        Gson g = new Gson();
        Reader r = new FileReader(new File("res/configuration.json"));
        JsonReader jsonReader = g.newJsonReader(r); //fazer um novo reader de json

        jsonReader.beginObject(); //espera um inicio de objeto
        if(jsonReader.peek().name().equals("activities")){
            jsonReader.nextName();
            ArrayList<AwarenessActivity> activities = parseActivitiesList(jsonReader);
        }

        Log.d("AwarenessHelper", "JSON lido..");




        /*Fence headphoneFence = new Fence("headphoneFence",FenceType.HEADPHONE, new MyCustomAction());
        fences.add(headphoneFence);*/
    }
    //TODO: Criar parser externo
    private ArrayList<AwarenessActivity> parseActivitiesList(JsonReader jsonReader) throws IOException {
        ArrayList<AwarenessActivity> activitiesList = new ArrayList<AwarenessActivity>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()){
            activitiesList.add(parseActivity(jsonReader));
        }
        jsonReader.endArray();
        return activitiesList;
    }


    private AwarenessActivity parseActivity(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
            jsonReader.nextName(); // "name" tag
            String activityName = jsonReader.nextString();
            jsonReader.nextName(); // "fences" tag
            ArrayList<Fence> fence = parseFenceList(jsonReader);
        jsonReader.endObject();
        return new AwarenessActivity(activityName,fence);
    }

    private ArrayList<Fence> parseFenceList(JsonReader jsonReader) throws IOException {
        ArrayList<Fence> fencesList = new ArrayList<Fence>();
        jsonReader.beginArray(); //inicio "fences" array
            while(jsonReader.hasNext()){
                try {
                    fencesList.add(parseFence(jsonReader));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        jsonReader.endArray();
        return fencesList;


    }

    private Fence parseFence(JsonReader jsonReader) throws IOException, ClassNotFoundException{
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
                        break;
                    case "fenceAction":

                        fenceAction = jsonReader.nextString();
                        Class<?> theAction = Class.forName(fenceAction);

                        try {
                            Constructor<?> constructor = theAction.getConstructor();
                            Object obj = constructor.newInstance();
                            if(obj instanceof FenceAction)
                                action = (FenceAction) theAction.cast(obj);
                            else
                                Log.e("AwarenessLib", "Cast obj is not of the same type. theAction: "
                                        + theAction.getSimpleName() + " obj: " + obj.getClass().getSimpleName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "fenceMethod":
                        fenceMethod = FenceMethod.valueOf(jsonReader.nextString());
                        break;
                    case "fenceType":
                        String type = jsonReader.nextString();
                        try{
                            fenceType = FenceType.valueOf(type);
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

    private FenceParameter parseDetectedActivityParams(JsonReader jsonReader) {
        throw new UnsupportedOperationException("Under renovations.");
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

        AwarenessFence myFence= fence.getMethod();

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
