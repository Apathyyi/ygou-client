package sy.bishe.ygou.delegate.personal.showorder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragmentDelegate;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.personal.PersonalDelegate;
import sy.bishe.ygou.delegate.personal.order.OrderListDelegate;
import sy.bishe.ygou.delegate.personal.order.ShowOrderDetailDelegate;
import sy.bishe.ygou.net.callback.IFailure;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.recycler.BaseDecoration;


public class ShowOrderDelegate extends YGouDelegate {



    private   final List<Map<String, String>> MAP = new ArrayList<Map<String,String>>();
    OrderHotAdapter showOrderHotAdapter = null;
    ShowOrderAdapter showOrderAdapter = null;
    @BindView(R2.id.rv_showOrder_hot)
    ListView list_hot = null;

    @BindView(R2.id.rv_showOrder)
    RecyclerView rv_show = null;


    @BindView(R2.id.showorder_search)
    AppCompatEditText edit_serarch = null;
//
//    @OnClick(R2.id.btn_showorder_serach)
//        void search(){
//        String searchText = edit_serarch.getText().toString();
//        if (searchText==null||searchText.isEmpty()){
//
//        }
//
//        }

    //发布评价
    @OnClick(R2.id.showOrder_add)
    void add(){
        OrderListDelegate orderListDelegate = new OrderListDelegate();
        Bundle bundle = new Bundle();
        bundle.putString(PersonalDelegate.ORDER_TYPE,"待评价");
        orderListDelegate.setArguments(bundle);
        getSupportDelegate().startWithPop(orderListDelegate);

    }

    //返回
    @OnClick(R2.id.showOrder_back)
    void back(){
        getFragmentManager().popBackStack();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_show_order;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {

        GridLayoutManager showManager = new GridLayoutManager(getContext(),2);
        rv_show.setLayoutManager(showManager);
        rv_show.addItemDecoration(BaseDecoration.create(ContextCompat.getColor(getContext(),R.color.white),10));
        final SupportFragmentDelegate delegate = getSupportDelegate();
        rv_show.addOnItemTouchListener(ShowOrderCilciLisenter.create(delegate));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        initHot();
        initEvl();
    }

    private void initEvl() {
        RestClient.builder()
                .setUrl("evaluate/query")
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("全部晒单", "onSuccess: "+response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        String status = jsonObject.getString("status");
                        if (status!=null&&status.equals("ok")){
                             showOrderAdapter = new ShowOrderAdapter(new ShowOrderDataConvert().setsJsonData(response).convert());
                            rv_show.setAdapter(showOrderAdapter);
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Log.i("失败", "IFailure: ");
                    }
                })
                .build()
                .get();
    }

    private void initHot() {


        RestClient.builder()
                .setUrl("evaluate/queryHot")
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("最火的三条评价", "onSuccess: "+response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        String status = jsonObject.getString("status");
                        if (status!=null&&status.equals("ok")){
                            JSONArray data = jsonObject.getJSONArray("data");
                            int size = data.size();
                            for (int i = 0; i <size ; i++) {
                                JSONObject evl = data.getJSONObject(i);
                                String evl_goods_img = evl.getString("evl_goods_img");
                                int evl_order_id = evl.getInteger("evl_order_id");
                                Log.i("商品照片", "onSuccess: "+evl_goods_img);
                                String evl_content = evl.getString("evl_content");
                                String goods_name = evl.getString("evl_goods_name");
                                String rank = String.valueOf(i+1);
                                Map<String,String > map = new HashMap<>();
                                map.put("id",String.valueOf(evl_order_id));
                                map.put("rank",rank);
                                map.put("img",evl_goods_img);
                                map.put("title",goods_name);
                                map.put("text",evl_content);
                                MAP.add(map);
                            }
                            showOrderHotAdapter = new OrderHotAdapter(getContext(),MAP);
                            list_hot.setAdapter(showOrderHotAdapter);
                            list_hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Map<String, String> map = MAP.get(position);
                                    String order_id = map.get("id");
                                    Log.i("最火晒单的订单id", "onItemClick: "+order_id);
                                    //得到评价商品订单id
                                    final ShowOrderDetailDelegate detailDelegate =new ShowOrderDetailDelegate();
                                    Bundle bundle = new Bundle();
                                    bundle.putString(OrderListDelegate.ORDER_ID,order_id);
                                    detailDelegate.setArguments(bundle);
                                    Log.i("goodsId:", ""+order_id+"****position:"+position);
                                    getSupportDelegate().start(detailDelegate);
                                }
                            });


                        }
                    }
                })
                .build()
                .get();

    }


}
