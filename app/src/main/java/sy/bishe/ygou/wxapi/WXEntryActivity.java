package sy.bishe.ygou.wxapi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mob.MobSDK;
import com.mob.OperationCallback;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.sharesdk.onekeyshare.OnekeyShare;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.net.callback.IError;
import sy.bishe.ygou.net.callback.IFailure;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI iwxapi = YGouApp.iwxapi;
    private ProgressDialog  mProgressDialog = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iwxapi = WXAPIFactory.createWXAPI(this, YGouApp.WX_APPID,false);
        iwxapi.handleIntent(getIntent(),this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(intent,this);
        finish();
    }

    /**
     * 隐私授权
     * @param granted
     */
    private void submitPrivacyGrantResult(boolean granted) {
        MobSDK.submitPolicyGrantResult(granted, new OperationCallback<Void>() {
            @Override
            public void onComplete(Void data) {
                Log.d("", "隐私协议授权结果提交：成功");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("", "隐私协议授权结果提交：失败");
            }
        });
    }

    /**
     * 一键分享
     */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，确保SDcard下面存在此张图片
        oks.setImagePath("/sdcard/item_dynamic.jpg");
        // url在微信、Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }
    /**
     * 请求授权
     * @param baseReq
     */
    @Override
    public void onReq(BaseReq baseReq) {
    }

    /**
     * 回调
     * @param baseResp
     */
    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) baseResp).code;
                //获取accesstoken
                getAccessToken(code);
                Log.i("fantasychongwxlogin", code.toString()+ "");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                finish();
                break;
            default:
                finish();
                break;
        }

    }

    /**
     * 获取token
     * @param code
     */
    private void getAccessToken(String code) {
        createProgressDialog();
        //获取授权
        StringBuffer loginUrl = new StringBuffer();
        loginUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=")
                .append(YGouApp.WX_APPID)
                .append("&secret=")
                .append(YGouApp.WX_APPSercret)
                .append("&code=")
                .append(code)
                .append("&grant_type=authorization_code");
        Log.d("urlurl", loginUrl.toString());
        RestClient.builder()
                .setUrl(loginUrl.toString())
                .loader(this)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("RESPONSE", "onSuccess: "+response);
                        String access = null;
                        String openId = null;
                        try {
                            JSONObject jsonObject = JSONObject.parseObject(response);
                            access = jsonObject.getString("access_token");
                            openId = jsonObject.getString("openid");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getUserInfo(access, openId);
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Log.d("fan12", "onFailure: ");
                       mProgressDialog.dismiss();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {

                    }
                })

                .build();
    }

    /**
     * 获取信息
     * @param access
     * @param openId
     */
    private void getUserInfo(String access, String openId) {
        String getUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access + "&openid=" + openId;

        RestClient.builder()
                .setUrl(getUserInfoUrl)
                .loader(this)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("RESPONSE", "onSuccess: "+response);
                        SharedPreferences.Editor editor= getSharedPreferences("userInfo", MODE_PRIVATE).edit();
                        editor.putString("responseInfo", response);
                        editor.commit();
                        finish();
                        mProgressDialog.dismiss();
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Log.d("fan12", "onFailure: ");
                        mProgressDialog.dismiss();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {

                    }
                })
                .build();
    }

    private void createProgressDialog() {
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//转盘
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("登录中，请稍后");
        mProgressDialog.show();
    }
}
