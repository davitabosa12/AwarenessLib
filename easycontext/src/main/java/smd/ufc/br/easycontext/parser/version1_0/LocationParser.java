package smd.ufc.br.easycontext.parser.version1_0;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

import smd.ufc.br.easycontext.fence.LocationRule;

public class LocationParser {

    public LocationRule parseLocationRule(JsonReader jsonReader) throws IOException {
        jsonReader.nextName(); // property 'method'
        String method = jsonReader.nextString();
        //get longitude, latitude and radius
        double latitude, longitude, radius;




    }

    static class LocationMethods {
        public static final String ENTERING= "LOCATION.ENTERING";
        public static final String EXITING= "LOCATION.EXITING";
        public static final String IN= "LOCATION.IN";
    }
}
