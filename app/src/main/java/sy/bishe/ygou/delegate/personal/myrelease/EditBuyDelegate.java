package sy.bishe.ygou.delegate.personal.myrelease;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
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

public class EditBuyDelegate extends YGouDelegate {


    private ReleaseBean releaseBean = new ReleaseBean();

    private static final String ARG_BUY_ID = "ARG_BUY_ID";
    private int sId = -1;

    public static YGouDelegate create(int id) {
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_BUY_ID,id);
        final EditBuyDelegate detailDelegate = new EditBuyDelegate();
        detailDelegate.setArguments(bundle);
        return detailDelegate;
    }


    @BindView(R2.id.edit_buy_type)
    AppCompatSpinner spi_type;

    @BindView(R2.id.edit_buy_title)
    AppCompatEditText edit_title;

    @BindView(R2.id.edit_buy_desc)
    AppCompatEditText edit_desc;

    @BindView(R2.id.edit_buy_count)
    AppCompatEditText edit_count;

    @BindView(R2.id.edit_buy_price)
    AppCompatEditText edit_price;

    @BindView(R2.id.edit_buy_contact)
    AppCompatEditText edit_contact;
    @OnClick(R2.id.edit_buy_back)
    void back(){
        getFragmentManager().popBackStack();
    }

    @OnClick(R2.id.edit_buy_submit)
    void sub(){
        if (checkInfomation()){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
            //获取当前时间
            Date date = new Date(System.currentTimeMillis());
            String format = simpleDateFormat.format(date);
            releaseBean.setRelease_time(format);
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(releaseBean);
            RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());

            RestClient.builder()
                    .setUrl("release/update")
                    .setBody(requestBody)
                    .onSuccess(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            JSONObject result = JSONObject.parseObject(response);
                            Log.i("response", "onSuccess: "+response);
                            String status = result.getString("status");
                            {
                                if (status!=null&&status.equals("ok")){
                                    Toast.makeText(getContext(),"修改成功",Toast.LENGTH_SHORT).show();
                                     getSupportDelegate().startWithPop(new MyReleaseDelegate());
                                    //getSupportDelegate().start(new SuccessBuyDelegate());
                                }else {
                                    Toast.makeText(getContext(),"修改失败,请稍后再试",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    })
                    .build()
                    .post();
        }
        }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        if (bundle != null){
            sId = bundle.getInt(ARG_BUY_ID);
            Log.i("sGoodsId", sId+"");
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_buy_edit;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {


    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {

        RestClient.builder()
                .setUrl("release/queryById/"+sId)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response);

                        String status = jsonObject.getString("status");
                        if (!status.isEmpty()&&status.equals("ok")){

                            JSONObject data = jsonObject.getJSONObject("data");
                            String release_title = data.getString("release_title");
                            String release_desc = data.getString("release_desc");
                            String release_price = data.getString("release_price");
                            String release_count = data.getString("release_count");
                            String release_contact = data.getString("release_contact");
                            String id = data.getString("id");
                            String user_id = data.getString("user_id");
                            String user_img = data.getString("release_user_img");

                            releaseBean.setId(Integer.parseInt(id));
                            releaseBean.setUser_id(Integer.parseInt(user_id));
                            releaseBean.setRelease_img(user_img);
                            edit_title.setText(release_title);
                            edit_desc.setText(release_desc);
                            edit_price.setText(release_price);
                            edit_count.setText(release_count);
                            edit_contact.setText(release_contact);
                        }
                    }
                })
                .build()
                .get();
    }


    private boolean checkInfomation(){
        boolean isOk = true;
        String title = edit_title.getText().toString();
        String desc = edit_desc.getText().toString();
        String type = spi_type.getSelectedItem().toString();
        String price = edit_price.getText().toString();
        String count = edit_count.getText().toString();
        String contact = edit_contact.getText().toString();
        if (title.isEmpty()||title.length()>10){
            isOk = false;
            edit_title.setError("标题不能为空并且小于10个字符");
        }else{
            releaseBean.setRelease_title(title);
            edit_title.setError(null);

        }
        if (desc.isEmpty()||desc.length()>30){
            isOk = false;
            edit_desc.setError("描述不能为空并且小于30个字符");
        }else {
            edit_desc.setError(null);
            releaseBean.setRelease_desc(desc);
        }
        if (type.equals("请选择")){
            isOk = false;
            Toast.makeText(getContext(),"选择一个类型",Toast.LENGTH_SHORT).show();
        }else {
            releaseBean.setRelease_type(type);
        }
        if (edit_price.getText().toString().isEmpty()||price.contains("0.")){
            isOk = false;
            edit_price.setError("价格不能为空并且不小于0");
        }else {
            edit_desc.setError(null);
            releaseBean.setRelease_price(Double.valueOf(price));
        }
        if (edit_count.getText().toString().isEmpty()||count.equals("0")){
            isOk = false;
            edit_count.setError("数量不能为空并且不小于1");
        }else {
            edit_count.setError(null);
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
