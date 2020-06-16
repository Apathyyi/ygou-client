package sy.bishe.ygou.app.account;

import sy.bishe.ygou.utils.storage.YGouPreferences;

public class AccountManager {
    private enum SignTag{
        SIGN_TAG
    }
    //保存登陆状态
    public static void setSignTag(boolean tag){
        YGouPreferences.setAppFlag(SignTag.SIGN_TAG.name(),tag);
    }
    //返回是否登录
    private static boolean isSign(){
        return YGouPreferences.getAppFlag(SignTag.SIGN_TAG.name());
    }
    public static void checkAccount(IUserChecker checker){
        if (isSign()){
            checker.onSignIn();
        }else {
            checker.offSignIn();
        }

    }
}
