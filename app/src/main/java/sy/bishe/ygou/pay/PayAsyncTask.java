package sy.bishe.ygou.pay;

import android.app.Activity;
import android.os.AsyncTask;

import com.alipay.sdk.app.PayTask;

import sy.bishe.ygou.alipay.PayResult;
import sy.bishe.ygou.ui.loader.YGouLoader;
import sy.bishe.ygou.utils.logger.YGouLogger;

public class PayAsyncTask extends AsyncTask<String,Void,String> {
    private final Activity ACTIVITY;
    private final IPayResultListener LISTENER;
    //返回结果参数
    private static final String PAY_STATUS_SUCCESS = "9000";
    private static final String PAY_STATUS_PAYING = "8000";
    private static final String PAY_STATUS_FAIL = "4000";
    private static final String PAY_STATUS_CANCEL = "6001";
    private static final String PAY_STATUS_ERROR = "6002";

    public PayAsyncTask(Activity activity, IPayResultListener listener) {
        ACTIVITY = activity;
        LISTENER = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        final String aliPaySign = strings[0];
        final PayTask payTask = new PayTask(ACTIVITY);
        return payTask.pay(aliPaySign,true);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
                 YGouLoader.stopLoading();
                 final PayResult payResult = new PayResult(result);
                // 支付宝返回此次支付结构及加签，建议对支付宝签名信息拿签约是支付宝提供的公钥做验签
                 final String resultInfo = payResult.getResult();
                 final String resultStatus = payResult.getResultStatus();
                 YGouLogger.d("AL_PAY_RESULT", resultInfo);
                 YGouLogger.d("AL_PAY_RESULT", resultStatus);
                 switch (resultStatus) {
                         case PAY_STATUS_SUCCESS:
                                 if (LISTENER != null) {
                                         LISTENER.onPaySuccess();
                                     }
                                break;
                         case PAY_STATUS_FAIL:
                                 if (LISTENER != null) {
                                         LISTENER.onPayFail();
                                     }
                                 break;
                         case PAY_STATUS_PAYING:
                                 if (LISTENER != null) {
                                         LISTENER.onPaying();
                                     }
                                 break;
                         case PAY_STATUS_CANCEL:
                                 if (LISTENER != null) {
                                         LISTENER.onPayCancle();
                                    }
                                 break;
                         case PAY_STATUS_ERROR:
                                 if (LISTENER != null) {
                                         LISTENER.onPayContentError();
                                     }
                                 break;
                         default:
                                 break;
                     }
             }
    }
