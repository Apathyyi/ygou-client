package sy.bishe.ygou.delegate.personal.userInfo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.android.material.textfield.TextInputEditText;
import com.joanzapata.iconify.widget.IconTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.bean.UserBean;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.utils.storage.YGouPreferences;
import sy.bishe.ygou.utils.timer.AgeUtils;
//修改
public class EditInfoDelegate extends YGouDelegate {

    //時區 中國
    private Calendar calendar= Calendar.getInstance(Locale.CHINA);

    private UserBean userBean;

    private static final String ARG_USER = "ARG_USER";

    public static EditInfoDelegate create(UserBean userBean){
        final Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_USER,userBean);
        final EditInfoDelegate editInfoDelegate = new EditInfoDelegate();
        editInfoDelegate.setArguments(bundle);
        return editInfoDelegate;
    }
    //城市
    List<String> cities = new ArrayList<>();
    List<List<String>> provinces = new ArrayList<>();

    @BindView(R2.id.et_edit_name)
    TextInputEditText edit_name = null;

    @BindView(R2.id.icon_edit_man)
    IconTextView icon_man = null;

    @BindView(R2.id.icon_edit_woman)
    IconTextView icon_woman = null;

    @BindView(R2.id.rl_edit_birth)
    RelativeLayout rl_birth = null;

    @BindView(R2.id.et_edit_birth)
    AppCompatTextView tv_birth = null;

    @BindView(R2.id.tv_edit_age)
    AppCompatTextView tv_age = null;

    @BindView(R2.id.et_edit_mail)
    TextInputEditText edit_mail = null;

    @BindView(R2.id.et_edit_phone)
    TextInputEditText edit_phone = null;

    @BindView(R2.id.rl_edit_address)
    RelativeLayout rl_address = null;

    @BindView(R2.id.tv_edit_address)
    AppCompatTextView tv_address = null;

    @BindView(R2.id.icon_edit_identify)
    IconTextView icon_identify = null;

    @BindView(R2.id.et_edit_school)
    TextInputEditText edit_school = null;

    @BindView(R2.id.et_edit_college)
    TextInputEditText edit_college = null;

    @BindView(R2.id.et_edit_specialty)
    TextInputEditText edit_specialty = null;

    @BindView(R2.id.et_edit_signature)
    TextInputEditText edit_signature = null;

    @BindView(R2.id.et_edit_grade)
    TextInputEditText edit_grade = null;

    /**
     * 返回
     */
    @OnClick(R2.id.tv_edit_back)
    void onClickBack(){
       getFragmentManager().popBackStack();
    }
    /**
     * 地址选择
     */
    @OnClick(R2.id.rl_edit_address)
    void ocClickaddress(){
        OptionsPickerView pvOptions = new OptionsPickerBuilder(_mActivity, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                String address = cities.get(options1)+"-"
                        + provinces.get(options1).get(option2);
                Log.d("tx", "onOptionsSelect: "+address);
                tv_address.setText(address);
            }
        })
                .setTitleText("选择地址")
                .setCancelText("取消")
                .setSubmitText("确定")
                .build();
        pvOptions.setPicker(cities, provinces);
        pvOptions.show();

    }
    //性別
    @OnClick(R2.id.icon_edit_man)
    void onClickman(){
        icon_man.setTextColor(getContext().getColor(R.color.app_main));
        icon_woman.setTextColor(getContext().getColor(R.color.text_bottom_comment));
        userBean.setUser_gender("男");
    }
    @OnClick(R2.id.icon_edit_woman)
    void onClickwoman(){
        icon_woman.setTextColor(getContext().getColor(R.color.app_main));
        icon_man.setTextColor(getContext().getColor(R.color.text_bottom_comment));
        userBean.setUser_gender("女");
    }

    /**
     * 保存
     */
    @OnClick(R2.id.tv_edit_save)
    void onCLickSave(){
        Log.i("", "onCLickSave: ");
        if (checkInfomation()){
            JSONObject jsonObject = (JSONObject) JSON.toJSON(userBean);
            RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());
            RestClient.builder()
                    .setUrl("user/update/"+ YGouPreferences.getCustomAppProfile("userId"))
                    .setBody(requestBody)
                    .onSuccess(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            Log.i("response", "onSuccess: "+response);
                            JSONObject result = JSONObject.parseObject(response);
                            String status = result.getString("status");
                            if (status.equals("ok")){
                                Toast.makeText(getContext(),"修改成功",Toast.LENGTH_SHORT).show();
                                getFragmentManager().popBackStack();
                            }
                        }
                    })
                    .build()
                    .post();
        }
    }

    /**
     * 何時信息
     * @return
     */
    private boolean checkInfomation() {
        String name = edit_name.getText().toString();
        String mail = edit_mail.getText().toString();
        String phone = edit_phone.getText().toString();
        String address = tv_address.getText().toString();
        String school = edit_school.getText().toString();
        String college = edit_college.getText().toString();
        String specailty = edit_specialty.getText().toString();
        String grade = edit_grade.getText().toString();
        String signature = edit_signature.getText().toString();
        boolean isok = true;
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            edit_mail.setError("邮箱格式错误");
            isok=false;
        }else {
            edit_mail.setError(null);
            userBean.setUser_email(mail);
        }
        if (!Patterns.PHONE.matcher(phone).matches()){
            edit_phone.setError("电话格式错误");
            isok = false;
        }else {
            edit_phone.setError(null);
            userBean.setUser_phone(phone);
        }
        if (name.isEmpty()||name.equals("")){
            edit_name.setError("名字不为空");
            isok = false;
        }else {
            edit_name.setError(null);
            userBean.setUser_name(name);
        }
        if (userBean.getUser_isidentificated().equals("1")) {
            //验证学校信息
            if (school.isEmpty()||school.equals("")||specailty.isEmpty()||specailty.equals("")||college.isEmpty()||college.equals("")||grade.isEmpty()||grade.equals("")){
                edit_school.setError("请填写完认证信息");
                isok = false;
            }else {
                edit_school.setError(null);
                userBean.setUser_school(school);
                userBean.setUser_stuspeciality(specailty);
                userBean.setUser_college(college);
                userBean.setUser_grade(grade);
            }
        }else if (!school.isEmpty()&&!specailty.isEmpty()&&!college.isEmpty()&&!grade.isEmpty()){
                userBean.setUser_stuspeciality("1");
                edit_school.setError(null);
                userBean.setUser_school(school);
                userBean.setUser_stuspeciality(specailty);
                userBean.setUser_college(college);
                userBean.setUser_grade(grade);
        }
        userBean.setUser_address(address);
        userBean.setUser_signature(signature);
        return isok;
    }

    @Override
    public Object setLayout() {
        return R.layout.editinfo_delegate;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams. SOFT_INPUT_ADJUST_PAN);
        initData();
        rl_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(_mActivity,0,tv_birth,calendar);
            }
        });
    }

    /**
     * 初始化信息
     */
    private void initData() {
        edit_name.setText(userBean.getUser_name());
        if (userBean.getUser_gender().equals("男")){
            icon_man.setTextColor(getContext().getColor(R.color.app_main));
        }else {
            icon_woman.setTextColor(getContext().getColor(R.color.app_main));
        }
        tv_birth.setText(userBean.getUser_birth());
        tv_age.setText(userBean.getUser_age());
        edit_mail.setText(userBean.getUser_email());
        edit_phone.setText(userBean.getUser_phone());
        tv_address.setText(userBean.getUser_address());

        if (userBean.getUser_isidentificated().equals("0")){
            icon_identify.setTextColor(getContext().getColor(R.color.dot_hong));
        }else {
            icon_identify.setTextColor(getContext().getColor(R.color.holo_green_light));
        }
        edit_school.setText(userBean.getUser_school());
        edit_college.setText(userBean.getUser_college());
        edit_specialty.setText(userBean.getUser_stuspeciality());
        edit_signature.setText(userBean.getUser_signature());
        edit_grade.setText(userBean.getUser_grade());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        if (bundle != null){
            userBean = (UserBean) bundle.getSerializable(ARG_USER);
            Log.i("userBean", "onCreate: "+userBean.toString());
        }
    }

    /**
     * 彈窗  選擇地址
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public  void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                monthOfYear = monthOfYear + 1;
                String month,day;
                if (monthOfYear<10){
                     month = "0"+monthOfYear;
                }
                else {
                    month = monthOfYear+"";
                }
                if (dayOfMonth<10){
                     day = "0"+dayOfMonth;
                }else {
                    day = dayOfMonth+"";
                }
                String birth = year+ "-" + month + "-" + day;
                tv.setText(birth);
                userBean.setUser_birth(birth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = simpleDateFormat.parse(birth);
                    int age = AgeUtils.getAgeByBirth(date);
                    Log.i("age", "onDateSet: "+age);
                    tv_age.setText(String.valueOf(age));
                    userBean.setUser_age(String.valueOf(age));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RestClient.builder()
                .setUrl("city/query")
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response).getJSONObject("jsonObject");
                        Set<String> strings = jsonObject.keySet();
                        Iterator<String> iterator = strings.iterator();
                        while (iterator.hasNext()){
                            String next = iterator.next();
                            Log.d("next", next);
                            cities.add(next);
                            JSONArray jsonArray = jsonObject.getJSONArray(next);
                            int size = jsonArray.size();
                            List<String> provice = new ArrayList<>();
                            for (int i = 0; i < size; i++) {
                                String name = jsonArray.getString(i);
                                Log.d("name", name);
                                provice.add(name);
                            }
                            provinces.add(provice);
                        }
                    }
                })
                .build()
                .get();
    }
}
