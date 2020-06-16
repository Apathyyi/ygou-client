package sy.bishe.ygou.delegate.find;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;

import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.bean.ReleaseBean;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class BuyDelegate extends YGouDelegate {

    //求购bean
    private ReleaseBean releaseBean = new ReleaseBean();
    //弹窗
    ProgressDialog progressDialog ;

    @BindView(R.id.buy_title)
    AppCompatEditText edt_title = null;

    @BindView(R.id.buy_desc)
    AppCompatEditText edt_desc = null;
    @BindView(R.id.buy_type)
    AppCompatSpinner spi_type = null;

    @BindView(R.id.buy_price)
    AppCompatEditText edt_price = null;

    @BindView(R.id.buy_count)
    AppCompatEditText edt_count = null;

    @BindView(R2.id.buy_contact)
    AppCompatEditText edit_contact;
    @BindView(R2.id.buy_wait)
    LinearLayout ll_buy_wait = null;
    /**
     * 发布
     */
    @OnClick(R.id.buy_submit)
    void onClickCheck(){
        if (checkInfomation()){
            showProgressDialog("提示","正在上传请稍等");
            ll_buy_wait.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(),"信息核实",Toast.LENGTH_SHORT).show();


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
            //获取当前时间
            Date date = new Date(System.currentTimeMillis());
            String format = simpleDateFormat.format(date);
            releaseBean.setRelease_time(format);
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(releaseBean);
            RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());

            RestClient.builder()
                    .setUrl("release/add")
                    .setBody(requestBody)
                    .onSuccess(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            JSONObject result = JSONObject.parseObject(response);
                            Log.i("response", "onSuccess: "+response);
                            String status = result.getString("status");
                            {
                                if (status.equals("ok")){
                                    Toast.makeText(getContext(),"发布成功",Toast.LENGTH_SHORT).show();
                                    ll_buy_wait.setVisibility(View.GONE);
                                    hideProgressDialog();
                                    getFragmentManager().popBackStack();
                                    //getSupportDelegate().start(new SuccessBuyDelegate());
                                }else {
                                    Toast.makeText(getContext(),"发布失败,请稍后再试",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    })
                    .build()
                    .post();
            //1 保存数据
            //2 刷新求购页面
            //3 刷新我的求购
        }
    }

    /**
     * 展示弹窗
     * @param title
     * @param message
     */

    public void showProgressDialog(String title, String message) {
        if (progressDialog == null) {

            progressDialog = ProgressDialog.show(getContext(), title,
                    message, true, false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
        }

        progressDialog.show();

    }

    /*
     * 隐藏提示加载
     */
    public void hideProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_buy;
    }
    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private boolean checkInfomation(){
        boolean isOk = true;
        String title = edt_title.getText().toString();
        String desc = edt_desc.getText().toString();
        String type = spi_type.getSelectedItem().toString();
        String price = edt_price.getText().toString();
        String count = edt_count.getText().toString();
        String contact = edit_contact.getText().toString();
        if (title.isEmpty()||title.length()>10){
            isOk = false;
            edt_title.setError("标题不能为空并且小于10个字符");
        }else{
            releaseBean.setRelease_title(title);
            edt_title.setError(null);

        }
        if (desc.isEmpty()||desc.length()>30){
            isOk = false;
            edt_desc.setError("描述不能为空并且小于30个字符");
        }else {
            edt_desc.setError(null);
            releaseBean.setRelease_desc(desc);
        }
        if (type.equals("请选择")){
            isOk = false;
            Toast.makeText(getContext(),"选择一个类型",Toast.LENGTH_SHORT).show();
        }else {
            releaseBean.setRelease_type(type);
        }
        if (edt_price.getText().toString().isEmpty()||price.contains("0.")){
            isOk = false;
            edt_price.setError("价格不能为空并且不小于0");
        }else {
            edt_price.setError(null);
            releaseBean.setRelease_price(Double.valueOf(price));
        }
        if (edt_count.getText().toString().isEmpty()||count.equals("0")){
            isOk = false;
            edt_count.setError("数量不能为空并且不小于1");
        }else {
            edt_count.setError(null);
            releaseBean.setRelease_count(Integer.parseInt(count));
        }
        if (contact.isEmpty()||contact.equals(" ")||!Patterns.PHONE.matcher(contact).matches()){
            isOk = false;
            edit_contact.setError("请正确填写联系方式");
        }else {
            edit_contact.setError(null);
            releaseBean.setRelease_contact(contact);
        }
        releaseBean.setUser_id(Integer.parseInt(YGouPreferences.getCustomAppProfile("userId")));
        return isOk;
    }
}
