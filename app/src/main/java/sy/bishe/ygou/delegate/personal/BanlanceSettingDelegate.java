package sy.bishe.ygou.delegate.personal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.alibaba.fastjson.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class BanlanceSettingDelegate extends YGouDelegate {

    @BindView(R2.id.balance_set_check)
    AppCompatEditText edit_check = null;

    @BindView(R2.id.balance_set_recheck)
    AppCompatEditText edit_recheck = null;


    @OnClick(R2.id.balance_set_back)
    void back(){
        getFragmentManager().popBackStack();
    }

    /**
     * 提交
     */
    @OnClick(R2.id.ll_balance_set_sub)
    void sub(){
        Log.i("提交", "sub: ");
        if (!edit_check.getText().toString().isEmpty()&&edit_check.getText().toString().length()==6){
            String check = edit_check.getText().toString();
            if (!check.equals(edit_recheck.getText().toString())){

                edit_check.setError("两次输入密码不一致");
            }else {
                edit_check.setError(null);
                Log.i("保存密码", "sub: ");
                //保存是否设置过密码
                YGouPreferences.addCustomAppProfile("isSetBalance",edit_check.getText().toString());
                //保存数据库
                RequestBody id = RequestBody.create(YGouApp.JSON, JSONObject.toJSONString(YGouPreferences.getCustomAppProfile("userId")));
                RestClient.builder()
                        .setUrl("user/updatePayNum/"+edit_check.getText().toString())
                        .setBody(id)
                        .onSuccess(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                Log.i("yes", "onSuccess: "+response);
                                String status = JSONObject.parseObject(response).getString("status");
                                if (status.equals("ok")){
                                    Toast.makeText(getContext(),"设置成功",Toast.LENGTH_SHORT).show();
                                    getFragmentManager().popBackStack();
                                }
                            }
                        })
                        .build()
                        .post();
            }
        }else {
            edit_check.setError("请输入六位密码");
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.balance_setting;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {

    }
}
