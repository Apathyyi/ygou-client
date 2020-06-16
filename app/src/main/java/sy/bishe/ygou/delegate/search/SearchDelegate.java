package sy.bishe.ygou.delegate.search;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.sort.SortDetailDelegate;
import sy.bishe.ygou.net.callback.IFailure;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class SearchDelegate extends YGouDelegate implements TextWatcher {

    private String serachName ; //搜索文本内容

    private SearchAdapter hisadapter = null; //历史记录适配器

    private HotAdapater hotAdpater = null;//热搜商品适配

    private DataSearchAdapter dataSearchAdapter = null;//搜索时的数据

    @BindView(R2.id.ll_history)
    LinearLayoutCompat layoutCompat = null;
    //热门搜索
    @BindView(R2.id.ll_hot)
    LinearLayoutCompat ll_hot = null;
    //搜索历史
    @BindView(R2.id.rv_search)
    RecyclerView sRecycleView = null;
    //搜索时自动加载数据
    @BindView(R2.id.rv_search_data)
    RecyclerView data_recycleView = null;


    @BindView(R2.id.et_search_view)
    AppCompatEditText searcheditText =null;


    @BindView(R2.id.rv_search_hot)
    RecyclerView recyclerView_hot = null;

//    @OnClick(R2.id.ll_edit_search)
//    void Onclickedit(){
//        showSoftInput(searcheditText);
//    }
    /*
    返回
     */
    @OnClick(R2.id.icon_top_search_back)
    void back(){
        getFragmentManager().popBackStack();
    }

    @OnClick(R2.id.tv_top_search)
    void onClickSearch(){
        /**
         * 跳转页面
         */
        //保存历史
        serachName = searcheditText.getText().toString();
        saveItem(searcheditText.getText().toString());
        searcheditText.setText("");
        //跳转
        searcheditText.clearFocus();
        //搜索以后
        sRecycleView.setVisibility(View.VISIBLE);
        layoutCompat.setVisibility(View.VISIBLE);
        ll_hot.setVisibility(View.VISIBLE);
        recyclerView_hot.setVisibility(View.VISIBLE);
        data_recycleView.setVisibility(View.GONE);
        //0代表模糊查询所有
        getSupportDelegate().start(SortDetailDelegate.create(-1,serachName));
    }

    @OnClick(R2.id.search_delete)
    void deleteSerach(){
        Log.i("删除搜索历史", "deleteSerach: ");
        YGouPreferences.addCustomAppProfile(SearchDataConverter.TAG_SEARCH_HISTORY,null);
        hisadapter.getData().clear();
        hisadapter.notifyDataSetChanged();
    }

    @OnClick(R2.id.search_refresh)
    /**
     * 刷新热搜
     */
    void refresh(){
        RestClient.builder()
                .setUrl("goodsinfo/queryByHot")
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ArrayList<MultipleitemEntity> convert = new HotDataConvert().setsJsonData(response).convert();
                        hotAdpater.getData().clear();
                        hotAdpater.addData(convert);
                        hotAdpater.notifyDataSetChanged();
                    }
                })
                .build()
                .get();
    }

    public  void showSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view,0);
    }

    //隐藏键盘
    public  void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    /**
     * 保存历史
     * @param searchText
     */
    private void saveItem(String searchText) {
        if (!searchText.isEmpty()&&!searchText.equals("")){
            List<String> history = new ArrayList<>();
            final String historyStr = YGouPreferences.getCustomAppProfile(SearchDataConverter.TAG_SEARCH_HISTORY);
            Log.d("history", "onClickSearch: "+historyStr);
            Log.d("searchText", "onClickSearch: "+searchText);
            if (historyStr.isEmpty()){
                history = new ArrayList<>();
            }
            else {
                history = JSON.parseObject(historyStr,ArrayList.class);
            }
            Log.i("history", "saveItem: "+history);
            if (!history.contains(searchText)){
                Log.i("不包含", "saveItem: "+searchText);
                if (history.size()<5){
                    history.add(searchText);
                }else {
                    history.remove(0);
                    history.add(searchText);
                }
                final String json = JSON.toJSONString(history);
                YGouPreferences.addCustomAppProfile(SearchDataConverter.TAG_SEARCH_HISTORY,json);
                Log.i("json", "保存的新的历史记录: ");
                final List<MultipleitemEntity> list = new SearchDataConverter().convert();
                hisadapter.getData().clear();
                hisadapter.addData(list);
                hisadapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_search;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        sRecycleView.setVisibility(View.VISIBLE);
        layoutCompat.setVisibility(View.VISIBLE);
        data_recycleView.setVisibility(View.GONE);
        List<String> list1 = new ArrayList<>();
        searcheditText.addTextChangedListener(this);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        sRecycleView.setLayoutManager(manager);

        //搜索数据
        data_recycleView.addOnItemTouchListener(DataItemClickLisentner.create(this));
        //历史
        final List<MultipleitemEntity> list = new SearchDataConverter().convert();
        hisadapter = new SearchAdapter(list);
        sRecycleView.setAdapter(hisadapter);
        sRecycleView.addOnItemTouchListener(HistoriLisenter.create(this));
        //热门
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        recyclerView_hot.setLayoutManager(layoutManager);
        recyclerView_hot.addOnItemTouchListener(HotItemClickLisenter.create(this));


        final DividerItemDecoration itemDecoration = new DividerItemDecoration();
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
        sRecycleView.addItemDecoration(itemDecoration);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /**
     * s输入框字改变
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s==null||s.equals("")||s.equals(" ")){
            sRecycleView.setVisibility(View.VISIBLE);
            layoutCompat.setVisibility(View.VISIBLE);
            ll_hot.setVisibility(View.VISIBLE);
            recyclerView_hot.setVisibility(View.VISIBLE);
        }else {
            sRecycleView.setVisibility(View.GONE);
            layoutCompat.setVisibility(View.GONE);
            ll_hot.setVisibility(View.GONE);
            recyclerView_hot.setVisibility(View.GONE);
            data_recycleView.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            data_recycleView.setLayoutManager(linearLayoutManager);
            RestClient.builder()
                    .setUrl("goodsinfoByKey/query/"+s)
                    .onSuccess(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            dataSearchAdapter=new DataSearchAdapter(new DataSearchConvert().setsJsonData(response).convert());
                            data_recycleView.setAdapter(dataSearchAdapter);
                            dataSearchAdapter.notifyDataSetChanged();
                        }
                    })
                    .build()
                    .get();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
          if (s==null||s.toString().equals("")){
              Log.i("", "afterTextChanged: "+"没有");
              sRecycleView.setVisibility(View.VISIBLE);
              layoutCompat.setVisibility(View.VISIBLE);
              ll_hot.setVisibility(View.VISIBLE);
              recyclerView_hot.setVisibility(View.VISIBLE);
              data_recycleView.setVisibility(View.GONE);
          }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        RestClient.builder()
                .setUrl("goodsinfo/queryByHot")
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("热搜商品", "onSuccess: "+response);
                        hotAdpater = new HotAdapater(new HotDataConvert().setsJsonData(response).convert());
                        recyclerView_hot.setAdapter(hotAdpater);
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
