package sy.bishe.ygou.utils.timer;

import java.util.TimerTask;

public class BaseTimerTask extends TimerTask {
    private ITimerLisenter timerLisenter = null;

    public BaseTimerTask(ITimerLisenter timerLisenter) {
        this.timerLisenter = timerLisenter;
    }

    /**
     * 接口回调判空
     */
    @Override
    public void run() {
        if (timerLisenter!=null){
            timerLisenter.onTimer();
        }


    }
}
