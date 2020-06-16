package sy.bishe.ygou.delegate.index;

import android.annotation.SuppressLint;
import android.app.Service;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.delegate.buttons.ButtonDelegate;
import sy.bishe.ygou.delegate.buttons.ButtonItemDelegate;
import sy.bishe.ygou.delegate.detail.GoodsDetailDelegate;
import sy.bishe.ygou.delegate.personal.showorder.ShowOrderDelegate;
import sy.bishe.ygou.delegate.search.SearchDataConverter;
import sy.bishe.ygou.delegate.search.SearchDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.banner.HolderCreator;
import sy.bishe.ygou.ui.recycler.BaseDecoration;
import sy.bishe.ygou.ui.refresh.RefreshHandler;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class IndexDlegate extends ButtonItemDelegate implements HorizontalScrollTabStrip.TagChangeListener, OnItemClickListener {

    private boolean isAll = true;//默认是全部

    private OnItemClickListener listener = this; //点击商品


    public static final String TYPE = "TYPE"; //点击类型


    private boolean isIndex = false;//是否是首页

    @BindView(R2.id.rv_index)
    RecyclerView sRecyclerView = null;

    @BindView((R2.id.srl_index))
    SwipeRefreshLayout SswipeRefreshLayout = null;

    @BindView(R2.id.btn_index)
    Toolbar sToolbar = null;

    @BindView(R2.id.icon_indexscan)
    IconTextView sIcontextview = null;

    @BindView(R2.id.edt_search)
    AppCompatEditText sSerchView = null;


    @BindView(R2.id.hrs_index)
    HorizontalScrollTabStrip hrs_index=null;

    @BindView(R2.id.id_bottom_line)
    View view = null;
    @BindView(R2.id.index_banner)
    ConvenientBanner<String> convenientBanner = null;
    private List<Integer> goodIds = new ArrayList<>(); //banner对应的商品id
    //banners图片
    private  final ArrayList<String> INTEGERS = new ArrayList<>();
    //转换参数
    LinearLayout.LayoutParams lineLp;

    //默认当前位置 在推荐
    private int mindex = 0;
    //导航栏数组
    private String[] stringArray;

    @BindView(R2.id.c1)
    CircleImageView c1 = null;
    @BindView(R2.id.c2)
    CircleImageView c2 = null;
    @BindView(R2.id.c3)
    CircleImageView c3 = null;
    @BindView(R2.id.c4)
    CircleImageView c4 = null;
    @BindView(R2.id.c5)
    CircleImageView c5 = null;
    @BindView(R2.id.c6)
    CircleImageView c6 = null;
    @BindView(R2.id.c7)
    CircleImageView c7 = null;
    @BindView(R2.id.c8)
    CircleImageView c8 = null;
    @BindView(R2.id.c9)
    CircleImageView c9 = null;
    @BindView(R2.id.c10)
    CircleImageView c10 = null;


    @BindView(R2.id.t1)
    AppCompatTextView t1 = null;
    @BindView(R2.id.t2)
    AppCompatTextView t2 = null;
    @BindView(R2.id.t3)
    AppCompatTextView t3 = null;
    @BindView(R2.id.t4)
    AppCompatTextView t4 = null;
    @BindView(R2.id.t5)
    AppCompatTextView t5 = null;
    @BindView(R2.id.t6)
    AppCompatTextView t6 = null;
    @BindView(R2.id.t7)
    AppCompatTextView t7 = null;
    @BindView(R2.id.t8)
    AppCompatTextView t8 = null;
    @BindView(R2.id.t9)
    AppCompatTextView t9 = null;
    @BindView(R2.id.t10)
    AppCompatTextView t10 = null;
    /**
     * 消息按钮 跳转到好友会话页面
     */
//    @OnClick(R2.id.icon_indexmessage)
//    void onClickmessage(){
//        getParentDelegate().getSupportDelegate().start(new BaseChatDelegate());
//    }
    private RefreshHandler Srefreshhandler = null;
    //下拉的效果
    @SuppressLint("ResourceAsColor")
    private void initrefreshlayout(){
        SswipeRefreshLayout.setColorSchemeColors(
                Color.BLUE,
                Color.RED,
                Color.BLACK);
        SswipeRefreshLayout.setProgressViewOffset(true,120,300);
    }

    @OnClick(R2.id.ll_search_to)
   void toSearch(){
        getParentDelegate().getSupportDelegate().start(new SearchDelegate());
    }

    //初始化布局

    /**
     * 初始化banner  取热度最高的五个商品
     *
     */
    private void initConvenientBanner(){

        RestClient.builder()
                .setUrl("goodsinfo/query/banners")
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response);

                        String status = jsonObject.getString("status");
                        if (status!=null&&status.equals("ok")){
                            Log.i("获取banners", "onSuccess: ");

                            JSONArray data = jsonObject.getJSONArray("data");
                            int size = data.size();

                            for (int i=0;i<size;i++){
                                JSONObject good = data.getJSONObject(i);
                                String goodsinfo_thumb = good.getString("goodsinfo_thumb");
                                int goodsinfo_id = good.getInteger("goodsinfo_id");
                                //对应的bannerid和商品id
                                goodIds.add(goodsinfo_id);
                                INTEGERS.add(goodsinfo_thumb);
                            }
                            convenientBanner.setPages(new HolderCreator(),INTEGERS)
                                    .setPageIndicator(new int[]{R.drawable.dot_normal,R.drawable.dot_foucs})
                                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                                    .setOnItemClickListener(listener)
                                    .setCanLoop(true);
                        }
                    }
                })
                .build()
                .get();
    }

    /**
     * 初始化recycle
     */

    private void initrecycleView(){
        final GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        sRecyclerView.setLayoutManager(manager);
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
        sRecyclerView.addItemDecoration(itemDecoration);
        //设置分割线
        sRecyclerView.addItemDecoration(BaseDecoration.create(ContextCompat.getColor(getContext(),R.color.app_background),1));
        final ButtonDelegate buttonDelegate = getParentDelegate();
        sRecyclerView.addOnItemTouchListener(IndexItemClickListener.create(buttonDelegate));

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        stringArray = getContext().getResources().getStringArray(R.array.hrs);
        initConvenientBanner();
        initrefreshlayout();
        initrecycleView();
        initHrs();
        hrs_index.setOnTagChangeListener(this);
        Srefreshhandler.firstPage("goodsinfo/query");
    }

    /**
     * 初始化标题栏参数
     */
    private void initHrs() {
        initLineParams();
        String[] strings = getResources().getStringArray(R.array.hrs);
        List<String> list = Arrays.asList(strings);
        hrs_index.setTags(list);
    }

    /**
     * 初始化布局参数
     */
    private void initLineParams() {
        lineLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lineLp.weight = 0f;
        WindowManager wm = (WindowManager) getContext().getSystemService(Service.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        lineLp.width = (int) (width / (hrs_index.mDefaultShowTagCount + 0.2));
        lineLp.height = (int) (getResources().getDisplayMetrics().density * 1 + 0.2f);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_index;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        isIndex = true;
        Srefreshhandler = RefreshHandler.create(SswipeRefreshLayout,sRecyclerView,new IndexDataConverter(),getContext());
    }


    /**
     * 点击切换数据
     * @param location
     * @param isClick 是否是点击触发
     */
    @Override
    public void changeLine(int location, boolean isClick) {
        Log.i("location", "changeLine: "+hrs_index.getmTagIndex());
        int click = hrs_index.getmTagIndex();
        if (click==mindex){
            //点击当前item
            Log.i("点击当前", "changeLine: ");
        }else {
            mindex=click;
            Log.i("切换数据", "changeLine: ");
            //不同切换数据
           switch (click){
               case 0:
                   //推荐数据
                   Srefreshhandler.clear("goodsinfo/query");
                   String[] defaultsort = getResources().getStringArray(R.array.default_sort);
                   List<String> list_default = Arrays.asList(defaultsort);

                   String[] defaultimg = getResources().getStringArray(R.array.default_img);
                   List<String> list_default_img= Arrays.asList(defaultimg);

                   changeText(list_default);
                   chageimg(list_default_img);
                   break;
               case 1:
                   //书籍
                   Srefreshhandler.clear("goodsinfo/queryByName/"+1);
                   String[] booksort = getResources().getStringArray(R.array.books_sort);
                   List<String> list_book = Arrays.asList(booksort);

                   String[] bookimgs = getResources().getStringArray(R.array.book_img);
                   List<String> list_book_img = Arrays.asList(bookimgs);

                   changeText(list_book);
                   chageimg(list_book_img);
                    break;
               case 2:
                   //服装
                   Srefreshhandler.clear("goodsinfo/queryByName/"+2);
                   break;
               case 3:
                   //工具
                   Srefreshhandler.clear("goodsinfo/queryByName/"+3);
                   break;
               case 4:
                   //手机
                   Srefreshhandler.clear("goodsinfo/queryByName/"+5);
                   break;
               case 5:
                   //美妆
                   Srefreshhandler.clear("goodsinfo/queryByName/"+7);
                   break;
               case 6:
                   //乐器
                   Srefreshhandler.clear("goodsinfo/queryByName/"+9);
                   break;
           }
        }

    }

    /**
     * 改变图片  暂未实现
     * @param imglist
     */
    private void chageimg(List<String> imglist) {

    }

    /**
     * 改变图标下的品牌
     * @param list
     */
    private void changeText(List<String> list) {
        Log.i("LISt", "changeText: "+list.get(0));
        t1.setText(list.get(0));
        t2.setText(list.get(1));
        t3.setText(list.get(2));
        t4.setText(list.get(3));
        t5.setText(list.get(4));
        t6.setText(list.get(5));
        t7.setText(list.get(6));
        t8.setText(list.get(7));
        t9.setText(list.get(8));
        t10.setText(list.get(9));
    }

    /**
     * 点击广告  暂未实现
     */
    @OnClick(R2.id.ll_index_adv)
    void onClikcadv(){
        getParentDelegate().getSupportDelegate().start(new AdvDelegate());
    }

    /**
     * 低价商品
     */
    @SuppressLint("WrongConstant")
    @OnClick(R2.id.ll_1)
    void onClick1(){
        isIndex = false;
        LowPriceDelegate lowPriceDelegate = LowPriceDelegate.create(1);
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE,1);
        getParentDelegate().getSupportDelegate().start(lowPriceDelegate);
//        getSupportDelegate().start(new LowPriceDelegate(),1);
    }
    //背包
    @SuppressLint("WrongConstant")
    @OnClick(R2.id.ll_2)
    void onClick2(){
        isIndex = false;
        LowPriceDelegate lowPriceDelegate = LowPriceDelegate.create(2);
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE,2);
        getParentDelegate().getSupportDelegate().start(lowPriceDelegate);
//        getSupportDelegate().start(new LowPriceDelegate(),1);
    }

    //最新降价
    @SuppressLint("WrongConstant")
    @OnClick(R2.id.ll_3)
    void onClick3(){
        isIndex = false;
        LowPriceDelegate lowPriceDelegate = LowPriceDelegate.create(3);
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE,3);
        getParentDelegate().getSupportDelegate().start(lowPriceDelegate);
    }

    //女装
    @SuppressLint("WrongConstant")
    @OnClick(R2.id.ll_4)
    void onClick4(){
        isIndex = false;
        LowPriceDelegate lowPriceDelegate = LowPriceDelegate.create(4);
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE,4);
        getParentDelegate().getSupportDelegate().start(lowPriceDelegate);
    }

    //工具
    @SuppressLint("WrongConstant")
    @OnClick(R2.id.ll_5)
    void onClick5(){
        isIndex = false;
        LowPriceDelegate lowPriceDelegate = LowPriceDelegate.create(5);
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE,5);
        getParentDelegate().getSupportDelegate().start(lowPriceDelegate);
    }
    //品牌
    @SuppressLint("WrongConstant")
    @OnClick(R2.id.ll_6)
    void onClick6(){
        isIndex = false;
        LowPriceDelegate lowPriceDelegate = LowPriceDelegate.create(6);
        getParentDelegate().getSupportDelegate().start(lowPriceDelegate);
    }
    //限时
    @SuppressLint("WrongConstant")
    @OnClick(R2.id.ll_7)
    void onClick7(){
        isIndex = false;
        LowPriceDelegate lowPriceDelegate = LowPriceDelegate.create(7);
        getParentDelegate().getSupportDelegate().start(lowPriceDelegate);
    }
    //手机
    @SuppressLint("WrongConstant")
    @OnClick(R2.id.ll_8)
    void onClick8(){
        isIndex = false;
        LowPriceDelegate lowPriceDelegate = LowPriceDelegate.create(8);
        getParentDelegate().getSupportDelegate().start(lowPriceDelegate);
    }
    //乐器
    @SuppressLint("WrongConstant")
    @OnClick(R2.id.ll_9)
    void onClick9(){
        isIndex = false;
        LowPriceDelegate lowPriceDelegate = LowPriceDelegate.create(9);
        getParentDelegate().getSupportDelegate().start(lowPriceDelegate);
    }

    //晒单
    @SuppressLint("WrongConstant")
    @OnClick(R2.id.ll_10)
    void onClick10(){
        isIndex = false;
        getParentDelegate().getSupportDelegate().start(new ShowOrderDelegate());
    }
    /**
     * 猜你喜欢
     */
    @BindView(R2.id.tv_like)
    AppCompatTextView tv_like;
    @OnClick(R2.id.tv_like)
    void tolike(){
        if (isAll){
            //刷新数据
            tv_like.setTextColor(Color.BLACK);
            tv_like.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
            tv_all.setTextColor(getContext().getColor(R.color.text_gray));
            List<String> history = new ArrayList<>();
            final String historyStr = YGouPreferences.getCustomAppProfile(SearchDataConverter.TAG_SEARCH_HISTORY);
            Log.d("history", "onClickSearch: "+historyStr);
            //以前没用搜索过
            if (historyStr.isEmpty()){
                //通过购买记录或者购物车
                Log.i("没有搜索历史", "tolike: ");
                Srefreshhandler.clear("goodsinfo/queryByLike/"+ YGouPreferences.getCustomAppProfile("userId"));
            }
            else {
                Log.i("有搜索历史", "tolike: ");
                history = JSON.parseObject(historyStr,ArrayList.class);
                //搜索记录组成list
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("list",history);
                RequestBody requestBody = RequestBody.create(YGouApp.JSON,jsonObject.toJSONString());
                Srefreshhandler.clear("goodsinfo/queryByHist/"+ YGouPreferences.getCustomAppProfile("userId"),requestBody);
           }
            isAll=false;
        }
    }

    /**
     * 推荐
     */
    @BindView(R2.id.tv_all)
    AppCompatTextView tv_all = null;

    @OnClick(R2.id.tv_all)
    void toall(){
        if (!isAll){
            tv_like.setTextColor(getContext().getColor(R.color.text_gray));
            tv_all.setTextColor(Color.BLACK);
            Srefreshhandler.clear("goodsinfo/query");
            isAll=true;
        }
    }

    /**
     * banner点击事件
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        Log.i("POSITION", "onItemClick: "+position);
        Integer id = goodIds.get(position);
        Log.i("商品id", "onItemClick: "+id);
        final GoodsDetailDelegate detailDelegate = GoodsDetailDelegate.create(id);
        getParentDelegate().getSupportDelegate().start(detailDelegate);
    }


    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.setCanLoop(false);
    }
}
