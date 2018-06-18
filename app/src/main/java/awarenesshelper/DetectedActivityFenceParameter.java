package awarenesshelper;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by davitabosa on 18/06/2018.
 */

public class DetectedActivityFenceParameter implements FenceParameter {
    private ArrayList<Integer> activityTypeList = null;



    public DetectedActivityFenceParameter(){
        activityTypeList = new ArrayList<Integer>();
        Log.d("AwarenessLib","DetectedActivityFenceParameter created.");
    }

    public ArrayList<Integer> getActivityTypeList() {
        return activityTypeList;
    }

    public void addActivityType(int activityType){
        activityTypeList.add(activityType);
    }
}
