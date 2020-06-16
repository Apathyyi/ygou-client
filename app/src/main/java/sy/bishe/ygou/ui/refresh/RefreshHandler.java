package sy.bishe.ygou.ui.refresh;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

import okhttp3.RequestBody;
import sy.bishe.ygou.app.config.ConfigKeys;
import sy.bishe.ygou.app.config.YGou;
import sy.bishe.ygou.delegate.index.IndexDataConverter;
import sy.bishe.ygou.net.callback.IFailure;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;


//implements SwipeRefreshLayout.OnRefreshListener,

public class RefreshHandler extends RecyclerView.OnScrollListener implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private int loadmoreCount = 1;
    private int total = 5;
    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final RecyclerView RECYCLEVIEW;
    private MultiRecyclerAdapter sAdapter = null;
    private final DataConverter CONVERTER;
    private Context context = null;

    public RefreshHandler(SwipeRefreshLayout refresh_layout, RecyclerView recycleview, DataConverter converter,Context context) {
        this.REFRESH_LAYOUT = refresh_layout;
         this.RECYCLEVIEW = recycleview;
        this.CONVERTER = converter;
        this.context =context;
       this.REFRESH_LAYOUT.setOnRefreshListener(this);
    }
    public static RefreshHandler create(SwipeRefreshLayout swipeRefreshLayout,RecyclerView recyclerView,DataConverter converter,Context context){
            return new RefreshHandler(swipeRefreshLayout,recyclerView,converter,context);
    }


private void refresh() {
    //启用加载
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            REFRESH_LAYOUT.setRefreshing(false);
        }
    }, 2000);
}

    @Override
    public void onRefresh() {
        refresh();
    }

    /**
     * 首页
     * @param url
     */
    public void firstPage(String url) {
        Log.i("BASE_URL",YGou.getConfigruration(ConfigKeys.API_HOST)+url);
        RestClient.builder()
                .setUrl(url)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response",response);
                        JSONObject object = JSONObject.parseObject(response);
                        String status = object.getString("status");
                        if (status.equals("ok")){
                         if (sAdapter!=null){
                             sAdapter.getData().clear();
                         }else {
                             sAdapter = MultiRecyclerAdapter.create(new IndexDataConverter().setsJsonData(response));
                             sAdapter.setOnLoadMoreListener(RefreshHandler.this, RECYCLEVIEW);
                             RECYCLEVIEW.addOnScrollListener(RefreshHandler.this);
                             sAdapter.setEnableLoadMore(false);
                             RECYCLEVIEW.setAdapter(sAdapter);
                         }
                        }
                }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Log.i("失败", "onFailure: **********************");
                    }
                })
                .build()
                .get();
    }

    /**
     * 加载更多
     * @param count
     */
    private void loadMore(int count) {
        loadmoreCount++;
        Log.i("进入loadmore", "loadMore: ");
        sAdapter.setEnableLoadMore(false);
        RestClient.builder()
                .setUrl("goodsinfo/loadmore/"+count)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response",response);
                        JSONObject object = JSONObject.parseObject(response);
                        String status = object.getString("status");
                        if (status.equals("ok")){
                            ArrayList<MultipleitemEntity> convert = new IndexDataConverter().setsJsonData(response).convert();
                            if (convert!=null) {
                                sAdapter.addData(convert);
                                sAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                })
                .build()
                .get();
    }

    /**
     *
     * 切换数据
     * @param url
     */
    public void clear(String url){
        Log.i("BASE_URL",YGou.getConfigruration(ConfigKeys.API_HOST)+url);
        RestClient.builder()
                .setUrl(url)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response",response);
                        JSONObject object = JSONObject.parseObject(response);
                        String status = object.getString("status");
                        if (status.equals("ok")){
                            ArrayList<MultipleitemEntity> convert = new IndexDataConverter().setsJsonData(response).convert();
                            if (convert!=null){
                                sAdapter.getData().clear();
                                sAdapter.addData(convert);
                              sAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Log.i("失败", "onFailure: **********************");
                    }
                })
                .build()
                .get();
    }

    //用户搜索记录推荐
    public void clear(String url,RequestBody requestBody){
        Log.i("BASE_URL",YGou.getConfigruration(ConfigKeys.API_HOST)+url);
        RestClient.builder()
                .setUrl(url)
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response",response);
                        JSONObject object = JSONObject.parseObject(response);
                        String status = object.getString("status");
                        if (status.equals("ok")){
                            ArrayList<MultipleitemEntity> convert = new IndexDataConverter().setsJsonData(response).convert();
                            if (convert!=null){
                                sAdapter.getData().clear();
                                sAdapter.addData(convert);
                                sAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Log.i("失败", "onFailure: **********************");
                    }
                })
                .build()
                .post();
    }

    /**
     * 下滑到底部
     * @param recyclerView
     * @param newState
     */
    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        boolean isbootm = recyclerView.canScrollVertically(1);
        if (!isbootm){
            Log.i("", "onScrollStateChanged: 底部");
            if ((sAdapter.getItemCount())<(total*loadmoreCount)){
                Log.i("loadmoreCount", "onLoadMoreRequested: "+loadmoreCount+"total"+total);
                Log.i("getItemCount", "onLoadMoreRequested: "+sAdapter.getItemCount());
                sAdapter.setEnableLoadMore(false);
//                Toast.makeText(context,"已经到底部了",Toast.LENGTH_SHORT).show();
            }
            else {
                Log.i("", "onLoadMoreRequested: "+"加载更多");
                Log.i("loadmoreCount", "onLoadMoreRequested: "+loadmoreCount+"total"+total);
                Log.i("getItemCount", "onLoadMoreRequested: "+sAdapter.getItemCount());
                sAdapter.setEnableLoadMore(true);
            }
        }

    }

    /**
     * 请求加载
     */
    @Override
    public void onLoadMoreRequested() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadMore(loadmoreCount);
            }
        },200);
        if (sAdapter.getItemCount()<total*loadmoreCount){
            sAdapter.loadMoreEnd();
            sAdapter.setEnableLoadMore(false);
        }else if (sAdapter.getItemCount()==total*loadmoreCount){
            sAdapter.loadMoreComplete();
            sAdapter.setEnableLoadMore(true);
        }else {
            sAdapter.loadMoreComplete();
            sAdapter.setEnableLoadMore(false);
        }
    }

}
