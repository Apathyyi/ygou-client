package sy.bishe.ygou.delegate.personal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.alipay.AuthResult;
import sy.bishe.ygou.alipay.OrderInfoUtil2_0;
import sy.bishe.ygou.alipay.PayResult;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.pay.IPayResultListener;
import sy.bishe.ygou.pay.YGouPay;
import sy.bishe.ygou.utils.storage.YGouPreferences;

//充值
public class RechargeDelegate extends YGouDelegate implements IPayResultListener {


    //支付标志
    private static final int SDK_PAY_FLAG = 1;
    //弹窗
    private static Activity activity ;
    //支付监听
    private static IPayResultListener listener = null;

    private static final int SDK_AUTH_FLAG = 2;

    @BindView(R2.id.recharge_much)
    AppCompatEditText edit_much = null;

    /**
     * 返回
     */
    @OnClick(R2.id.recharge_back)
    void back(){
        getFragmentManager().popBackStack();
    }

    /**
     * 提交
     */
    @OnClick(R2.id.recharge_sub)
    void sub(){

        if (edit_much.getText().toString().isEmpty()){
            Toast.makeText(getContext(),"请输入金额",Toast.LENGTH_SHORT).show();
        }else {

            boolean rsa2 = (YGouPay.RSA2_PRIVATE.length() > 0);

            Map<String, String> keyValues = new HashMap<String, String>();

            keyValues.put("app_id", YGouPay.APPID);

            keyValues.put("biz_content", "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\"," + "\"total_amount\": \" " + edit_much.getText().toString() + "\",\"subject\":\"1\",\"body\":\"我是测试数据\",\"out_trade_no\":\"" + OrderInfoUtil2_0.getOutTradeNo() + "\"}");

            keyValues.put("charset", "utf-8");

            keyValues.put("method", "alipay.trade.app.pay");

            keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");

            long l = System.currentTimeMillis();
            Date date = new Date(l);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddHHmmss");
            String format = simpleDateFormat.format(date);

            keyValues.put("timestamp", format);

            keyValues.put("version", "1.0");

            payV2(keyValues);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getSupportActivity();
        listener = this;
    }

    @Override
    public Object setLayout() {
        return R.layout.recharge;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {

    }

    /**
     * 支付监听
     */
    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Log.i("resultInfo", resultInfo);
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Log.i("payResult", resultStatus);
                        listener.onPaySuccess();
                        // showAlert(PayDemoActivity.this, getString(R.string.pay_success) + payResult);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        // showAlert(PayDemoActivity.this, getString(R.string.pay_failed) + payResult);
                        Log.i("payResult", resultStatus);
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();
                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
//                        showAlert(PayDemoActivity.this, getString(R.string.auth_success) + authResult);
                    } else {
                        // 其他状态值则为授权失败
//                        showAlert(ShopCartDelegate, getString(R.string.auth_failed) + authResult);
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    /**
     * 支付宝支付
     * @param map
     */
    public static void payV2(Map<String,String > map) {

        if (TextUtils.isEmpty(YGouPay.APPID) || (TextUtils.isEmpty(YGouPay.RSA2_PRIVATE) && TextUtils.isEmpty(YGouPay.RSA_PRIVATE))) {
            //showAlert(this, getString(R.string.error_missing_appid_rsa_private));
            return;
        }
        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo 的获取必须来自服务端；
         */
        boolean rsa2 = (YGouPay.RSA2_PRIVATE.length() > 0);

        String orderParam = OrderInfoUtil2_0.buildOrderParam(map);

        String privateKey = rsa2 ? YGouPay.RSA2_PRIVATE : YGouPay.RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(map, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 成功
     */
    @Override
    public void onPaySuccess() {

        RequestBody id = RequestBody.create(YGouApp.JSON, JSONObject.toJSONString(YGouPreferences.getCustomAppProfile("userId")));
        RestClient.builder()
                .setUrl("useruser/balance/add/"+edit_much.getText().toString())
                .setBody(id)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("更新余额", "onSuccess: "+response);
                        Double userBalance = Double.parseDouble(YGouPreferences.getCustomAppProfile("userBalance"));
                        userBalance = userBalance +Double.parseDouble(edit_much.getText().toString());
                        YGouPreferences.addCustomAppProfile("userBalance",String.valueOf(userBalance));
                    }
                })
                .build()
                .post();
    }

    @Override
    public void onPaying() {

    }

    @Override
    public void onPayFail() {

    }

    @Override
    public void onPayCancle() {

    }

    @Override
    public void onPayContentError() {

    }
}
