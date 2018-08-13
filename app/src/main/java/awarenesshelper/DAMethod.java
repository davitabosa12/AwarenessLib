package awarenesshelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by davitabosa on 13/08/2018.
 */

enum DAMethod {
    DA_DURING("DetectedActivity.DURING"),
    DA_STARTING("DetectedActivity.STARTING"),
    DA_STOPPING("DetectedActivity.STOPPING");
    private String value;
    private static final Map<String, DAMethod> map = new HashMap<>();
    static {
        for (DAMethod en : values()) {
            map.put(en.value, en);
        }
    }

    public static DAMethod valueFor(String name) {
        return map.get(name);
    }
    DAMethod(String valueOf){
        this.value = valueOf;
    }
}
