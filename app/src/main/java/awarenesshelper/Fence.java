package awarenesshelper;

import android.annotation.SuppressLint;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.awareness.fence.HeadphoneFence;
import com.google.android.gms.awareness.fence.LocationFence;

public class Fence {
    private String name;
    private FenceType type;
    private FenceMethod method;
    private FenceAction action;
    private FenceParameter params;

    public Fence(String name, FenceType type, FenceAction action, FenceMethod method, FenceParameter params){
		this.name = name;
		this.type = type;
		this.action = action;

		switch (type){
            case HEADPHONE:
                if(!(params instanceof HeadphoneFenceParameter))
                    throw new IllegalArgumentException("Fence parameters of different types, expected a HeadphoneFenceParameter, got " +
                            params.getClass().getSimpleName());
                break;
            case LOCATION:

                if(!(params instanceof LocationFenceParameter))
                    throw new IllegalArgumentException("Fence parameters of different types, expected a LocationFenceParameter, got " +
                            params.getClass().getSimpleName());
                break;
            case DETECTED_ACTIVITY:

                if(!(params instanceof DetectedActivityFenceParameter))
                    throw new IllegalArgumentException("Fence parameters of different types, expected a DetectedActivityFenceParameter, got " +
                            params.getClass().getSimpleName());
                break;
            default:
                break;
        }
        //TODO: validar methods
        this.params = params;
        this.method = method;
        if(!validateMethods()){
            throw new IllegalArgumentException("Params of different methods.");
        }
	}

    private boolean validateMethods(){
        return true;
    }
	public FenceAction getAction() {
		return action;
	}

	@SuppressLint("MissingPermission")
    public AwarenessFence getMethod() {
        DetectedActivityFenceParameter daParams;
        LocationFenceParameter locParams;
        HeadphoneFenceParameter hParams;
		switch (method){
            case DA_DURING:
                int[] types;
            {
                    daParams = (DetectedActivityFenceParameter) params;
                    types = new int[daParams.getActivityTypeList().size()];
                    for (int i = 0; i < daParams.getActivityTypeList().size(); i++) {
                        types[i] = daParams.getActivityTypeList().get(i);
                    }
                }
                return DetectedActivityFence.during(types);
            case DA_STARTING:
                {
                    daParams = (DetectedActivityFenceParameter) params;
                    types = new int[daParams.getActivityTypeList().size()];
                    for (int i = 0; i < daParams.getActivityTypeList().size(); i++) {
                        types[i] = daParams.getActivityTypeList().get(i);
                    }
                }
                return DetectedActivityFence.starting(types);
            case DA_STOPPING:
                {
                    daParams = (DetectedActivityFenceParameter) params;
                    types = new int[daParams.getActivityTypeList().size()];
                    for (int i = 0; i < daParams.getActivityTypeList().size(); i++) {
                        types[i] = daParams.getActivityTypeList().get(i);
                    }
                }
                return DetectedActivityFence.stopping(types);
            case LOCATION_IN:
                locParams = (LocationFenceParameter) params;
                return LocationFence.in(locParams.getLatitude(),locParams.getLongitude(),
                        locParams.getRadius(),locParams.getDwellTimeMillis());
            case LOCATION_EXITING:
                locParams = (LocationFenceParameter) params;
                return LocationFence.exiting(locParams.getLatitude(),locParams.getLongitude(),locParams.getRadius());
            case LOCATION_ENTERING:
                locParams = (LocationFenceParameter) params;
                return LocationFence.entering(locParams.getLatitude(),locParams.getLongitude(),locParams.getRadius());
            case HEADPHONE_DURING:
                hParams = (HeadphoneFenceParameter) params;
                return HeadphoneFence.during(hParams.getHeadphoneState());
            case HEADPHONE_UNPLUGGING:
                return HeadphoneFence.unplugging();
            case HEADPHONE_PLUGGING_IN:
                return HeadphoneFence.pluggingIn();
            default:
                throw new UnsupportedOperationException("Not supported yet.");
        }
	}

	public String getName() {
		return name;
	}

}
