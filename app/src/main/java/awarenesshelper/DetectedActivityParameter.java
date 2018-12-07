package awarenesshelper;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by davitabosa on 18/06/2018.
 */

public class DetectedActivityParameter implements FenceParameter {
    private ArrayList<Integer> activityTypeList = null;



    private DetectedActivityParameter(){
        activityTypeList = new ArrayList<Integer>();
        Log.d("AwarenessLib","DetectedActivityParameter created.");
    }

    public ArrayList<Integer> getActivityTypeList() {
        return activityTypeList;
    }

    public void addActivityType(int activityType){
        activityTypeList.add(activityType);
    }

    public static class Builder{
        ArrayList<Integer> activityTypeList = new ArrayList<Integer>();
        public Builder(){

        }
        public Builder addActivityType(int activityType){
            activityTypeList.add(activityType);
            return this;
        }

        public DetectedActivityParameter build(){
            DetectedActivityParameter temp = new DetectedActivityParameter();
            for(int at : activityTypeList){
                temp.addActivityType(at);
            }
            return temp;
        }
    }
}
