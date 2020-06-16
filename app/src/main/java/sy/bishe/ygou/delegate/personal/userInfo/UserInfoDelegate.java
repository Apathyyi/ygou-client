package sy.bishe.ygou.delegate.personal.userInfo;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.othershe.library.NiceImageView;

import butterknife.BindView;
import butterknife.OnClick;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.bean.UserBean;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.utils.storage.YGouPreferences;


public class UserInfoDelegate extends YGouDelegate {

    private UserBean userBean;
    private boolean hide = true;

    @BindView(R2.id.tv_arrow_nameval)
    AppCompatTextView viewName = null;

    @BindView(R2.id.tv_arrow_adressval)
    AppCompatTextView viewAdress = null;

    @BindView(R2.id.tv_arrow_genderval)
    AppCompatTextView viewGender = null;

    @BindView(R2.id.tv_arrow_birthval)
    AppCompatTextView viewBirth = null;

    @BindView(R2.id.tv_arrow_emailval)
    AppCompatTextView viewemail = null;

    @BindView(R2.id.tv_arrow_phoneval)
    AppCompatTextView viewphone = null;

    @BindView(R2.id.tv_arrow_ageval)
    AppCompatTextView viewage = null;

    @BindView(R2.id.tv_arrow_user_isidentificated)
    AppCompatTextView viewidentify = null;

    @BindView(R2.id.tv_arrow_user_isidentificatedval)
    AppCompatTextView viewidentifyval = null;

    @BindView(R2.id.tv_arrow_schoolval)
    AppCompatTextView viewschool = null;
    @BindView(R2.id.rl_school)
    RelativeLayout rl_school = null;

    @BindView(R2.id.tv_arrow_collegeval)
    AppCompatTextView view_college = null;
    @BindView(R2.id.tv_arrow_specialtyval)
    AppCompatTextView view_specialty = null;
    @BindView(R2.id.tv_arrow_gradeval)
    AppCompatTextView view_grade = null;

    @BindView(R2.id.ll_identify)
    LinearLayoutCompat ll_identify = null;

    @BindView(R2.id.tv_arrow_signature)
    AppCompatTextView tv_arrow_signature = null;

    @BindView(R2.id.img_arrow_avatar)
    NiceImageView img = null;
    @OnClick(R2.id.icon_info_back)
    void onclickBack(){
        getFragmentManager().popBackStack();
    }
    /**
     * 收货地址编辑
     */
    @OnClick(R2.id.icon_arrown)
    void onClickReciveAdress(){
       getSupportDelegate().start(new AdressDelegate());
    }


    @OnClick(R2.id.icon_arrow_img_text)
    void onClickEdit(){
        getSupportDelegate().start(EditInfoDelegate.create(userBean));
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_user_info;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        RestClient.builder()
                .setUrl("user/queryById/"+ YGouPreferences.getCustomAppProfile("userId"))
                .loader(getContext())
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response).getJSONObject("data");
                        JSONObject user = JSONObject.parseObject(response).getJSONObject("jsonObject");
                        if (user!=null&&!user.isEmpty()){
                            String user_img = user.getString("user_img");

                        }
                        if (jsonObject != null && !jsonObject.isEmpty()) {
                            userBean = jsonObject.toJavaObject(UserBean.class);
                            String thumb = jsonObject.getString("user_img");
                            String name = jsonObject.getString("user_name");
                            String adress = jsonObject.getString("user_address");
                            String gender = jsonObject.getString("user_gender");
                            String birth = jsonObject.getString("user_birth");
                            String email = jsonObject.getString("user_email");
                            String phone = jsonObject.getString("user_phone");
                            String age = jsonObject.getString("user_age");
                            boolean isidentificated = jsonObject.getBoolean("user_isidentificated");
                            String school = jsonObject.getString("user_school");
                            String college = jsonObject.getString("user_college");
                            String specialty = jsonObject.getString("user_stuspeciality");
                            String grade = jsonObject.getString("user_grade");
                            String signature = jsonObject.getString("user_signature");

                            Glide.with(getContext())
                                    .load(thumb)
                                    .into(img);
                            viewName.setText(name);
                            viewAdress.setText(adress);
                            viewGender.setText(gender);
                            viewBirth.setText(birth);
                            viewemail.setText(email);
                            viewphone.setText(phone);
                            viewage.setText(age);
                            viewschool.setText(school);
                            view_college.setText(college);
                            view_specialty.setText(specialty);
                            view_grade.setText(grade);
                            tv_arrow_signature.setText(signature);
                            if (isidentificated) {
                                viewidentify.setText("已认证");
                                viewidentifyval.setText("查看认证信息");
                                viewidentifyval.setTextColor(getContext().getColor(R.color.color_shape_start));
                                viewidentifyval.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (hide) {
                                            ll_identify.setVisibility(View.VISIBLE);
                                            hide = false;
                                            viewidentifyval.setText("收起");
                                        } else {
                                            hide = true;
                                            ll_identify.setVisibility(View.GONE);
                                            viewidentifyval.setText("查看认证信息");
                                        }
                                    }
                                });
                            } else {
                                viewidentify.setText("未认证");
                                viewidentifyval.setText("去认证");
                                viewidentifyval.setTextColor(getContext().getColor(R.color.color_shape_start));
                                viewidentifyval.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getSupportDelegate().start(EditInfoDelegate.create(userBean));
                                    }
                                });
                            }
                        }
                    }

                })
                .build()
                .get();
    }
}
