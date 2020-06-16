package sy.bishe.ygou.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sy.bishe.ygou.R;
import sy.bishe.ygou.alipay.AuthResult;
import sy.bishe.ygou.alipay.OrderInfoUtil2_0;
import sy.bishe.ygou.alipay.PayResult;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.utils.storage.YGouPreferences;

import static sy.bishe.ygou.alipay.OrderInfoUtil2_0.getOutTradeNo;

public class YGouPay implements View.OnClickListener {

    public static final String APPID = "2016101900721440";
    public static final String RSA2_PRIVATE="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCbxUOiCQW2sZplmVSjUY87gQ989oOPviM/ZT+zfI+XNUAWFRn/0tMg6xrAAFVLYKe6+5h/HsfFavKceJnyjarpNbJQAlUtQ4VjgKd2mDrVzIOY/5DY0I4wuvWLNeyfLps3mq86GopYIygR9Abse48wyiEXUjmEa0SZaXb8EHwbDzdcnUDhvWw+rtTv7EBO+edEcursHAA4JVEUW2jXMFEpXpv/de8xpJ+Km7BphWDC7E/AYtJK6J+nA3Pc2udDXezo6aj6O1II0O3qbWGQ177yFGRay+sHlBBl1QllKfZb9u9RcvOD5TG7YAFApQczLSKGHkWOIn2Pga33X8g6Oh1xAgMBAAECggEAYEJnyBMRP9PC2LQHFKHLBHd9zbg8lrBl8cAsvErmJQfYIY1Kssdihv4iWrIaA2moXSkrFKs9wmaTpFP2tmgLTPBMz5qRblZCcLnJQsVR3lrym7KanRFMPRrbr/pTzg6sBpGFZT+PbGXVVjrZqHu2gez2xAWg2466Jp+A0sWRG9qxx9ymg4pHrTSgsthADdyvOAX+H+8eH4yMIpI8C5wel5ZzfclgGd7HDHjgTcG1M0kXjQdhgY7USQRNDAtSkkn31QhxQiYEvUmm3+CtZ7s+uCXweXyKb0V2IEThTv4Mo2U/c0cNESRwlI8cHUZ1SwxKLcAepYGVwoQanmNakL6orQKBgQDttxIYB8bHAyuCXYOkQKvPM2OG6NdscvYOrW4QLExT5Xg0lZV8C0W5OEYsgXIkAOe9Gk5jErj4yY5lRKS854I0Z1I4+WLwOlt1O7x6hLUsULCHILmloShq/7xjiowXOwIx3N+QYorrxTVoYy6IJEb6ZilvH7oaaqpxrQ9HmcZA5wKBgQCnwJhporfMnDFlEicnoEMM6wFoSUxdUNIc7fg/Fjw2VZlUasM3GPOhYbODF+t4pphK7aRU99jwectdkZqI9qjhBlV2Pq7o5Mn77MlU+24z0S4a7/5pGmPzZAq3/xf/mP7JC4cdnyv6nrCm1RcOXJUMeDXXfUExy0o8N7fD04Nr5wKBgAU1abQVF4nsy9tDaUsED7+uhJPELlQ2AKY5rpu0HMgJ0yRz0p6xMInqbb8z5uhbDLjcBrOtCViKMhCN/H4XJIc5bFfLPxu8a5yZ/ntAxSd/bR7I3MiAS35stBgKRR+NL3MgP9XOEvUvkQgEhFR/Vv7NlkLVEyLZC3sKK7eJ6wopAoGAZa1j9CtPcnJ9sgBFwUvvOCY9LSO4aJszL5vBqyNkDZ7ml/c6Kewyx297qgrWi8OWbVwLDgFH+cIo1KcLXtb616HKLSijRcBe+Ra1lY/sZPX2Fxe+QaBgUschhBLXTfJDBEK7e+Xdd0O2WPixx9+v6+X05I8j8/V/Im63MLfEYaMCgYEA2jv94P8MjblkQgJ7inKjGn099GJMso8Yg7+w2j0dLRl6XXKAspkPz/3E5/RLM5mzK7MWgH1l4C1X5WdgOCzQ7uWx+oX65KMpeuj4HQzR/jSFJcY6TKTDYpd2fhfWFH/1E8gDtjLNv8yHh1spyGLXG/zV4vka+ReLE6dXyr0YUlU=";
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKzKPwooKFlnFtqw52cLcnYXG4d/JQxOQz+gbl1yHL+aH9LwKmwpDaUVLU8PoA0MjjylHE0CvjZcLOYBg9ZOE23jhcOt/TqnlwQWAb+PftNUc1lqG7DguaxPisHlF5hGE77qVfGJslsZa8zcmlwqofxU6EA28NrJlRHOgOqhywbRAgMBAAECgYEAol7XSGMeL40ZQ95zRTjTkbb7LqPDG8I7nYHT1EoUS9S4TQ9UvhkB8nxo/F9xhnGjA8ggRRnrxsdthaIs0mjCBbkL6tkAA+oC0+CByZGcK3/MyfYpfmkOb3hR9Uf6MnMNse6FOxUqaIiBOeDHrH/jh65IU3QHPZ7WUjDocVzUynUCQQDZCnCKtvAb+JQXaNrCN9DbU7ceiVlAoeCYVbQ6/SIgkeuViNFDz5xhUZgYr64MscPyVNmNRLX5LaIK5aTbZNHbAkEAy85gAfyuk2whtQFyWohYv7KODTAUg8kQDi3q7yezBtunvdcXebYcJAT/CbQx79SUNfbhPitJcJfI7G2qo9WXwwJAfQtUt7aaM6NNvwb8MjVyMUrUD9XWBDS/b0TBZ9L7XIR1lFuVJVpxoGggFujwgAPS/sT/6jISHZO67khMaNJ3TQJBALgzuaByIdMKm1h+iz16GESCfFM9Dn2h3woBxGINDoj3RJ+XZRdTWhHQFjZOEAhEqQQxM5k+BG1oMmhGIkFbjoMCQC7OP28JH5QqqTZ7vTeUEJt5sAmIXINBjmPeOlgAQ/0eE3wOtSOGIheaTTSJJFd5FT6o4D2ZwAGGOro8wPLwVs8=";
    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_AUTH_FLAG = 2;
    private static IPayResultListener sIPayResultListener = null;

    /**
     * 支付监听
     * @param sIPayResultListener
     * @return
     */
    public YGouPay setsIPayResultListener(IPayResultListener sIPayResultListener) {
        this.sIPayResultListener = sIPayResultListener;
        return this;
    }

    public YGouPay setsOrderId(int sOrderId) {
        this.sOrderId = sOrderId;
        return this;
    }
    //当前以输入密码位数
    private int cuurenrtposition = 0;
    //存入输入的密码
    private List<String> list = new ArrayList<>();
    private static Activity sActivity = null;
    private AlertDialog sDialog = null;
    private int sOrderId = -1;
    private YGouDelegate delegate = null;
    private Double money ;
    private StringBuilder stringBuilder = new StringBuilder();

    public YGouPay(YGouDelegate delegate,Double money) {
        this.money = money;
        this.delegate = delegate;
        this.sActivity = delegate.getSupportActivity();
        Log.i("sActivity", sActivity.toString()+money);
        this.sDialog = new AlertDialog.Builder(delegate.getContext()).create();
    }
    public static YGouPay getInstance(YGouDelegate delegate,Double money){
        return new YGouPay(delegate,money);
    }

    /**
     * 弹框
     */
    public void beginPayDialog(){

        sDialog.show();
        final Window window = sDialog.getWindow();
        if(window!=null)
        {
        window.setContentView(R.layout.dialog_pay);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(params);
        window.findViewById(R.id.btn_dialog_pay_alpay).setOnClickListener(this::onClick);
        window.findViewById(R.id.btn_dialog_pay_wechat).setOnClickListener(this::onClick);
        window.findViewById(R.id.btn_dialog_pay_cancel).setOnClickListener(this::onClick);
        window.findViewById(R.id.ll_pay_balance).setOnClickListener(this::onClick);
        }
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        Log.d("ONCLICK", "onClick: ");
        if (id==R.id.btn_dialog_pay_alpay){
            boolean rsa2 = (YGouPay.RSA2_PRIVATE.length() > 0);
            Map<String, String> keyValues = new HashMap<String, String>();
            keyValues.put("app_id", YGouPay.APPID);
            keyValues.put("biz_content", "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\"," + "\"total_amount\": \" " + money + "\",\"subject\":\"1\",\"body\":\"我是测试数据\",\"out_trade_no\":\"" + getOutTradeNo() + "\"}");
            keyValues.put("charset", "utf-8");
            keyValues.put("method", "alipay.trade.app.pay");
            keyValues.put("timestamp","2014-07-24 08:07:50");
            keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");
            keyValues.put("version", "1.0");
            YGouPay.payV2(keyValues);
           //payV2();
        //    pay(sOrderId);
//            sDialog.cancel();
        }else if (id == R.id.btn_dialog_pay_wechat){
            //sDialog.cancel();
            payBywechat();
        }else if (id == R.id.btn_dialog_pay_cancel){
            sDialog.cancel();
            sIPayResultListener.onPayCancle();
        }else if (id == R.id.ll_pay_balance){
            Log.i("ll_pay_balance", "onClick: ");
            if (!YGouPreferences.getCustomAppProfile("isSetBalance").isEmpty()){
                payBybalance();
            }
           else {
               Toast.makeText(delegate.getContext(),"先去个人中心设置密码",Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * 余额支付
     */
    public  void payBybalance() {
        sIPayResultListener.onPaying();
        sDialog.show();
        final Window window = sDialog.getWindow();
        if(window!=null)
        {
            window.setContentView(R.layout.dialog_pay_balance);
            window.setGravity(Gravity.CENTER);
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(params);
            //六位密码
            View view1 = window.findViewById(R.id.balance_password_one);
            View view2 = window.findViewById(R.id.balance_password_two);
            View view3 = window.findViewById(R.id.balance_password_three);
            View view4 = window.findViewById(R.id.balance_password_four);
            View view5 = window.findViewById(R.id.balance_password_five);
            View view6 = window.findViewById(R.id.balance_password_six);
            List<View> listview = new ArrayList<>();
            listview.add(view1);
            listview.add(view2);
            listview.add(view3);
            listview.add(view4);
            listview.add(view5);
            listview.add(view6);
            AppCompatTextView tv_money = window.findViewById(R.id.balance_money);
            tv_money.setText(String.valueOf(money));
            String balance_userName = YGouPreferences.getCustomAppProfile("userName");
            AppCompatTextView userName = window.findViewById(R.id.balance_userName);
            userName.setText(balance_userName+")");
            window.findViewById(R.id.balance_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sDialog.cancel();
                }
            });
            window.findViewById(R.id.ll_pay_choose).setOnClickListener(this::onClick);

            GridView gridView = window.findViewById(R.id.grid_number);
            //数字键盘
            GridAdapter gridAdapter = new GridAdapter(delegate.getContext(), gridView);
            gridView.setAdapter(gridAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("Position:*****", position+"Cuurenrtposition:****"+cuurenrtposition);
                    if (cuurenrtposition==0&&position==11){
                        cuurenrtposition=0;
                    }else {
                        switch (position) {
                            case 0:
                                list.add("1");
                                listview.get(cuurenrtposition).setVisibility(View.VISIBLE);
                                break;
                            case 1:
                                list.add("2");
                                listview.get(cuurenrtposition).setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                list.add("3");
                                listview.get(cuurenrtposition).setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                list.add("4");
                                listview.get(cuurenrtposition).setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                list.add("5");
                                listview.get(cuurenrtposition).setVisibility(View.VISIBLE);
                                break;
                            case 5:
                                list.add("6");
                                listview.get(cuurenrtposition).setVisibility(View.VISIBLE);
                                break;
                            case 6:
                                list.add("7");
                                listview.get(cuurenrtposition).setVisibility(View.VISIBLE);
                                break;
                            case 7:
                                list.add("8");
                                listview.get(cuurenrtposition).setVisibility(View.VISIBLE);
                                break;
                            case 8:
                                list.add("9");
                                listview.get(cuurenrtposition).setVisibility(View.VISIBLE);
                                break;
                            case 9:
                                //移除所有
                                cuurenrtposition=-1;
                                for (int i = 0;i<list.size();i++){
                                    listview.get(i).setVisibility(View.GONE);
                                }
                                list.clear();
                                break;
                            case 10:
                                list.add("0");
                                listview.get(cuurenrtposition).setVisibility(View.VISIBLE);
                                break;
                            case 11:
                                if (list.size() != 0) {
                                    Log.i("list", "onItemClick: " + list.size());
                                    list.remove(cuurenrtposition - 1);
                                    listview.get(cuurenrtposition - 1).setVisibility(View.GONE);
                                    cuurenrtposition = cuurenrtposition - 2;
                                }
                                break;
                        }
                        //六位密码输入完成
                        if (cuurenrtposition == 5) {
                            //检查密码是否正确 完成支付
                            for (int i = 0; i < list.size(); i++) {
                                stringBuilder.append(list.get(i));
                            }
                            Log.i("密码：***", "" + stringBuilder);
                            //核对支付密码
                            if (stringBuilder.toString().equals(YGouPreferences.getCustomAppProfile("isSetBalance"))) {
                                Double balance = Double.valueOf(YGouPreferences.getCustomAppProfile("userBalance"));
                                Log.i("密码正确", "onItemClick: ");
                                if (money < balance) {
                                    balance = balance - money;
                                    //更新余额
                                    YGouPreferences.addCustomAppProfile("userBalance", String.valueOf(balance));
                                    //跳转订单详情页面
//                                sDialog.cancel();
                                    sIPayResultListener.onPaySuccess();
                                    sDialog.cancel();
                                } else {
                                    Log.i("余额不足!请先充值", "onItemClick: ");
                                    Toast.makeText(delegate.getContext(), "余额不足！请充值", Toast.LENGTH_SHORT).show();
                                    sIPayResultListener.onPayFail();
                                    sDialog.cancel();

                                }
                                //当前支付是否完成
                            } else {
                                //支付密码错误
                                Toast.makeText(delegate.getContext(), "密码错误请重新输入", Toast.LENGTH_SHORT).show();
                                for (int i = 0;i<list.size();i++){
                                    listview.get(i).setVisibility(View.GONE);
                                }
                                list.clear();
                                cuurenrtposition=-1;
                            }
                        }
                        cuurenrtposition++;
                    }
                }
            });
        }
    }

    /**
     * 微信支付
     */
    public final void payBywechat(){
        IWXAPI iwxapi = YGouApp.iwxapi;
        if (!iwxapi.isWXAppInstalled()) {
            Log.i("WX", "未安装微信");
        } else {   if (iwxapi == null) {
            iwxapi = WXAPIFactory.createWXAPI(sActivity, YGouApp.WX_APPID, true);
        }
            final PayReq payReq = new PayReq();
            payReq.appId = YGouApp.WX_APPID;
            payReq.partnerId ="";
            payReq.prepayId = "";
            payReq.packageValue ="Sign=WXPay";
            payReq.nonceStr="";
            payReq.timeStamp="";
            payReq.sign = "ede49f32d60657d84e96208335fc2608";
            iwxapi.sendReq(payReq);
        }
    }
    /**
     * 封装得alipay
     * @param orderId
     */
    public final void pay(int orderId){
        final String singUrl = "alipay.trade.app.pay"+orderId;
        String url = "http://app.api.zanzuanshi.com/api/v1/alipay/a=";
        RestClient.builder()
                .setUrl(singUrl)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final String paySign = JSONObject.parseObject(response).getString("result");
                        Log.i("PAYSIGN", paySign);
                        final PayAsyncTask payAsyncTask = new PayAsyncTask(sActivity,sIPayResultListener);
                        payAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,paySign);
                    }
                })
                .build()
                .post();
    }

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
                        sIPayResultListener.onPaySuccess();
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
     * 支付宝支付业务示例
     */
    public static void payV2() {

        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
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
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);

        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);



        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(sActivity);
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

    public static void payV2(Map<String,String > map) {

        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
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
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);

        String orderParam = OrderInfoUtil2_0.buildOrderParam(map);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(map, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(sActivity);
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
}
