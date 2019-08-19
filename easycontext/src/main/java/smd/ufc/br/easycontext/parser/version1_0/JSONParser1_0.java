package smd.ufc.br.easycontext.parser.version1_0;

import android.util.Log;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import smd.ufc.br.easycontext.Configuration;
import smd.ufc.br.easycontext.fence.Fence;
import smd.ufc.br.easycontext.fence.FenceAction;
import smd.ufc.br.easycontext.fence.Rule;
import smd.ufc.br.easycontext.parser.JSONParser;

public class JSONParser1_0 implements JSONParser {
    public static final String TAG = "JSONParser1_0";
    @Override
    public Configuration parseJSON(JsonReader jsonReader) throws IOException {
        String token = jsonReader.nextName();
        if(token.equals("fences")){
            List<Fence> fences = parseFenceList(jsonReader);
        }
        return null;
    }

    private List<Fence> parseFenceList(JsonReader jsonReader) throws IOException {
        List<Fence> fences = new ArrayList<>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()){
            fences.add(parseFence(jsonReader));
        }
        jsonReader.endArray();
    }

    private Fence parseFence(JsonReader jsonReader) throws IOException {
        String name;
        Rule rule;
        FenceAction action;
        jsonReader.beginObject();
        jsonReader.nextName(); // property 'name'
        name = jsonReader.nextString();
        jsonReader.nextName(); // property 'rule'
        rule = parseRule(jsonReader);
        jsonReader.nextName(); // property 'action'
        action = parseAction(jsonReader);
        jsonReader.endObject();
    }

    private FenceAction parseAction(JsonReader jsonReader) {
        return null;
    }

    private Rule parseRule(JsonReader jsonReader) throws IOException {
        String ruleName;
        jsonReader.beginObject();
        jsonReader.nextName(); // property ruleName
        ruleName = jsonReader.nextString();
        Rule rule = null;
        //check rule types
        if(ruleName.equals("Headphone")){
            //parse headphone rule...

        } else if(ruleName.equals("Location")){
            //parse location rule...
        } else if(ruleName.equals("Activity")){
            //parse activity rule...
        } else if(ruleName.equals("TimeInterval")){
            //parse TimeInterval rule...
        } else if(ruleName.equals("Aggregate")){
            //parse aggregate rule...
        } else {
            //unknown rule
            Log.e(TAG, "parseRule: unknown rule type '" + ruleName + "'.");
            return null;
        }
        return null;

    }
}
