package smd.ufc.br.easycontext;

import com.google.android.gms.awareness.fence.AwarenessFence;

/**
 * Created by davitabosa on 13/08/2018.
 */

public class DetectedActivityFence extends Fence {
    private final DetectedActivityParameter params;
    private final DAMethod method;

    public DetectedActivityFence(String name, DAMethod method, FenceAction action, DetectedActivityParameter params) {
        super(name, FenceType.DETECTED_ACTIVITY, action, params);
        this.params = params;
        this.method = method;
    }


    @Override
    public AwarenessFence getMethod() {
        int size = params.getActivityTypeList().size();
        int[] types = new int[size];
        for (int i = 0; i < size; i++) {
            types[i] = params.getActivityTypeList().get(i);
        }
        switch(method){
            case DA_STOPPING:
                return com.google.android.gms.awareness.fence.DetectedActivityFence.stopping(types);
            case DA_STARTING:
                return com.google.android.gms.awareness.fence.DetectedActivityFence.starting(types);
            case DA_DURING:
                return com.google.android.gms.awareness.fence.DetectedActivityFence.during(types);
            default:
                return null;
        }
    }
}
