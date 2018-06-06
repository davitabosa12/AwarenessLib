package awarenesshelper;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.HeadphoneFence;
import com.google.android.gms.awareness.state.HeadphoneState;

public class Fence {
    private String name;
    private FenceType type;
    private AwarenessFence method;
    private FenceAction action;

    public Fence(String name, FenceType type, FenceAction action){
		this.name = name;
		this.type = type;
		this.action = action;
		//TODO Generalizar o tipo de Fence para outros metodos. (Ex. LocationFence, DetectedActivityFence..)
		method = HeadphoneFence.during(HeadphoneState.PLUGGED_IN);
	}


	public FenceAction getAction() {
		return action;
	}

	public AwarenessFence getMethod() {
		return method;
	}

	public String getName() {
		return name;
	}

}
