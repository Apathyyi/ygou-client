package sy.bishe.ygou.pay;

public interface IPayResultListener {
    void onPaySuccess();
    void onPaying();
    void onPayFail();
    void onPayCancle();
    void onPayContentError();
}
