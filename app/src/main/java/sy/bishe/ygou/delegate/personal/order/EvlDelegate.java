package sy.bishe.ygou.delegate.personal.order;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.bean.EvaluateBean;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.personal.PersonalDelegate;
import sy.bishe.ygou.net.callback.IFailure;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.utils.storage.YGouPreferences;

import static android.content.Context.LOCATION_SERVICE;

public class EvlDelegate extends YGouDelegate implements TextWatcher {

    private String orderId ;

     private EvaluateBean evaluateBean = new EvaluateBean();
    @BindView(R2.id.evl_goods_img)
    AppCompatImageView goods_img = null;

    @BindView(R2.id.evl_goods_name)
    AppCompatTextView goods_name = null;

    @BindView(R2.id.evl_goods_price)
    AppCompatTextView goods_price = null;

    @BindView(R2.id.evl_content)
    AppCompatEditText evl_content = null;

    @BindView(R2.id.evl_address)
    AppCompatTextView evl_address = null;

    //返回
    @OnClick(R2.id.evl_back)
    void back(){
        getFragmentManager().popBackStack();
    }

    @BindView(R2.id.btn_sub)
    AppCompatButton btn_sub =null;

    @BindView(R2.id.ll_evl_sub)
    LinearLayout ll_evl_sub = null;
    //发表
    @OnClick(R2.id.btn_sub)
    void sub(){
        Log.i("进行评价", "sub: ");
        if (evl_content.getText()!=null&&!evl_content.getText().toString().equals("")){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
            Date date = new Date(System.currentTimeMillis());
            String format = simpleDateFormat.format(date);
            evaluateBean.setEvl_time(format);
            evaluateBean.setEvl_content(evl_content.getText().toString());
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(evaluateBean);
            RequestBody requestBody = RequestBody.create(YGouApp.JSON,jsonObject.toJSONString());
            RestClient.builder()
                    .setBody(requestBody)
                    .setUrl("evaluate/add")
                    .onSuccess(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                           JSONObject result = JSONObject.parseObject(response);
                            String status = result.getString("status");
                            if (status.equals("ok")){
                                Log.i("评价成功", "onSuccess: ");
                                //返回订单页面 更新订单状态
                                RestClient.builder()
                                        .setUrl("order/updateTagToEvlById/"+orderId)
                                        .onSuccess(new ISuccess() {
                                            @Override
                                            public void onSuccess(String response) {
                                                JSONObject result = JSONObject.parseObject(response);
                                                String status = result.getString("status");
                                                if (status.equals("ok")) {
                                                    Log.i("修改成功", "onSuccess: ");
                                                    //跳回订单页面
                                                    OrderListDelegate delegate = new OrderListDelegate();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString(PersonalDelegate.ORDER_TYPE,"evaluate");
                                                    delegate.setArguments(bundle);
                                                    getSupportDelegate().startWithPop(delegate);
                                                   // getSupportDelegate().startWithPop(new PersonalDelegate());
                                                }else {
                                                    Toast.makeText(getContext(),"评价失败,请稍后再试",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .build()
                                        .get();
                            }
                        }
                    })
                    .failure(new IFailure() {
                        @Override
                        public void onFailure() {
                            Log.i("评价失败", "onFailure: ");
                        }
                    })
                    .build()
                    .post();
        }else {
            Log.i("不可", "sub: ");
        }
    }

    //定位
    @OnClick(R2.id.ll_evl_location)
    void location(){
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            //开启定位权限,200是标识码 请求权限
            ActivityCompat.requestPermissions((Activity) getContext(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);

        }else{
            Toast.makeText(getContext(),"已开启定位权限",Toast.LENGTH_LONG).show();
            LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            LocationProvider Netprovider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
            criteria.setAltitudeRequired(false);//无海拔要求
            criteria.setBearingRequired(false);//无方位要求
            criteria.setCostAllowed(true);//允许产生资费
            criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
            String provider = locationManager.getBestProvider(criteria, true);
            Log.i("定位", "location: "+provider);
            Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
            Geocoder geoCoder = new Geocoder(getContext(), Locale.CHINESE);
            String add = "";
            try {
                List<Address> addresses = geoCoder.getFromLocation(
                        lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),
                        1);
                Address address = addresses.get(0);
                Log.i("你所在的地址是", "getLocationAddress: " + address.toString());
                // Address[addressLines=[0:"中国",1:"北京市海淀区",2:"华奥饭店公司写字间中关村创业大街"]latitude=39.980973,hasLongitude=true,longitude=116.301712]
                String adminArea = address.getAdminArea();
                String locality = address.getLocality();
                add = adminArea+"-"+locality;
                evl_address.setText(address.getFeatureName());
                evaluateBean.setEvl_location(add);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 200://刚才的识别码
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){//用户同意权限,执行我们的操作
                    Toast.makeText(getContext(),"同意开启",Toast.LENGTH_SHORT).show();
                    location();
                }else{//用户拒绝之后,当然我们也可以弹出一个窗口,直接跳转到系统设置页面
                    Toast.makeText(getContext(),"未开启定位权限,开启权限定位",Toast.LENGTH_LONG).show();
                }
                break;
            default:break;
        }
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_evl;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        evl_content.addTextChangedListener(this);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        if (bundle!=null){
            String orderid = bundle.getString(OrderListDelegate.ORDER_ID);
            this.orderId = orderid;
        }
        Log.i("订单id是", "onCreate: "+orderId);
        evaluateBean.setEvl_order_id(Integer.parseInt(orderId));
        evaluateBean.setEvl_user_id(Integer.parseInt(YGouPreferences.getCustomAppProfile("userId")));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        RestClient.builder()
                .setUrl("order/query/"+orderId)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        Log.i("response", "onSuccess:订单id "+orderId+""+response);

                        String status = jsonObject.getString("status");
                        if (status!=null&&status.equals("ok")){
                            JSONObject data = jsonObject.getJSONObject("data");
                            String img = data.getString("order_thumb");
                            String name = data.getString("order_title");
                            double price = data.getDouble("order_price");
                            Glide.with(getContext())
                                    .load(img)
                                    .into(goods_img);
                            goods_name.setText(name);
                            goods_price.setText(String.valueOf(price));

                            evaluateBean.setEvl_goods_img(img);
                            evaluateBean.setEvl_goods_name(name);
                            evaluateBean.setEvl_goods_price(String.valueOf(price));
                        }

                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Log.i("访问接口失败", "onFailure: ");
                    }
                })
                .build()
                .get();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s!=null&&!s.equals("")){
                ll_evl_sub.setBackgroundColor(getContext().getColor(R.color.green));
                btn_sub.setTextColor(Color.WHITE);
            }else {
                ll_evl_sub.setBackgroundColor(getContext().getColor(R.color.app_background));
                btn_sub.setTextColor(getContext().getColor(R.color.btn_enable));
            }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
