package smd.ufc.br.easycontext.fence.action;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.awareness.fence.FenceState;

import java.util.ArrayList;
import java.util.List;

import smd.ufc.br.easycontext.fence.FenceAction;

/**
 * Action that bundles many other actions.
 */
public class MultipleAction implements FenceAction {

    List<FenceAction> actions;

    public MultipleAction(){
        actions = new ArrayList<>();
    }
    public MultipleAction(List<FenceAction> actions) {
        if(actions == null)
            actions = new ArrayList<>();
        this.actions = actions;
    }

    public void add(FenceAction action){
        this.actions.add(action);
    }
    public boolean remove(FenceAction action){
        return this.actions.remove(action);
    }
    public void clear(){
        this.actions.clear();
    }

    @Override
    public void doOperation(Context context, FenceState state, Bundle data) {
        for (FenceAction action :
                actions) {
            action.doOperation(context, state, data);
        }
    }
}
