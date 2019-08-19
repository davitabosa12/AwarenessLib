package smd.ufc.br.easycontext.fence.parameter;

import android.util.Log;

import java.util.ArrayList;

import smd.ufc.br.easycontext.fence.DetectedActivityRule;

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

        public Builder addActivityType(String activityName){
            if(activityName.equals("IN_VEHICLE")){
                activityTypeList.add(DetectedActivityRule.IN_VEHICLE);
            } else if(activityName.equals("RUNNING")){
                activityTypeList.add(DetectedActivityRule.RUNNING);
            } else if(activityName.equals("ON_FOOT")){
                activityTypeList.add(DetectedActivityRule.ON_FOOT);
            } else if(activityName.equals("ON_BICYCLE")){
                activityTypeList.add(DetectedActivityRule.ON_BICYCLE);
            } else if(activityName.equals("STILL")){
                activityTypeList.add(DetectedActivityRule.STILL);
            } else if(activityName.equals("WALKING")){
                activityTypeList.add(DetectedActivityRule.WALKING);
            } else if(activityName.equals("UNKNOWN")){
                activityTypeList.add(DetectedActivityRule.UNKNOWN);
            }
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
