package sy.bishe.ygou.delegate.personal.userInfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.bean.AddressBean;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class AdressDelegate extends YGouDelegate implements  CompoundButton.OnCheckedChangeListener {

    /**
     * 地址
     */
    private AdressAdapter adressAdapter = null;
    List<String> citys = new ArrayList<>();

    List<List<String>> provinces = new ArrayList<>();
    private Dialog dialog =  null;

    private AddressBean addressBean = new AddressBean();

    @BindView(R2.id.rv_address)
    RecyclerView sRecycleView = null;

    /**
     * 返回
     */
    @OnClick(R2.id.icon_address_back)
    void onClickback(){
        getFragmentManager().popBackStack();
    }
    @OnClick(R2.id.icon_address_add)
    void onClickAdd(){
        dialog.show();
        Window window = dialog.getWindow();
        if (window!=null){
            window.setContentView(R.layout.dailog_add_address);
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(params);
            AppCompatTextView tv_save = window.findViewById(R.id.edit_address_save);
            TextInputEditText edit_name  = window.findViewById(R.id.edit_address_name);
            TextInputEditText edit_phone =  window.findViewById(R.id.edit_address_phone);
            TextInputEditText edit_address_area = window.findViewById(R.id.edit_address_area);
            TextInputEditText edit_content = window.findViewById(R.id.edit_address_content);
            CheckBox checkBox = window.findViewById(R.id.edit_address_default);
            checkBox.setOnCheckedChangeListener(this::onCheckedChanged);
            tv_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isok = true;
                    if (edit_name.getText().toString().isEmpty()||edit_name.getText().toString().equals("")){
                        isok=false;
                        edit_name.setError("收件人不为空");
                    }else {
                        edit_name.setError(null);
                        addressBean.setAddress_name(edit_name.getText().toString());
                    }
                    if (edit_phone.getText().toString().isEmpty()||edit_phone.getText().toString().equals("")){
                        edit_phone.setError("收件人手机号不为空");
                        isok=false;
                    }else {
                        edit_phone.setError(null);
                        addressBean.setAddress_phone(edit_phone.getText().toString());
                    }
                    if (edit_address_area.getText().toString().isEmpty()||edit_address_area.getText().toString().equals("")){
                    }else {
                        addressBean.setAddress_area(edit_address_area.getText().toString());
                    }
                    if (edit_content.getText().toString().isEmpty()||edit_content.getText().toString().equals("")){
                        isok=false;
                        edit_content.setError("详细地址不能为空");
                    }else {
                        edit_content.setError(null);
                        addressBean.setAddress_content(edit_content.getText().toString());
                    }
                    if (isok){
                        Log.i("OK", "onClick: ");
                        addressBean.setAddress_user_id(Integer.parseInt(YGouPreferences.getCustomAppProfile("userId")));
                        add(addressBean);
                    }
                }

            });
        }

    }

    /**
     * 添加收貨地址
     * @param addressBean
     */
    private void add(AddressBean addressBean) {
        JSONObject jsonObject = (JSONObject) JSON.toJSON(addressBean);
        RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());
        RestClient.builder()
                .setUrl("address/add")
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("response", "onSuccess: "+response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("ok")){
                            RestClient.builder()
                                    .setUrl("address/query/"+ YGouPreferences.getCustomAppProfile("userId"))
                                    .loader(getContext())
                                    .onSuccess(new ISuccess() {
                                        @Override
                                        public void onSuccess(String response) {
                                            final List<MultipleitemEntity> list = new AdressDataConverter().setsJsonData(response).convert();
                                            adressAdapter = new AdressAdapter(list);
                                            sRecycleView.setAdapter(adressAdapter);
                                        }
                                    })
                                    .build()
                                    .get();
                            dialog.dismiss();
                        }
                    }
                })
                .build()
                .post();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new AlertDialog.Builder(getSupportDelegate().getActivity()).create() ;
    }

    @Nullable


    @Override
    public Object setLayout() {
        return R.layout.delegate_adress;
    }
    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        sRecycleView.setLayoutManager(manager);
        RestClient.builder()
                .setUrl("address/query/"+ YGouPreferences.getCustomAppProfile("userId"))
                .loader(getContext())
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("ADRESS", response);
                        final List<MultipleitemEntity> list = new AdressDataConverter().setsJsonData(response).convert();
                        adressAdapter = new AdressAdapter(list);
                        sRecycleView.setAdapter(adressAdapter);
                    }
                })
                .build()
                .get();

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        addressBean.setAddress_default("1");
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
                            citys.add(next);
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

    /**
     * 是否默認地址
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.i("isChecked", "onCheckedChanged: "+isChecked);
        if (isChecked){
            addressBean.setAddress_default("1");
        }else {
            addressBean.setAddress_default("0");
        }
    }
}
