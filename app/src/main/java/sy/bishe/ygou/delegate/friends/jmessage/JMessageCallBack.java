package sy.bishe.ygou.delegate.friends.jmessage;

import cn.jpush.im.api.BasicCallback;

public abstract class JMessageCallBack extends BasicCallback {

    @Override
    public void gotResult(int i, String s) {

    }
    void onSuccess(String userName, String password){

    }
    void  onFailed(int status, String desc){

    }
}
