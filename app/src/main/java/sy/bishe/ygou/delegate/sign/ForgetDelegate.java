package sy.bishe.ygou.delegate.sign;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.fastjson.JSONObject;

import java.text.MessageFormat;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.utils.timer.BaseTimerTask;
import sy.bishe.ygou.utils.timer.ITimerLisenter;


//忘记密码
public class ForgetDelegate extends YGouDelegate implements ITimerLisenter {

    private int count = 60; //60s

    //发送验证码过时
    private boolean isSendVerfication = false;

    private Timer timer = null;

    @BindView(R2.id.forget_check)
    AppCompatEditText edit_check = null;

    @BindView(R2.id.forget_recheck)
    AppCompatEditText edit_recheck = null;

    @BindView(R2.id.forget_phone)
    AppCompatEditText edit_phone = null;

    @BindView(R2.id.forget_ver)
    AppCompatEditText edit_ver = null;

    /**
     * 返回
     */
    @OnClick(R2.id.forget_back)
    void back(){
        getFragmentManager().popBackStack();
    }

    /**
     *
     * 发送验证码
     */

    @BindView(R2.id.forget_send)
    AppCompatTextView tv_send = null;
    @OnClick(R2.id.forget_send)
    void send(){

        String phonenumber = edit_phone.getText().toString();

        if (checkInfomation()){
            //可以发验证码
            if (timer!=null){
                final BaseTimerTask task = new BaseTimerTask(this);
                timer.schedule(task,0,1000);
            }else {
                initTimer();
            }
            if (phonenumber.isEmpty()|| phonenumber.length()!=11){
                edit_phone.setError("请输入正确的手机号码");
            }
            else {
                //没有被注册
                SMSSDK.getVerificationCode("86", phonenumber);
                isSendVerfication = true;
                edit_phone.setError(null);
            }
        }
    }

    /**
     * 核实信息
     * @return
     */
    private boolean checkInfomation() {

        boolean isok = true;
        String check = edit_check.getText().toString();
        String recheck = edit_recheck.getText().toString();
        String phone = edit_phone.getText().toString();
        if (check==null||recheck==null||phone==null){
            edit_check.setError("请先填写信息");
            isok = false;
        }
        if (check.isEmpty()||check.length()<6||check.length()>16){
            edit_check.setError("密码不少于六位不大于16位");
            isok = false;
        }else {
            edit_check.setError(null);
        }
        if (recheck.isEmpty()||!recheck.equals(check)){
            edit_recheck.setError("两次密码不一致");
            isok = false;
        }else {
            edit_recheck.setError(null);
        }
        if (phone.isEmpty()|| !Patterns.PHONE.matcher(phone).matches()){
            edit_phone.setError("请输入正确号码");
            isok =false;
        }else {
            RestClient.builder()
                    .setUrl("user/queryByPhone/"+phone)
                    .onSuccess(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            Log.i("手机注册信息", "onSuccess: "+response);
                            String status = JSONObject.parseObject(response).getString("status");
                            if (status!=null&&status.equals("ok")){
                                edit_phone.setError(null);
                            }else {
                                edit_phone.setError("改手机号未曾注册");
                            }
                        }
                    }).build()
                    .get();
        }
        return isok;
    }

    /**
     * 提交
     */

    @OnClick(R2.id.ll_forget_sub)
    void sub(){
        SMSSDK.submitVerificationCode("86",edit_phone.getText().toString(),edit_ver.getText().toString());
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_forget;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        SMSSDK.registerEventHandler(new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                //回调完成
                if(result==SMSSDK.RESULT_COMPLETE){
                    //提交验证码成功
                    if(event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                        String pass = edit_check.getText().toString();
                        RequestBody user_password = RequestBody.create(YGouApp.JSON, pass);
                        RestClient.builder()
                                .setUrl("user/updatePassByPhone/"+edit_phone.getText().toString())
                                .setBody(user_password)
                                .onSuccess(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        JSONObject jsonObject = JSONObject.parseObject(response);
                                        String status = jsonObject.getString("status");
                                        if (status!=null&&status.equals("ok")){
                                            tologin();
                                        }
                                        else {
                                            Toast.makeText(getContext(),"验证失败，稍后再试",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .build()
                                .post();
                    }
                    else{
                        edit_ver.setError("验证码错误");
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

    /**
     * 去登录
     */
    public void tologin(){
        getSupportDelegate().start(new SigninDelegate());
    }

    @Override
    public void onTimer() {
        getSupportActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(tv_send != null && isSendVerfication == true){
                    tv_send.setText(MessageFormat.format("\n{0}s",count));
                    count--;
                    if (count<0){
                        tv_send.setText("发送");
                        isSendVerfication = true;
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
     * 初始化timer
     */
    private void initTimer(){
        timer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        timer.schedule(task,0,1000);
    }
}
