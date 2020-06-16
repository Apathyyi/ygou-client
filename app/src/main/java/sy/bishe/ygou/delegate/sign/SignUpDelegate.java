package sy.bishe.ygou.delegate.sign;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.textfield.TextInputEditText;

import java.text.MessageFormat;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.account.ISignLisenter;
import sy.bishe.ygou.app.account.SignHandler;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.bean.UserBean;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.utils.storage.YGouPreferences;
import sy.bishe.ygou.utils.timer.BaseTimerTask;
import sy.bishe.ygou.utils.timer.ITimerLisenter;

public class SignUpDelegate extends YGouDelegate implements ITimerLisenter {

    private int count = 60;
    private Timer timer = null;

    UserBean userBean =new UserBean();
    //用户名
    @BindView(R2.id.edit_signup_name)
    TextInputEditText sName = null;
    //邮箱
    @BindView(R2.id.edit_signup_email)
    TextInputEditText sEmail = null;
    //电话
    @BindView(R2.id.edit_signup_phone)
    TextInputEditText sPhone = null;
    //验证码
    @BindView(R2.id.edit_sign_verification)
    TextInputEditText sVerification = null;
    //密码
    @BindView(R2.id.edit_signup_password)
    TextInputEditText sPassword = null;
    //确认密码
    @BindView(R2.id.edit_signup_repassword)
    TextInputEditText sRepassword = null;

    @BindView(R2.id.aTv_verification)
    AppCompatTextView verification = null;

    //登陆监听
    private ISignLisenter signLisenter = null;

    private boolean isSendVerfication = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignLisenter){
            signLisenter = (ISignLisenter) activity;
        }
    }
    //发送验证码
    @OnClick(R2.id.aTv_verification)
    void onClickVerification(){
        if (timer!=null){
            final BaseTimerTask task = new BaseTimerTask(this);
            timer.schedule(task,0,1000);
    }else {
            initTimer();
        }
        String phonenumber = sPhone.getText().toString();
        if (phonenumber.isEmpty()|| phonenumber.length()!=11){
            sPhone.setError("请输入正确的手机号码");
        }
        else {
                //没有被注册
                SMSSDK.getVerificationCode("86", phonenumber);
                isSendVerfication = true;
                sPhone.setError(null);
        }
    }
    //注册按钮
    @OnClick(R2.id.btn_signup)
    void onClickSignup(){
        if (checkInformation()){
            //信息格式填写无误
            //默认头像
            userBean.setUser_img("https://pic4.zhimg.com/50/f9a937f76_qhd.jpg");
            //提交验证码
            SMSSDK.submitVerificationCode("86",sPhone.getText().toString(),sVerification.getText().toString());
        }
    }

    /**
     * 登录链接
     */
    @OnClick(R2.id.tv_signin_link)
    void OnclicktoSignin(){
        getSupportDelegate().start(new SigninDelegate());
    }
    private boolean checkInformation(){
        JSONObject jsonObject = new JSONObject();
        final String name = sName.getText().toString();
        final String emial = sEmail.getText().toString();
        final String phone = sPhone.getText().toString();
        final String verification = sVerification.getText().toString();
        final String password = sPassword.getText().toString();
        final String repassword = sRepassword.getText().toString();
        boolean isOk = true;
        if (name.isEmpty()){
            sName.setError("请输入用户名");
            isOk = false;
        }else if (name.length()>8){
            sName.setError("用户名太长");
            isOk = false;
        }else {
            //检查用户名是否注册过
            //将登录的用户名传到后端
            jsonObject.put("user_name", name);
            RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());
            RestClient.builder()
                    .setUrl("user/queryByName")
                    .setBody(requestBody)
                    .onSuccess(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            JSONObject jsonObject = JSONObject.parseObject(response);
                            Log.i("response", response + name);
                            String status = jsonObject.getString("status");
                            if (status.equals("ok")){
                                sName.setError("用户名已被注册，请换一个名称");

                            }else {
                                userBean.setUser_name(name);
                                sName.setError(null);
                            }
                        }
                    })
                    .build()
                    .post();
        }
        if (emial.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emial).matches()){
            sEmail.setError("请输入正确的邮箱");
            isOk = false;
        }else {
            userBean.setUser_email(emial);
            sEmail.setError(null);
        }
        if (phone.isEmpty()|| phone.length()!=11){
            sPhone.setError("请输入正确的手机号码");
            isOk = false;
        }
        else {
            //手机号已注册过
            jsonObject.put("user_phone", phone);
            RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());
            RestClient.builder()
                    .setUrl("user/queryByPhone")
                    .setBody(requestBody)
                    .onSuccess(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            JSONObject jsonObject = JSONObject.parseObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("ok")){
                                userBean.setUser_phone(phone);
                                sPhone.setError(null);
                            }else {
                                sPhone.setError("该电话以注册过了");
                            }
                        }
                    })
                    .build()
                    .post();
        }
        if (verification.isEmpty()){
            sVerification.setError("请输入验证码");
        }else {

        }
        if (password.isEmpty()||password.length()<6){
            sPassword.setError("密码不少于六位");
            isOk = false;
        }else {
            sPassword.setError(null);
            userBean.setUser_password(password);
        }
        if (repassword.isEmpty()||repassword.length()<6||!(repassword.equals(password))){
            sRepassword.setError("请重新确认密码");
            isOk = false;
        }
        else {
            sRepassword.setError(null);
        }
        return isOk;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_singup;
    }
    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        verification.setText("发送");
        SMSSDK.registerEventHandler(new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                //回调完成
                if(result==SMSSDK.RESULT_COMPLETE){
                    //提交验证码成功
                    if(event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                            //注册成功
                            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(userBean);
                             Log.i("验证码成功:userBean", userBean.getUser_name()+userBean.getUser_password());
                             Log.i("验证码成功:userBean", jsonObject.toJSONString());
                             RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());
                             RestClient.builder()
                                     .setUrl("user/add")
                                     .setBody(requestBody)
                                     .onSuccess(new ISuccess() {
                                         @Override
                                         public void onSuccess(String response) {
                                             JSONObject jsonObject = JSONObject.parseObject(response);
                                             String status = jsonObject.getString("status");
                                             if (status.equals("ok")){
                                                 YGouPreferences.addCustomAppProfile("SignUp","yes");
                                                 toLoginActivity();
                                             }
                                             else {

                                             }
                                         }
                                     })
                                     .build()
                                     .post();
                        }
                    else{
                            sVerification.setError("验证码错误");
                        }
                //    }
                }
                //获取验证码成功
                else if(event==SMSSDK.EVENT_GET_VERIFICATION_CODE){

                }
                //返回支持国家的列表
                else if(event==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){

                }
                //失败
                else {

                }
            }
        });
    }

    @Override
    public void onTimer() {
        getSupportActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(verification != null && isSendVerfication == true){
                    verification.setText(MessageFormat.format("\n{0}s",count));
                    count--;
                    if (count<0){
                        verification.setText("发送");
                        isSendVerfication = false;
                        if (timer != null){
                            timer.cancel();
                            timer = null;
                        }
                    }
                }
            }
        });
    }
    /**
     * 注册成功跳转到登录界面
     */
    private void toLoginActivity() {
        SignHandler.onSignUp(userBean,signLisenter);
        getSupportDelegate().start(new SigninDelegate());
    }

    /**
     * 初始化timer
     */
    private void initTimer(){
        timer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        timer.schedule(task,0,1000);
    }
}
