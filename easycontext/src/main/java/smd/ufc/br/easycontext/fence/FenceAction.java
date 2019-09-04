package smd.ufc.br.easycontext.fence;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.awareness.fence.FenceState;

import java.io.Serializable;

public abstract class FenceAction implements Serializable {

	public abstract void doOperation(Context context, FenceState state, Bundle data);

	@Override
	public String toString() {
		return this.getClass().getCanonicalName();
	}
}
