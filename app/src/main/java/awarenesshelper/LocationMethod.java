package awarenesshelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by davitabosa on 13/08/2018.
 */

enum LocationMethod {
    LOCATION_ENTERING("Location.ENTERING"),
    LOCATION_EXITING("Location.EXITING"),
    LOCATION_IN("Location.IN");
    private String value;
    private static final Map<String, LocationMethod> map = new HashMap<>();
    static {
        for (LocationMethod en : values()) {
            map.put(en.value, en);
        }
    }

    public static LocationMethod valueFor(String name) {
        return map.get(name);
    }
    LocationMethod(String valueOf){
        this.value = valueOf;
    }
}
