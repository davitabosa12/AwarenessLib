package awarenesshelper;

/**
 * Created by davitabosa on 18/06/2018.
 */

public class HeadphoneParameter implements FenceParameter {
    int headphoneState;
    private HeadphoneParameter(int headphoneState){
        this.headphoneState = headphoneState;
    }

    public int getHeadphoneState() {
        return headphoneState;
    }



    class Builder{
        private int headphoneState = 0;
        public Builder(){

        }

        public Builder setHeadphoneState(int headphoneState) {
            this.headphoneState = headphoneState;
            return this;
        }

        public HeadphoneParameter build(){
            return new HeadphoneParameter(headphoneState);
        }


    }
}
