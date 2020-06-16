package sy.bishe.ygou.delegate.sign;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.textfield.TextInputEditText;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.account.ISignLisenter;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.bean.UserBean;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.buttons.ButtonDelegate;
import sy.bishe.ygou.net.callback.IFailure;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class SigninDelegate extends YGouDelegate {

    private UserBean userBean = new UserBean();//用户bean

    @BindView(R2.id.edit_signin_nameoremial)
    TextInputEditText sNameoremial = null;

    @BindView(R2.id.edit_signin_password)
    TextInputEditText sPassword = null;

    private ISignLisenter signLisenter = null;
    private IWXAPI iwxapi = YGouApp.iwxapi;

    public static  String nameoremial = null;
    public static  String password = null;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignLisenter){
            signLisenter = (ISignLisenter) activity;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 登录
     */
    @OnClick(R2.id.btn_signin)
    void onClicksignin(){
        nameoremial = sNameoremial.getText().toString();
        password = sPassword.getText().toString();
        if (checkInformation()) {
            JSONObject jsonObject = new JSONObject();
            //将登录的用户名传到后端
            jsonObject.put("user_name", nameoremial);
            jsonObject.put("user_password", password);
            RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());
            RestClient.builder()
                    .setUrl("user/login")
                    .setBody(requestBody)
                    .onSuccess(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            Log.i("登录", response);
                            JSONObject jsonObject = JSONObject.parseObject(response);
                            String status = jsonObject.getString("status");
                            Log.i("UserBean", ""+userBean);
                            if (status.equals("ok")) {
                                userBean = jsonObject.getJSONObject("jsonObject").toJavaObject(UserBean.class);
                                signLisenter.onSignInSuccess();
                                //登陆成功则注册JMessage
                                YGouPreferences.addCustomAppProfile("userId", String.valueOf(userBean.getUser_id()));
                                YGouPreferences.addCustomAppProfile("userName",userBean.getUser_name());
                                YGouPreferences.addCustomAppProfile("userBalance",String.valueOf(userBean.getUser_balance()));
                                Log.i("用户名和密码", "onSuccess: "+nameoremial+"::"+password);
                                //名字密码4-128byte
                                JMessageClient.register(YGouApp.APP_NAME+nameoremial, YGouApp.APP_NAME+password, new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        Log.i("register", "gotResult: "+i+":"+s);
                                        JMessageClient.login(YGouApp.APP_NAME+nameoremial, YGouApp.APP_NAME+password, new BasicCallback() {
                                            @Override
                                            public void gotResult(int i, String s) {
                                                Log.i("login", "gotResult: " + i + ":" + s);
                                                UserInfo myInfo = JMessageClient.getMyInfo();
                                                Log.i("USERINFO", "gotResult: "+myInfo.toString());
                                            }
                                        });
                                    }
                                });
//                                JMessageClient.register(YGouApp.APP_NAME+"rabbit", "yyyy", new BasicCallback() {
//                                    @Override
//                                    public void gotResult(int i, String s) {
//                                        Log.i("机器人傻妞", "gotResult: "+s);
////                                        JMessageClient.login(YGouApp.APP_NAME+"rabbit", "yyyy", new BasicCallback() {
////                                            @Override
////                                            public void gotResult(int i, String s) {
////                                                Log.i("login", "gotResult: " + i + ":" + s);
////                                                UserInfo myInfo = JMessageClient.getMyInfo();
////                                                Log.i("USERINFO", "gotResult: "+myInfo.toString());
////                                            }
////                                        });
//                                    }
//                                });
                                toShouyeFragment();
                            } else if (status.equals("密码错误")) {
                                sPassword.setError("密码错误");
                            } else {
                                sNameoremial.setError("不存在该用户名");
                            }
                        }
                    })
                    .failure(new IFailure() {
                        @Override
                        public void onFailure() {
                            Log.i("failure", "onFailure: ");
                        }
                    })
                    .build()
                    .post();
        }
    }

    //首页
    private void toShouyeFragment() {
        getSupportDelegate().start(new ButtonDelegate());
    }

    /**
     * 微信登录
     */
    @OnClick(R2.id.icon_signin_wechat)
    void onClickweChat(){
        if (!iwxapi.isWXAppInstalled()) {
            Log.i("WX", "未安装微信");
        } else {   if (iwxapi == null) {
            iwxapi = WXAPIFactory.createWXAPI(getActivity(), YGouApp.WX_APPID, true);
        }
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            iwxapi.sendReq(req);
        }
    }
    //注册页面
    @OnClick(R2.id.tv_signup_link)
    void onClicktoSignup(){
        getSupportDelegate().start(new SignUpDelegate());
    }

    private boolean checkInformation() {
        boolean isOk = true;
        if (nameoremial.isEmpty()){
            sNameoremial.setError("请输入用户名");
            isOk = false;
        }else {
            userBean.setUser_email(nameoremial);
            sNameoremial.setError(null);
        }
        if (password.isEmpty()||password.length()<6){
            sPassword.setError("请填写至少六位数密码");
            isOk = false;
        }else {
            userBean.setUser_password(nameoremial);
            sPassword.setError(null);
        }
        return isOk;
    }

    /**
     * 忘记密码
     */
    @OnClick(R2.id.sign_forget)
    void toForget(){
        getSupportDelegate().start(new ForgetDelegate());
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_signin;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
