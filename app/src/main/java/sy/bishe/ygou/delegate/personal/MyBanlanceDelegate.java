package sy.bishe.ygou.delegate.personal;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import butterknife.BindView;
import butterknife.OnClick;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.utils.storage.YGouPreferences;

//余额
public class MyBanlanceDelegate extends YGouDelegate {

    @BindView(R2.id.balance_money)
    AppCompatTextView tv_money = null;

    @OnClick(R2.id.ll_balance_recharge)
    void recharge(){
        getSupportDelegate().start(new RechargeDelegate());
    }

    @OnClick(R2.id.balance_back)
    void back(){
        getFragmentManager().popBackStack();
    }

    /**
     * 、设置
     */
    @OnClick(R2.id.balance_setting)
    void setting(){
        getSupportDelegate().start(new BanlanceSettingDelegate());
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_mybalance;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        String userBalance = YGouPreferences.getCustomAppProfile("userBalance");
        tv_money.setText(userBalance);
    }
}
