package sy.bishe.ygou.delegate.index;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.delegate.sort.SortDetailDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class LowPriceDelegate extends YGouDelegate implements AdapterView.OnItemSelectedListener {


    private  int sType = 1;  //首页传递的类型  点击哪一个

    //排序暂未实现
    boolean rankisfirst = true;
    boolean adressisfirst = true;
    private boolean timeRank = true;


    private boolean priceRank = true;
    private GridLayoutManager manager = null;
    MultiRecyclerAdapter adapter = null;

    @BindView(R2.id.rv_lowprice)
    RecyclerView recyclerView = null;
    @BindView(R2.id.spi_address)
    AppCompatSpinner spi_address = null;

    @BindView(R2.id.spi_rank)
    AppCompatSpinner spi_rank = null;
    @BindView(R2.id.tv_time_rank)
    AppCompatTextView tv_time_rank = null;

    @BindView(R2.id.tv_price_rank)
    AppCompatTextView tv_price_rank = null;

    public static LowPriceDelegate create(int i) {
        final Bundle bundle = new Bundle();
        bundle.putInt(IndexDlegate.TYPE,i);
        final LowPriceDelegate lowPriceDelegate = new LowPriceDelegate();
        lowPriceDelegate.setArguments(bundle);
        return lowPriceDelegate;
    }


    @OnClick(R.id.ll_lowprice_book)
    void toBook(){
        getSupportDelegate().start(SortDetailDelegate.create(0,"新东方"));
    }
    @OnClick(R.id.ll_lowprice_cloth)
    void toCloth(){
        getSupportDelegate().start(SortDetailDelegate.create(0,"女装上衣"));
    }
    @OnClick(R.id.ll_lowprice_beauty)
    void tobeauty(){
        getSupportDelegate().start(SortDetailDelegate.create(0,"耳环"));
    }

    @OnClick(R2.id.lowprice_back)
    void OnclicBack(){
        getFragmentManager().popBackStack();
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_lowprice;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments!=null){
            int anInt = arguments.getInt(IndexDlegate.TYPE);
            sType = anInt;
            Log.i("取出的type值", "onCreate: "+sType);
        }
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {

        manager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(manager);
    //    recyclerView.addOnItemTouchListener(IndexItemClickListener.create();
        ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(getContext(),R.layout.item_select, YGouApp.CITYS);
        final YGouDelegate delegate = this;
        recyclerView.addOnItemTouchListener(IndexItemClickListener.create(delegate));
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
        spi_rank.setOnItemSelectedListener(this);


    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        Log.i("传过来的type值", "onLazyInitView: "+sType);
        //低价商品
        switch (sType){
            case 1:
                RestClient.builder()
                        .setUrl("goodsinfo/query/lowprice")
                        .onSuccess(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                JSONObject object = JSONObject.parseObject(response);
                                String status = object.getString("status");
                                if (status.equals("ok")){
                                    adapter =MultiRecyclerAdapter.create( new IndexDataConverter().setsJsonData(response));
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        })
                        .build()
                        .get();
                break;
            case 2:
                //背包商品
                RestClient.builder()
                        .setUrl("goodsinfo/query/bag")
                        .onSuccess(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                JSONObject object = JSONObject.parseObject(response);
                                String status = object.getString("status");
                                if (status.equals("ok")){
                                    adapter =MultiRecyclerAdapter.create( new IndexDataConverter().setsJsonData(response));
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        })
                        .build()
                        .get();
                break;

                //最新商品
            case 3:
                RestClient.builder()
                        .setUrl("goodsinfo/query/Latest")
                        .onSuccess(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                JSONObject object = JSONObject.parseObject(response);
                                String status = object.getString("status");
                                if (status.equals("ok")){
                                    adapter =MultiRecyclerAdapter.create( new IndexDataConverter().setsJsonData(response));
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        })
                        .build()
                        .get();
                break;

                //女装
            case 4:
                RestClient.builder()
                        .setUrl("goodsinfo/query/woman")
                        .onSuccess(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                JSONObject object = JSONObject.parseObject(response);
                                String status = object.getString("status");
                                if (status.equals("ok")){
                                    adapter =MultiRecyclerAdapter.create( new IndexDataConverter().setsJsonData(response));
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        })
                        .build()
                        .get();
                break;
                //工具
            case 5:
                RestClient.builder()
                        .setUrl("goodsinfo/query/tool")
                        .onSuccess(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                JSONObject object = JSONObject.parseObject(response);
                                String status = object.getString("status");
                                if (status.equals("ok")){
                                    adapter =MultiRecyclerAdapter.create( new IndexDataConverter().setsJsonData(response));
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        })
                        .build()
                        .get();
                        break;
                //随机品牌商品
            case 6:
                RestClient.builder()
                        .setUrl("goodsinfo/query/brand")
                        .onSuccess(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                JSONObject object = JSONObject.parseObject(response);
                                String status = object.getString("status");
                                if (status.equals("ok")){
                                    adapter =MultiRecyclerAdapter.create( new IndexDataConverter().setsJsonData(response));
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        })
                        .build()
                        .get();
                break;
                //书籍
            case 7:
                RestClient.builder()
                        .setUrl("goodsinfo/query/book")
                        .onSuccess(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                JSONObject object = JSONObject.parseObject(response);
                                String status = object.getString("status");
                                if (status.equals("ok")){
                                    adapter =MultiRecyclerAdapter.create( new IndexDataConverter().setsJsonData(response));
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        })
                        .build()
                        .get();
                break;
                // //手机
            case 8:
                RestClient.builder()
                        .setUrl("goodsinfo/query/phone")
                        .onSuccess(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                JSONObject object = JSONObject.parseObject(response);
                                String status = object.getString("status");
                                if (status.equals("ok")){
                                    adapter =MultiRecyclerAdapter.create( new IndexDataConverter().setsJsonData(response));
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        })
                        .build()
                        .get();
                break;
                //乐器
            case 9:
                RestClient.builder()
                        .setUrl("goodsinfo/query/music")
                        .onSuccess(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                JSONObject object = JSONObject.parseObject(response);
                                String status = object.getString("status");
                                if (status.equals("ok")){
                                    adapter =MultiRecyclerAdapter.create( new IndexDataConverter().setsJsonData(response));
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        })
                        .build()
                        .get();
                break;
                default:
                    break;

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.spi_rank:
                break;
            case R.id.spi_address:
                String  address = (String) spi_address.getSelectedItem();
                List<MultipleitemEntity> data = adapter.getData();
                List<MultipleitemEntity> newdata = new ArrayList<>();

                for (MultipleitemEntity m:data
                     ) {
                    String area = m.getField(MultipleFields.AREA);
                    if (area.contains(address)){
                            newdata.add(m);
                    }
                }
                adapter.setNewData(newdata);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            manager.setReverseLayout(true);
            adapter.notifyItemRangeChanged(0,adapter.getItemCount()-1);
          //  rankByPriceDesc();

        }else {
            priceRank = true;
            manager.setReverseLayout(false);
            adapter.notifyItemRangeChanged(0,adapter.getItemCount()-1);
        }
    }

    /**
     * 时间排序
     */
    @OnClick(R2.id.ll_time_rank)
    void timerank(){
        List<MultipleitemEntity> data = adapter.getData();
        data.sort(new Comparator<MultipleitemEntity>() {
            @Override
            public int compare(MultipleitemEntity o1, MultipleitemEntity o2) {
                int result = 0;
                String o1_time = o1.getField(MultipleFields.TIME);
                String o2_time = o2.getField(MultipleFields.TIME);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date a = simpleDateFormat.parse(o1_time);
                    Date b =  simpleDateFormat.parse(o2_time);
                    if (b.before(a)){
                        result = 1;
                    }else {
                        result = 0;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
        adapter.notifyDataSetChanged();

    }


}
