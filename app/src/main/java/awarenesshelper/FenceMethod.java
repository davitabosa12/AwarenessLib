package awarenesshelper;

/**
 * Created by davitabosa on 13/06/2018.
 */

public enum FenceMethod {

    //==================================== HEADPHONE METHODS: ======================================
    HEADPHONE_DURING("Headphone.DURING"),
    HEADPHONE_PLUGGING_IN("Headphone.PLUGGING_IN"),
    HEADPHONE_UNPLUGGING("Headphone.UNPLUGGING"),

    //================================= DETECTED ACTIVITY METHODS: =================================
    DA_DURING("DetectedActivity.DURING"),
    DA_STARTING("DetectedActivity.STARTING"),
    DA_STOPPING("DetectedActivity.STOPPING"),

    //===================================== LOCATION METHODS: ======================================
    LOCATION_ENTERING("Location.ENTERING"),
    LOCATION_EXITING("Location.EXITING"),
    LOCATION_IN("Location.IN");

    private String value;
    FenceMethod(String valueOf){
        this.value = valueOf;
    }
}
