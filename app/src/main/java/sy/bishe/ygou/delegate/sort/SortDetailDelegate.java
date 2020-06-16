package sy.bishe.ygou.delegate.sort;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;

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
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.index.IndexItemClickListener;
import sy.bishe.ygou.delegate.search.SearchDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.recycler.BaseDecoration;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class SortDetailDelegate extends YGouDelegate implements View.OnFocusChangeListener {

    /**
     * 传入的品牌id
     */
    private static final String ARG_SORT_ID = "ARG_GOODS_ID";

    //品牌名
    private static final String ARG_BRAND_NAME = "ARG_BRAND_NAME";
    private String sBrandname = null;


    private int sContentId = 1;

    public static SortDetailDelegate create(int sortId, String brandName) {

        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SORT_ID,sortId);
        bundle.putString(ARG_BRAND_NAME,brandName);
        final SortDetailDelegate detailDelegate = new SortDetailDelegate();
        detailDelegate.setArguments(bundle);
        return detailDelegate;
    }
    private SortDetailAdapter recyadapter = null;
   // private RefreshHandler Srefreshhandler = null;
    //排序
    boolean rankisfirst = true;

    boolean adressisfirst = true;

    private boolean timeRank = true;
    private boolean priceRank = true;

    @BindView(R2.id.spi_address)
    AppCompatSpinner spi_address = null;

    @BindView(R2.id.spi_rank)
    AppCompatSpinner spi_rank = null;

    @BindView(R2.id.tv_time_rank)
    AppCompatTextView tv_time_rank = null;

    @BindView(R2.id.tv_price_rank)
    AppCompatTextView tv_price_rank = null;

    @BindView(R2.id.rv_sortdetail)
    RecyclerView recyclerView = null;

    @BindView(R2.id.ll_search_focus)
    LinearLayoutCompat ll_search_focus = null;

    /**
     * 返回
     */
   @OnClick(R2.id.icon_sortdetail_back)
   void onCliclikback(){
       getFragmentManager().popBackStack();
   }
    /**
     * 价格排序
     */
    @OnClick(R2.id.ll_price_rank)
    void onClickRankByPrice() {
        tv_price_rank.setTextColor(Color.RED);
        tv_time_rank.setTextColor(getContext().getColor(R.color.text_gray));
        if (priceRank) {
            //默认价格降序
            priceRank = false;
            rankByPriceDesc();

        }else {
            priceRank = true;
            rankByPriceAsc();
        }
    }

    @OnClick(R2.id.ll_search_focus)
    void toSearch(){
        getSupportDelegate().startWithPop(new SearchDelegate());
    }
    /**
     * 价格升序
     */
    private void rankByPriceAsc() {
        JSONObject jsonObject = new JSONObject();
        //将登录的用户名传到后端
        jsonObject.put("goodsInfoName", sBrandname);
        jsonObject.put("area",spi_address.getSelectedItem().toString());
        Log.i("sBrandname", "OnBindView: " + sBrandname + "*******sContentId" + sContentId);
        RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());
        RestClient.builder()
                //某类型下某名牌的商品
                .setUrl("goodsinfo/queryByTypeOrderByPriceDesc/" + sContentId)
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response", "onSuccess: " + response);
                        recyadapter.getData().clear();
                        ArrayList<MultipleitemEntity> convert = new SortDetailDataConvert().setsJsonData(response).convert();
                        recyadapter.addData(convert);
                        recyadapter.notifyItemRangeChanged(0,recyadapter.getItemCount());
                        // sAdapter.notifyItemRangeChanged(removePosition, sAdapter.getItemCount());
                    }
                })
                .build()
                .post();
    }

    /**
     * 价格降序
     */
    private void rankByPriceDesc() {
        JSONObject jsonObject = new JSONObject();
        //将登录的用户名传到后端
        jsonObject.put("goodsInfoName", sBrandname);
        jsonObject.put("area",spi_address.getSelectedItem().toString());
        Log.i("sBrandname", "OnBindView: " + sBrandname + "*******sContentId" + sContentId);
        RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());
        RestClient.builder()
                //某类型下某名牌的商品
                .setUrl("goodsinfo/queryByTypeOrderByPriceAsc/" + sContentId)
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response", "onSuccess: " + response);
                        recyadapter.getData().clear();
                        ArrayList<MultipleitemEntity> convert = new SortDetailDataConvert().setsJsonData(response).convert();
                        recyadapter.addData(convert);
                        recyadapter.notifyItemRangeChanged(0,recyadapter.getItemCount());
                        // sAdapter.notifyItemRangeChanged(removePosition, sAdapter.getItemCount());
                    }
                })
                .build()
                .post();
    }

    /**
     * 筛选
     */
    @OnClick(R2.id.ll_filter)
    void Onclickfilter(){
        //右弹框
    }

    @OnClick(R2.id.tv_time_rank)
    void RankByTime(){
        tv_time_rank.setTextColor(Color.RED);
        tv_price_rank.setTextColor(getContext().getColor(R.color.text_gray));
        //默认降序
        if (timeRank){
            timeRank=false;
            rankByTimeAsc();
        }else {
            timeRank =true;
            rankByTimeDesc();
        }
    }

    /**
     * 时间降序
     */
    private void rankByTimeDesc() {
        JSONObject jsonObject = new JSONObject();
        //将登录的用户名传到后端
        jsonObject.put("goodsInfoName", sBrandname);
        jsonObject.put("area",spi_address.getSelectedItem().toString());
        Log.i("sBrandname", "OnBindView: " + sBrandname + "*******sContentId" + sContentId);
        RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());
        RestClient.builder()
                //某类型下某名牌的商品
                .setUrl("goodsinfo/queryByTypeOrderByTimeAsc/" + sContentId)
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response", "onSuccess: " + response);
                        recyadapter.getData().clear();
                        ArrayList<MultipleitemEntity> convert = new SortDetailDataConvert().setsJsonData(response).convert();
                        recyadapter.addData(convert);
                        recyadapter.notifyItemRangeChanged(0,recyadapter.getItemCount());
                        // sAdapter.notifyItemRangeChanged(removePosition, sAdapter.getItemCount());
                    }
                })
                .build()
                .post();
    }

    /**
     * 时间
     */
    private void rankByTimeAsc() {
        JSONObject jsonObject = new JSONObject();
        //将登录的用户名传到后端
        jsonObject.put("goodsInfoName", sBrandname);
        jsonObject.put("area",spi_address.getSelectedItem().toString());
        Log.i("sBrandname", "OnBindView: " + sBrandname + "*******sContentId" + sContentId);
        RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());
        RestClient.builder()
                //某类型下某名牌的商品
                .setUrl("goodsinfo/queryByTypeOrderByTimeDesc/" + sContentId)
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response", "onSuccess: " + response);
                        recyadapter.getData().clear();
                        ArrayList<MultipleitemEntity> convert = new SortDetailDataConvert().setsJsonData(response).convert();
                        recyadapter.addData(convert);
                        recyadapter.notifyItemRangeChanged(0,recyadapter.getItemCount());
                        // sAdapter.notifyItemRangeChanged(removePosition, sAdapter.getItemCount());
                    }
                })
                .build()
                .post();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sortdetail;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        queryByDefault();
        ll_search_focus.setOnFocusChangeListener(this);
        final YGouDelegate delegate = this;
        Log.i("delegate", delegate.toString());
        recyclerView.addOnItemTouchListener(IndexItemClickListener.create(delegate));

        recyclerView.addItemDecoration(BaseDecoration.create(ContextCompat.getColor(getContext(),R.color.app_background),1));
        List<String> addressList = new ArrayList<>();
        addressList.add("全国");
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
                            addressList.add(next);
                        }
                    }
                })
                .build()
                .get();
        ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(getContext(),R.layout.item_select,addressList);
        spi_address.setAdapter(addressAdapter);
        spi_address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> rankList = new ArrayList<>();
        rankList.add("默认");
        rankList.add("热度");
        rankList.add("距离");
        ArrayAdapter<String> rankAdapter = new ArrayAdapter<String>(getContext(),R.layout.item_select,rankList);
        spi_rank.setAdapter(rankAdapter);
        //点击按照排序
        spi_rank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("position", "onItemSelected: " + position+spi_rank.getSelectedItem());

                if (rankisfirst){
                    rankisfirst = false;
                    return;
                }else {
                    if (spi_rank.getSelectedItem().equals("默认")) {
                        Log.i("position", "onItemSelected: " + position);
                        //默认排序
                        rankBydefault();
                    }
                    if (spi_rank.getSelectedItem().equals("热度")) {
                        //热度排序
                        rankByhot();
                    }
                    if (spi_rank.getSelectedItem().equals("距离")) {
                        rankBydistance();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //地址排序
        spi_address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("*********", "onItemSelected: "+spi_address.getSelectedItem().toString());
                if (adressisfirst){
                    adressisfirst=false;
                    return;
                }else {
                    if (spi_address.getSelectedItem().toString().equals("全国")){
                        JSONObject jsonObject = new JSONObject();
                        //将登录的用户名传到后端
                        jsonObject.put("goodsInfoName", sBrandname);
                        RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());
                        RestClient.builder()
                                //某类型下某名牌的商品
                                .setUrl("goodsinfo/queryByType/"+sContentId)
                                .setBody(requestBody)
                                .onSuccess(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        Log.i("response", "onSuccess: " + response);
                                        recyadapter.getData().clear();
                                        ArrayList<MultipleitemEntity> convert = new SortDetailDataConvert().setsJsonData(response).convert();
                                        recyadapter.addData(convert);
                                        recyadapter.notifyItemRangeChanged(0,recyadapter.getItemCount());
                                        // sAdapter.notifyItemRangeChanged(removePosition, sAdapter.getItemCount());
                                    }
                                })
                                .build()
                                .post();
                    }else {
                        rankByarea(spi_address.getSelectedItem().toString());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * 默认
     */
    private void queryByDefault() {
        JSONObject jsonObject = new JSONObject();
        //将登录的用户名传到后端
        jsonObject.put("goodsInfoName", sBrandname);
        Log.i("sBrandname", "OnBindView: " + sBrandname + "*******sContentId" + sContentId);
        RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());
        RestClient.builder()
                //某类型下某名牌的商品
                .setUrl("goodsinfo/queryByType/" + sContentId)
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response", "onSuccess: " + response);
                        recyadapter = new SortDetailAdapter(new SortDetailDataConvert().setsJsonData(response).convert());
                        // MultiRecyclerAdapter adapter = new MultiRecyclerAdapter(new IndexDataConverter().setsJsonData(response).convert());
                        recyclerView.setAdapter(recyadapter);
                    }
                })
                .build()
                .post();
    }

    /**
     * 地区
     * @param area
     */
    private void rankByarea(String area) {
        JSONObject jsonObject = new JSONObject();
        //将登录的用户名传到后端
        jsonObject.put("goodsInfoName", sBrandname);
        jsonObject.put("area",area);
        RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());
        RestClient.builder()
                //某类型下某名牌的商品
                .setUrl("goodsinfo/queryByTypeOrderByArea/"+sContentId)
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response", "onSuccess: " + response);
                        recyadapter.getData().clear();
                        ArrayList<MultipleitemEntity> convert = new SortDetailDataConvert().setsJsonData(response).convert();
                        recyadapter.addData(convert);
                        recyadapter.notifyItemRangeChanged(0,recyadapter.getItemCount());
                        // sAdapter.notifyItemRangeChanged(removePosition, sAdapter.getItemCount());
                    }
                })
                .build()
                .post();
    }

    /**
     * 地区
     */
    private void rankBydistance() {
        JSONObject jsonObject = new JSONObject();
        //将登录的用户名传到后端
        jsonObject.put("goodsInfoName", sBrandname);
        jsonObject.put("area",spi_address.getSelectedItem().toString());
        Log.i("sBrandname", "OnBindView: " + sBrandname + "*******sContentId" + sContentId);
        RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());
        RestClient.builder()
                //某类型下某名牌的商品
                .setUrl("goodsinfo/queryByTypeOrderByDistance/" + sContentId)
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response", "onSuccess: " + response);
                        recyadapter.getData().clear();
                        ArrayList<MultipleitemEntity> convert = new SortDetailDataConvert().setsJsonData(response).convert();
                        recyadapter.addData(convert);
                        recyadapter.notifyItemRangeChanged(0,recyadapter.getItemCount());
                        // sAdapter.notifyItemRangeChanged(removePosition, sAdapter.getItemCount());
                    }
                })
                .build()
                .post();
    }

    /**
     * 热度
     */
    private void rankByhot() {
        JSONObject jsonObject = new JSONObject();
        //将登录的用户名传到后端
        jsonObject.put("goodsInfoName", sBrandname);
        jsonObject.put("area",spi_address.getSelectedItem().toString());
        Log.i("sBrandname", "OnBindView: " + sBrandname + "*******sContentId" + sContentId);
        RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());
        RestClient.builder()
                //某类型下某名牌的商品
                .setUrl("goodsinfo/queryByTypeOrderByHot/" + sContentId)
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response", "onSuccess: " + response);
                        recyadapter.getData().clear();
                        ArrayList<MultipleitemEntity> convert = new SortDetailDataConvert().setsJsonData(response).convert();
                        recyadapter.addData(convert);
                        recyadapter.notifyItemRangeChanged(0,recyadapter.getItemCount());
                        // sAdapter.notifyItemRangeChanged(removePosition, sAdapter.getItemCount());
                    }
                })
                .build()
                .post();
    }

    /**
     * 默认
     */
    private void rankBydefault() {
        JSONObject jsonObject = new JSONObject();
        //将登录的用户名传到后端
        jsonObject.put("goodsInfoName", sBrandname);
        jsonObject.put("area",spi_address.getSelectedItem().toString());
        Log.i("sBrandname", "OnBindView: " + sBrandname + "*******sContentId" + sContentId);
        RequestBody requestBody = RequestBody.create(YGouApp.JSON, jsonObject.toJSONString());
        RestClient.builder()
                //某类型下某名牌的商品
                .setUrl("goodsinfo/queryByTypeOrderByArea/" + sContentId)
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response", "onSuccess: " + response);
                        recyadapter.getData().clear();
                        ArrayList<MultipleitemEntity> convert = new SortDetailDataConvert().setsJsonData(response).convert();
                        recyadapter.addData(convert);
                        // MultiRecyclerAdapter adapter = new MultiRecyclerAdapter(new IndexDataConverter().setsJsonData(response).convert());
                        recyadapter.notifyDataSetChanged();
                    }
                })
                .build()
                .post();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        if (bundle != null){
            sContentId = bundle.getInt(ARG_SORT_ID);
            sBrandname = bundle.getString(ARG_BRAND_NAME);
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    /**
     * 搜索栏焦点
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            getParentDelegate().getSupportDelegate().start(new SearchDelegate());
        }
    }
}
