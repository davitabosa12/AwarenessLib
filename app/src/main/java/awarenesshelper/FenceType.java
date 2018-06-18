package awarenesshelper;

public enum FenceType {


	DETECTED_ACTIVITY("DetectedActivity"),

	HEADPHONE("Headphone"),

	LOCATION("Location");

	private String value;
	FenceType(String valueOf){
	    this.value = valueOf;
    }

}
