package sy.bishe.ygou.delegate.cart;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.EnvUtils;
import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.delegate.buttons.ButtonItemDelegate;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.net.callback.IFailure;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.pay.IPayResultListener;
import sy.bishe.ygou.pay.YGouPay;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;
import sy.bishe.ygou.utils.logger.YGouLogger;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class ShopCartDelegate extends ButtonItemDelegate implements ISuccess, ICartItemListener, IPayResultListener, SwipeRefreshLayout.OnRefreshListener {
    //list
    @BindView(R2.id.rv_shop_cart)
    RecyclerView sRecyclerView = null;
    private ShopCartAdapter sAdapter = null;
    //选中的数量
    private int sCurrentCount = 0;
    //商品总数量
    private int sTotalCount = 0;
    //总价格
    private  double sTotalPrice = 0.00;

    private boolean isAll = false;
    //选中的价格
    private double castPrice = 0.00;
    //全选
    @BindView(R2.id.icon_shop_cart_select_all)
    IconTextView sIconSelectedAll = null;
    //购物车为空
    @BindView(R2.id.stub_no_item)
    ViewStub sViewStub = null;
    //总价显示
    @BindView(R2.id.tv_shop_cart_total_price)
    AppCompatTextView stvTotalPrice = null;
//
    @BindView(R2.id.rfl_cart)
    SwipeRefreshLayout swipeRefreshLayout = null;


    /**
     * 全选按钮
     */
    @OnClick(R2.id.icon_shop_cart_select_all)
    void onClickselectedAll(){

            final int tag = (int) sIconSelectedAll.getTag();
            if (tag == 0){
                //全选
                sIconSelectedAll.setTextColor(ContextCompat.getColor(getContext(), R.color.app_main));
                sIconSelectedAll.setTag(1);
                sAdapter.setsIsSelectedAll(true);
                List<MultipleitemEntity> data = sAdapter.getData();
                for (MultipleitemEntity e:data){
                    Double price = e.getField(ShopCartItemFields.PRICE);
                    int count = e.getField(ShopCartItemFields.COUNT);
                    sTotalPrice += price * count;
                    stvTotalPrice.setText(String.valueOf(sTotalPrice));
                }
                stvTotalPrice.setText(String.valueOf(sTotalPrice));
                sAdapter.setsTotalPrice(sTotalPrice);
                //更新
                sAdapter.notifyItemRangeChanged(0,sAdapter.getItemCount());
            }else {
                sIconSelectedAll.setTextColor(Color.GRAY);
                sIconSelectedAll.setTag(0);
                sAdapter.setsIsSelectedAll(false);
                sAdapter.setsTotalPrice(0.0);
                stvTotalPrice.setText(String.valueOf(0.0));
                sAdapter.notifyItemRangeChanged(0,sAdapter.getItemCount());
            }
    }

    /**
     * 删除
     */
    @OnClick(R.id.tv_top_shop_cart_remove_selected)
    void onClickremove(){

        final List<MultipleitemEntity> list = sAdapter.getData();
        final List<MultipleitemEntity> deleteList = new ArrayList<>();
        List<Integer> goodIds = new ArrayList<>();
        for (MultipleitemEntity entity:list){
            final boolean isSelected = entity.getField(ShopCartItemFields.IS_SELECTED);
            if (isSelected){
                deleteList.add(entity);
                goodIds.add(entity.getField(MultipleFields.ID));
            }
        }
        if (deleteList.isEmpty()){
            Toast.makeText(getContext(),"请先选择商品",Toast.LENGTH_SHORT).show();
            return;
        }
        RequestBody requestBody = RequestBody.create(YGouApp.JSON,JSONObject.toJSONString(goodIds));
        RestClient.builder()
                .setUrl("cart/deleteByIds")
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        Log.i("deleteByIds*********", "onSuccess: "+response);
                        String status = jsonObject.getString("status") ;
                        if (status.equals("ok")){
                            stvTotalPrice.setText("0");
                            for (MultipleitemEntity entity:deleteList){
                                int removePosition;
                                final int entityPosition = entity.getField(ShopCartItemFields.POSITION);

                                if (entityPosition > sCurrentCount - 1){
                                    removePosition = entityPosition - (sTotalCount - sCurrentCount);
                                }else {
                                    removePosition = entityPosition;
                                }
                                if (removePosition<=sAdapter.getItemCount()) {
                                    sAdapter.remove(removePosition);
                                    sCurrentCount = sAdapter.getItemCount();
                                    sAdapter.notifyItemRangeChanged(removePosition, sAdapter.getItemCount());
                                }
                            }
                            checkCount();
                        }
                    }
                })
                .build()
                .post();
    }

    /**
     * 清空
     */
    @OnClick(R.id.tv_top_shop_cart_clear)
    void onClickClear(){

        String userId = YGouPreferences.getCustomAppProfile("userId");
       if (sAdapter.getItemCount() == 0){
           Toast.makeText(getContext(),"请先购物",Toast.LENGTH_SHORT).show();
           return;
       }
        RestClient.builder()
                .setUrl("cart/deleteAll/"+userId)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("ok")){
                            sAdapter.getData().clear();
                            sAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .build()
                .get();
        checkCount();

    }

    /**
     * 结算
     */
    @OnClick(R2.id.tv_shop_cart_pay)
    void onClickPay(){
        castPrice = sAdapter.getsTotalPrice();
        Log.i("this)", this.getSupportActivity().toString()+sAdapter.getsTotalPrice());
        YGouPay yGouPay = YGouPay.getInstance(this, castPrice);
        yGouPay.setsIPayResultListener(this);
        final List<MultipleitemEntity> list = sAdapter.getData();
        final List<MultipleitemEntity> deleteList = new ArrayList<>();
        //结算的数据
        for (MultipleitemEntity entity:list){
            final boolean isSelected = entity.getField(ShopCartItemFields.IS_SELECTED);
            if (isSelected){
                deleteList.add(entity);
            }
        }
        if (deleteList.isEmpty()){
            Toast.makeText(getContext(),"请先选择商品",Toast.LENGTH_SHORT).show();
        }else {
                    yGouPay.beginPayDialog();
        }

    }

    /**
     * 创建订单
     */
    private void creatOrder(){
        final String orderUrl = "alipay.trade.app.pay";
        String APPID = "2021001140675075";
        String APP_PRIVATE_KEY="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCCM6oIXHraTx/O4fb2JG8i6xLvZVyyYnFuXATQCW9tleh+B/wVMHqUbflO2VQNimi9XmyDuk1p/h00MMsaGinChXCynN4mBNne23wipcU9AM/oAmA156C+2C0CCxADkhzJAzIMTlSTcBwfqM+iYvm6wC+GsgmeQI9sFGVuYIFqPpzXMqLnLEQi7XDZdT+RSwuYCS1pSJZG7/vg/rLarYQVFCfdbFgWGKvvOBmdGvqv+iI6BzqCLbYEp/D3WmucKFZl4P4VZVTuCtRoyQMDz/xYeOOwzaFK9Bcv8bt/TN+aZFch79bfLjylyoBcZXmqJ/nBa2XRHMn5zmw+MPX/JZexAgMBAAECggEAdqSrcRkMXbgyLOvdhafnmbMJuxnrYSWxZTX2sLeYUQyEIawW+hwF5xFbV41UOU6BaOYNDfALMOV9KRy5RlzG5tl/u1KUX6KWAayRLmVOGOuPiEP59cTOxnjocm8wNYDEOCtwRKRwDzXeer7yDXARTipoZF4Bw7Hiv0LKtsy7wNpmgHCsgokjnt0jHJ5lWiHrH9/8N4HyJ2d1CTBAivtbImx2piM+7P9ukb+179KNllHlzfa0oyYmuKv21TKbP3l5e8jWfT7kWX0Hrlp+3H8Av/UuNfn1N0+G3406Rd52R/Gs1BPK6bZxaFAqDRxyhdloCrCo/w9tekNiC4KecrtYvQKBgQC5DyFC7ygTn/r6l2djeMoqfb5riIeBbP5ayZ3aalRbggleO0QiZ5aoKtPG19u1EpsxiC6x1u+Oj7py0hkyYnMYmiG1pDD2X+PIZuC9UCTv6NkeZFAGYx+RHFVzi6JV+J7hAQzgvASspISOFRLDK66xy4a5RaeZEKAMz/Xem3TpTwKBgQC0HRYKAGcZ4+5yA+PQxJOQagrIFlreq3FkzL/I/Qoj9WChHNBoid4Xbf7kSk9R2prbAR9nbQRwH0ET6kEj4rsspu+A3SxSEhaZEu3g35Yfuf6/PsfqOwl1vmggANT2fw2d60hQH1vFyrI9pra+3nreHtQh/5jAZv9ltyU99Ksu/wKBgEVFmJnJlCLke7pZ+mWNzX9iWmk+ThrwhbXOSrx7mOA4KPGRFcwbIpYIkgWYv7FkKZu88l23qyyeOJjKdIwbtiG7cGrh70IKWuWlPPMgkPMNIljyC9KYQDS9em8qEbZEvpRTJFLrjoRhgQz+bmuIang7S08G99mgE4k4Pzz4zCaZAoGBAK+xw8Lj267Uuc98XelTKELyPiwqKmAVSxqx488yjoa/IJBo5B9lhUDDqqWUm0VUgkRqFvEz1eVth3TeyMYxsLYA7ZZ9qXMxc9vELEl0sSOnsolpu2eQSU0S/M1jIlqFW4oVbkfCHyqU7EPZcWBE9APifLnhc2cT0cSt3+VM2wOPAoGAfXOTGpX9yh+lITGRHmCbeGQHJxtf8x2skgIi/o6FlDwPPpv4u2BOkwkZvkVHOBrQJHjv03coMYIgFBJnfdXFEZr+Q5NGrlp6vRZoUzOtB5SI9rHiVImUVn0QFZH0Xv4nAhdKx5nviA3I3fyjamQRX2pVAV5+n17lHyrRpTJ5Hqw=";
        String ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgjOqCFx62k8fzuH29iRvIusS72VcsmJxblwE0AlvbZXofgf8FTB6lG35TtlUDYpovV5sg7pNaf4dNDDLGhopwoVwspzeJgTZ3tt8IqXFPQDP6AJgNeegvtgtAgsQA5IcyQMyDE5Uk3AcH6jPomL5usAvhrIJnkCPbBRlbmCBaj6c1zKi5yxEIu1w2XU/kUsLmAktaUiWRu/74P6y2q2EFRQn3WxYFhir7zgZnRr6r/oiOgc6gi22BKfw91prnChWZeD+FWVU7grUaMkDA8/8WHjjsM2hSvQXL/G7f0zfmmRXIe/W3y48pcqAXGV5qif5wWtl0RzJ+c5sPjD1/yWXsQIDAQAB";
        final WeakHashMap<String,Object> orderParams = new WeakHashMap<>();
        orderParams.put("userid",264393);
        orderParams.put("amount",0.01);
        orderParams.put("comment","测试支付");
        orderParams.put("type",1);
        orderParams.put("ordertype",0);
        orderParams.put("isanonymous",true);
        orderParams.put("followeduser",0);
        RestClient.builder()
                .setUrl(orderUrl)
                .loader(getContext())
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        YGouLogger.d("ORDER",response);
                        final int orderId = JSON.parseObject(response).getInteger("result");
                        YGouPay.getInstance(ShopCartDelegate.this,sTotalPrice)
                                .setsIPayResultListener(ShopCartDelegate.this)
                                .setsOrderId(orderId)
                                .beginPayDialog();
                    }
                })
                .build()
                .post();

    }
@SuppressWarnings("RestrictedApi")
/**
 *  没有购物信息 去购物
 */

    private void checkCount(){
        final int count = sAdapter.getItemCount();
        if (count == 0){
            final View stubView = sViewStub.inflate();
            final AppCompatTextView tvToBuy = stubView.findViewById(R.id.tv_stub_to_buy);
            tvToBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(getContext(),"购物",Toast.LENGTH_SHORT).show();
                }
            });
            sRecyclerView.setVisibility(View.GONE);
        }else {
            sRecyclerView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_shop_cart;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        sIconSelectedAll.setTag(0);
        //支付宝沙箱测试
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
    }
    private void initView() {
        Log.i("initView", "initView: ");
        swipeRefreshLayout.setColorSchemeColors(
                Color.BLUE,
                Color.RED,
                Color.BLACK);
        swipeRefreshLayout.setProgressViewOffset(true,120,300);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        sRecyclerView.setLayoutManager(manager);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initView();
        Log.i("init", "onLazyInitView: ");
        String userId = YGouPreferences.getCustomAppProfile("userId");
        Log.i("userId", "onLazyInitView: "+userId);
        RestClient.builder()
                .setUrl("cart/query/"+userId)
                .loader(getContext())
                .onSuccess(this)
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Log.d("FAILURE", "onFailure: ");
                    }
                })
                .build()
                .get();
    }

    /**
     * 回调
     * @param response
     */
    @Override
    public void onSuccess(String response) {
        Log.i("CART", response);
        swipeRefreshLayout.setRefreshing(false);
        final ArrayList<MultipleitemEntity> list = new ShopCartDataConverter().setsJsonData(response).convert();
        sAdapter = new ShopCartAdapter(list);
        final com.choices.divider.DividerItemDecoration itemDecoration = new com.choices.divider.DividerItemDecoration();
        itemDecoration.setDividerLookup(new DividerItemDecoration.DividerLookup() {
            @Override
            public Divider getVerticalDivider(int position) {
                return null;
            }
            @Override
            public Divider getHorizontalDivider(int position) {
                return new Divider.Builder()
                        .size(2)
                        .margin(20,20)
                        .color(Color.GRAY)
                        .build();
            }
        });
        sRecyclerView.addItemDecoration(itemDecoration);
        sRecyclerView.setAdapter(sAdapter);
        sAdapter.setiCartItemListener(this);
        sTotalPrice = sAdapter.getsTotalPrice();
        stvTotalPrice.setText("0");
        checkCount();
    }

    /**
     * 选择
     * @param itemtotalPrice
     */
    @Override
    public void onItemClick(double itemtotalPrice) {
        final double price = sAdapter.getsTotalPrice();
        stvTotalPrice.setText(String.valueOf(price));
    }

    /**
     * 支付成功
     */
    @Override
    public void onPaySuccess() {

        final List<MultipleitemEntity> list = sAdapter.getData();
        final List<MultipleitemEntity> settleList = new ArrayList<>();
        List<Integer> goodIds = new ArrayList<>();
        for (MultipleitemEntity entity:list){
            final boolean isSelected = entity.getField(ShopCartItemFields.IS_SELECTED);
            if (isSelected){
                settleList.add(entity);
                goodIds.add(entity.getField(MultipleFields.ID));
            }
        }
        Log.i("onPaySuccess", "onPaySuccess: ");

        //减钱
        resetbalance();
        //加钱
        addAdmin();
        //先创建订单
       creatLocalOrder(goodIds);
       //更新数据库 删除购物车数据
        createDatabase(goodIds,settleList);

    }

    /**
     * 数据库订单增加
     * @param goodIds
     * @param settleList
     */
    private void createDatabase(List<Integer> goodIds, List<MultipleitemEntity> settleList) {
        RequestBody requestBody = RequestBody.create(YGouApp.JSON,JSONObject.toJSONString(goodIds));
        RestClient.builder()
                .setUrl("cart/deleteByIds")
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        Log.i("deleteByIds*********", "onSuccess: "+response);
                        String status = jsonObject.getString("status") ;
                        if (status.equals("ok")){
                            stvTotalPrice.setText("0");
                            for (MultipleitemEntity entity:settleList){
                                int removePosition;
                                final int entityPosition = entity.getField(ShopCartItemFields.POSITION);

                                if (entityPosition > sCurrentCount - 1){
                                    removePosition = entityPosition - (sTotalCount - sCurrentCount);
                                }else {
                                    removePosition = entityPosition;
                                }
                                if (removePosition<=sAdapter.getItemCount()) {
                                    sAdapter.remove(removePosition);
                                    sCurrentCount = sAdapter.getItemCount();
                                    sAdapter.notifyItemRangeChanged(removePosition, sAdapter.getItemCount());
                                }
                            }
                            checkCount();
                        }
                        Toast.makeText(getContext(),"支付成功",Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .post();
    }

    /**
     * 更新余额
     */
    private void resetbalance(){
        //更新余额
        RequestBody id = RequestBody.create(YGouApp.JSON,JSONObject.toJSONString(YGouPreferences.getCustomAppProfile("userId")));
        RestClient.builder()
                .setUrl("user/balance/sub/"+castPrice)
                .setBody(id)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("更新余额", "onSuccess: "+response);

                    }
                })
                .build()
                .post();
    }

    /**
     * 管理员加钱
     */
    private void addAdmin(){
        RestClient.builder()
                .setUrl("admin/add/"+castPrice)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("管理员钱包增加", "onSuccess: ");
                    }
                })
                .build()
                .get();
    }
    /**
     *
     * 创建本地订单
     */
    private void creatLocalOrder(List<Integer> list) {

        RequestBody requestBody = RequestBody.create(YGouApp.JSON,JSONObject.toJSONString(list));
        try {
            RestClient.builder()
                    .setUrl("order/add/"+YGouPreferences.getCustomAppProfile("userId"))
                    .setBody(requestBody)
                    .onSuccess(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            Log.i("creatLocalOrder", response);
                            JSONObject jsonObject = JSONObject.parseObject(response);
                            String status = jsonObject.getString("status");
                            if (status!=null&&!status.isEmpty()&&status.equals("ok")){
                                //创建订单成功
                            }else {

                            }
                        }
                    })
                    .failure(new IFailure() {
                        @Override
                        public void onFailure() {

                        }
                    })
                    .build()
                    .post();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPaying() {
        Log.i("onPaying", "onPaying: ");
    }

    @Override
    public void onPayFail() {
        Log.i("onPayFail", "onPayFail: ");
    }

    @Override
    public void onPayCancle() {
        Log.i("onPayCancle", "onPayCancle: ");
    }

    @Override
    public void onPayContentError() {

    }

    @Override
    public void onRefresh() {
        String userId = YGouPreferences.getCustomAppProfile("userId");
        Log.i("userId", "onLazyInitView: "+userId);
        RestClient.builder()
                .setUrl("cart/query/"+userId)
                .loader(getContext())
                .onSuccess(this)
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Log.d("FAILURE", "onFailure: ");
                    }
                })
                .build()
                .get();
    }
}
