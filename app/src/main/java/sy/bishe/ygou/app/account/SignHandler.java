package sy.bishe.ygou.app.account;

import sy.bishe.ygou.bean.UserBean;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class SignHandler {
    public static void onSignUp(UserBean userBean,ISignLisenter signLisenter){
        String signup = YGouPreferences.getCustomAppProfile("SignUp");
        if (signup!=null){
            AccountManager.setSignTag(true);
            signLisenter.onSignUpSucess();
        }
    }
    public static void onSignIn(UserBean userBean,ISignLisenter signLisenter){
        String userId = YGouPreferences.getCustomAppProfile("userId");
        if (userId!=null){
            AccountManager.setSignTag(true);
            signLisenter.onSignInSuccess();
        }
    }
}
