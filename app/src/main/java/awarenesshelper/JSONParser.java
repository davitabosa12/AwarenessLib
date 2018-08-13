package awarenesshelper;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by davitabosa on 08/08/2018.
 */

public class JSONParser {
    Map<String, FenceAction> actions;
    Context context;

    public JSONParser(Context context, Map<String, FenceAction> actions) {
        this.actions = actions;
        this.context = context;
    }

    public List<AwarenessActivity> readJSON() throws IOException {
        Gson g = new Gson();
        List<AwarenessActivity> activities = null;
        Reader r = new FileReader(new File("res/configuration.json"));
        JsonReader jsonReader = g.newJsonReader(r); //fazer um novo reader de json

        jsonReader.beginObject(); //espera um inicio de objeto
        if(jsonReader.peek().name().equals("activities")){
            jsonReader.nextName();
            activities = parseActivitiesList(jsonReader);
        }

        Log.d("AwarenessHelper", "JSON lido..");
        return activities;



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
                    action = actions.get(fenceAction);
                    if(action == null)
                        Log.e("AwarenessLib", "Action " + fenceAction + " not found in Action Map. Did you spell it correctly?");
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

    private HeadphoneParameter parseHeadphoneParams(JsonReader jsonReader) throws IOException {
        HeadphoneParameter params = new HeadphoneParameter();
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
    private LocationParameter parseLocationParams(JsonReader jsonReader) throws IOException {
        LocationParameter params = new LocationParameter();
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

}
