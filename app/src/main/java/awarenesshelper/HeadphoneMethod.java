package awarenesshelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by davitabosa on 13/08/2018.
 */

public enum HeadphoneMethod {
    HEADPHONE_DURING("Headphone.DURING"),
    HEADPHONE_PLUGGING_IN("Headphone.PLUGGING_IN"),
    HEADPHONE_UNPLUGGING("Headphone.UNPLUGGING");
    private String value;
    private static final Map<String, HeadphoneMethod> map = new HashMap<>();
    static {
        for (HeadphoneMethod en : values()) {
            map.put(en.value, en);
        }
    }

    public static HeadphoneMethod valueFor(String name) {
        return map.get(name);
    }
    HeadphoneMethod(String valueOf){
        this.value = valueOf;
    }
}
