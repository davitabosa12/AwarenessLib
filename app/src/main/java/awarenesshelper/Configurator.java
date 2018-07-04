package awarenesshelper;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
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

    private static FenceClient fenceClient;
    private static List<Fence> fences;
    private static Context context;
    private static Configurator instance;
    private String activityName;
    ArrayList<AwarenessActivity> activities;

    private Configurator(Activity activity){
        this.context = activity.getApplicationContext();
        this.fenceClient = Awareness.getFenceClient(context);
        this.activityName = activity.getClass().getName();


        Log.d("AwarenessLib",activityName);
        fences = new ArrayList<>();
        try {
            readJSON();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Configurator(Service service){
        this.context = service.getApplicationContext();
        this.fenceClient = Awareness.getFenceClient(context);
        this.activityName = service.getClass().getName();
        Log.d("AwarenessLib",activityName);

        fences = new ArrayList<>();
        try {
            readJSON();
        } catch (IOException e) {
            Log.e("AwarenessLib", "Error reading JSON");
            e.printStackTrace();
        }
    }

    private void readJSON() throws IOException {
        Gson g = new Gson();
        InputStream is = context.getResources().openRawResource(R.raw.configuration);
        InputStreamReader reader = new InputStreamReader(is);
        //Reader r = new FileReader(new File("res/configuration.json"));
        JsonReader jsonReader = g.newJsonReader(reader); //fazer um novo reader de json

        jsonReader.beginObject(); //espera um inicio de objeto
        //System.err.println(jsonReader);
        if(jsonReader.nextName().equals("activities")){
          //  jsonReader.nextName();
            activities = parseActivitiesList(jsonReader);
        }
        if(activities == null){
            Toast.makeText(context,"No activities",Toast.LENGTH_LONG).show();
        }
        else{
            for(AwarenessActivity activity : activities){
                for(Fence fence : activity.fences){
                    Log.d("AwarenessLib","Registering fence: " + fence.getName());
                    registerFence(fence);
                }
            }
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
            AwarenessActivity ac = parseActivity(jsonReader, activityName);
            if(ac == null){

                continue;
            }

            activitiesList.add(parseActivity(jsonReader,activityName));
        }
        jsonReader.endArray();
        return activitiesList;
    }


    private AwarenessActivity parseActivity(JsonReader jsonReader, String activity) throws IOException {
        ArrayList<Fence> fence = null;
        jsonReader.beginObject();
            jsonReader.nextName(); // "name" tag
            String jsonActivityName = jsonReader.nextString();
            if(jsonActivityName.equals(activity)){
                jsonReader.nextName(); // "fences" tag
                Log.d("AwarenessLib", "Parsing fence list for " + jsonActivityName );
                fence = parseFenceList(jsonReader);
            }
            else{
                Log.d("AwarenessLib", "Ignoring activity with name: " + jsonActivityName );
                jsonReader.skipValue();
            }

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

    public static Configurator init(Service service){
        if(instance == null){
            Log.d("AwarenessLib",service.getClass().getName());
            instance = new Configurator(service);
        }
    }
    public static Configurator init(Activity activity){
	    if(instance == null){
	        Log.d("AwarenessLib",activity.getClass().getName());
	        instance = new Configurator(activity);
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
