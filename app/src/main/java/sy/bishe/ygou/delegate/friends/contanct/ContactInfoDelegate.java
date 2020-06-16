package sy.bishe.ygou.delegate.friends.contanct;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.joanzapata.iconify.widget.IconTextView;

import butterknife.BindView;
import butterknife.OnClick;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.friends.chat.ChatDetailDelegate;
import sy.bishe.ygou.net.callback.IFailure;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;

public class ContactInfoDelegate extends YGouDelegate {

    private static final String ARG_FRIEND_NAME = "ARG_FRIEND_NAME";//名字
    private String sName ;

    public static ContactInfoDelegate create(String name) {
        final Bundle bundle = new Bundle();
        bundle.putString(ARG_FRIEND_NAME,name);
        final ContactInfoDelegate detailDelegate = new ContactInfoDelegate();
        detailDelegate.setArguments(bundle);
        return detailDelegate;
    }
    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate()
            .override(100,100);

    @BindView(R2.id.contact_name)
    AppCompatTextView tv_name = null;

    @BindView(R2.id.contact_sex)
    IconTextView icon_sex = null;

    @BindView(R2.id.contact_age)
    AppCompatTextView tv_age= null;

    @BindView(R2.id.contact_count)
    AppCompatTextView tv_count = null;

    @BindView(R2.id.contact_Nickname)
    AppCompatTextView tv_nickName = null;

    @BindView(R2.id.contact_country)
    AppCompatTextView tv_country= null;

    @BindView(R2.id.contact_notename)
    AppCompatTextView tv_noteName= null;

    @BindView(R2.id.contact_birth)
    AppCompatTextView tv_birth= null;

    @BindView(R2.id.contact_job)
    AppCompatTextView tv_job= null;

    @BindView(R2.id.contact_email)
    AppCompatTextView tv_email= null;

    @BindView(R2.id.contact_adress)
    AppCompatTextView tv_address = null;

    @BindView(R2.id.contact_phone)
    AppCompatTextView tv_phone = null;

    @BindView(R2.id.rl_contact_background)
    RelativeLayout rl_background = null;
    @OnClick(R2.id.contact_back)
    void onClickback(){
        getFragmentManager().popBackStack();
    }
    /**
     * 打电话
     */
    @OnClick(R2.id.contact_btn_phone)
    void OnclickPhone(){

    }
    /**
     * 发消息
     */
    @OnClick(R2.id.contact_btn_send)
    void onClickSend(){
          getSupportDelegate().start(ChatDetailDelegate.create(sName));
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_contact_info;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        if (bundle != null){
            sName = bundle.getString(ARG_FRIEND_NAME);
            Log.i("sFriendId", sName+"");
        }
    }
    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RestClient.builder()
                .setUrl("user/queryByName/"+sName)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("", "onSuccess: "+response);
                        JSONObject object = JSONObject.parseObject(response);
                        Log.i("object", "onSuccess: "+object.toJSONString());
                        if (object.getString("status").equals("ok")){
                            JSONObject jsonObject = object.getJSONObject("data");
                            String name = jsonObject.getString("user_name");
                            Log.i("user_name", "onSuccess: "+name);
                            String user_email = jsonObject.getString("user_email");
                            String user_phone = jsonObject.getString("user_phone");
                            String user_address = jsonObject.getString("user_address");
                            String user_age = jsonObject.getString("user_age");
                            String user_birth = jsonObject.getString("user_birth");
                            String user_gender = jsonObject.getString("user_gender");
                            String user_word = jsonObject.getString("user_word");
                            //String backgroundUrl = jsonObject.getString("user_background");
                            tv_name.setText(user_word);
                            tv_nickName.setText(name);
                            tv_age.setText(user_age);
                            tv_birth.setText(user_birth);
                            tv_email.setText(user_email);
                            tv_phone.setText(user_phone);
                            tv_address.setText(user_address);
                            if (user_gender==null||user_gender.isEmpty()){
                                icon_sex.setText("{fa-female}");
                            }else {
                                if (user_gender.equals("男")){
                                    icon_sex.setText("{fa-male}");
                                }else {
                                    icon_sex.setText("{fa-female}");
                                }
                            }
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Log.i("onFailure", "onFailure: ");
                    }
                })
                .build()
                .get();
    }
}
