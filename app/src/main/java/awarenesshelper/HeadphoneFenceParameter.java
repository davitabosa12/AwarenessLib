package awarenesshelper;

/**
 * Created by davitabosa on 18/06/2018.
 */

public class HeadphoneFenceParameter implements FenceParameter {
    int headphoneState = Integer.MIN_VALUE;
    public HeadphoneFenceParameter(){

    }

    public int getHeadphoneState() {
        return headphoneState;
    }

    public void setHeadphoneState(int headphoneState) {
        this.headphoneState = headphoneState;
    }
}
