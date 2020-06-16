package sy.bishe.ygou.delegate.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.appbar.AppBarLayout;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.buttons.ButtonDelegate;
import sy.bishe.ygou.delegate.cart.ShopCartDelegate;
import sy.bishe.ygou.delegate.friends.chat.ChatDetailDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.banner.HolderCreator;
import sy.bishe.ygou.utils.animation.BezieAnimation;
import sy.bishe.ygou.utils.animation.BezierUtil;
import sy.bishe.ygou.utils.animation.ScaleUpAnimation;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class GoodsDetailDelegate extends YGouDelegate {

    //图片展示
    private  final ArrayList<String> INTEGERS = new ArrayList<>();

    @BindView(R2.id.detail_banner)
    ConvenientBanner<String> sBanner = null;
          //  ConvenientBanner<Integer> sBanner = null;
    @BindView(R2.id.goods_detail_toolbar)
    Toolbar sToolbar = null;

    AppBarLayout sAppBarLayout = null;
    //底部
    @BindView(R2.id.icon_favor)
    IconTextView sIconFavor= null;

    @BindView(R2.id.tv_shopping_cart_amount)
    CircleTextView ScircleTextView =null;

    @BindView(R2.id.rl_add_shop_cart)
    RelativeLayout sRelativeLayout =null;

    @BindView(R2.id.btn_like)
    AppCompatTextView tv_like = null;

    @BindView(R2.id.icon_shop_cart)
    IconTextView sIconShopCart = null;

    @BindView(R2.id.detail_title)
    AppCompatTextView tv_title = null;

    @BindView(R2.id.detail_price)
    AppCompatTextView tv_price = null;

    @BindView(R2.id.detail_label1)
    AppCompatTextView tv_label1 = null;

    @BindView(R2.id.detail_label2)
    AppCompatTextView tv_label2 = null;

    @BindView(R2.id.detail_label3)
    AppCompatTextView tv_label3 = null;

    @BindView(R2.id.detail_label4)
    AppCompatTextView tv_label4 = null;

    @BindView(R2.id.detail_address)
    AppCompatTextView tv_address = null;

    @BindView(R2.id.detail_time)
    AppCompatTextView tv_time = null;

    @BindView(R2.id.detail_desc)
    AppCompatTextView tv_desc = null;

//商品的id
    private static final String ARG_DOODS_ID = "ARG_GOODS_ID";

    private int sGoodsId = -1;
//商品图片地址
    private static String sGoodsthunmUrl = null;
    //购物车数量
    private int sShopCount = 0;
    //发布者名字
    private String releaseName ;

    //图片加载
    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存 原始图片和裁剪图片
            .centerCrop()//按比例放大
            .dontAnimate()
            .override(100,100);//裁剪

    /**
     *
     * 联系发布的用户
     */
    @OnClick(R2.id.rl_favor)
    void onClickcontanct(){
        getSupportDelegate().start(ChatDetailDelegate.create(releaseName));
    }
    /**
     * 返回
     */
    @OnClick(R2.id.icon_goods_back)
    void OnclickBack(){
        getFragmentManager().popBackStack();
    }
//    @OnClick(R2.id.btn_like)
//    void onClicklike(){
//        if (!isliked){
//            tv_like.setTextColor(Color.RED);
//            //保存到我的喜欢
//
//        }else {
//            tv_like.setTextColor(Color.GRAY);
//        }
//    }

    /**
     * 点去跳转到购物车
     */
    @OnClick(R2.id.rl_shop_cart)
    void OnClickToCart(){
        getSupportActivity().startWithPopTo(new ShopCartDelegate(), ButtonDelegate.class,false);
        Log.d("getParentDelegate", "OnClickToCart: "+this.getParentDelegate()+"getSupportActivity()"+getSupportActivity());
    }
    /**
     * 添加购物车
     */
    @OnClick(R2.id.rl_add_shop_cart)
    void onClickAddCart(){
        final CircleImageView imageView = new CircleImageView(getContext());
        Glide.with(this)
                .load(sGoodsthunmUrl)
                .apply(OPTIONS)
                .into(imageView);
        BezieAnimation.addCart(this, sRelativeLayout, sIconShopCart, imageView, new BezierUtil.AnimationListener() {
            @Override
            public void onAnimationEnd() {
                Log.i("onAnimationEnd", "onAnimationEnd: ");
                YoYo.with(new ScaleUpAnimation())
                        .duration(500)
                        .playOn(sIconShopCart);
                //添加商品信息到购物车
                //如果之前有添加过商品 只加数量
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user_id", YGouPreferences.getCustomAppProfile("userId"));
                RequestBody requestBody = RequestBody.create(YGouApp.JSON,jsonObject.toJSONString());
                Log.i("sGoodsId",sGoodsId+"");
                RestClient.builder()
                        .setUrl("cart/addGoodsId/"+sGoodsId)
                        .onSuccess(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                               String status  = JSONObject.parseObject(response).getString("status");
                                Log.d("isAdd*****", response);
                                if (true){
                                    sShopCount++;
                                    ScircleTextView.setVisibility(View.VISIBLE);
                                    ScircleTextView.setText(String.valueOf(sShopCount));
                                }
                            }
                        })
                        .setBody(requestBody)
                        .build()
                        .post();
            }
        });
    }

    /**
     * 设置购物车数量
     * @param object
     */
    private void setShopcartCount(JSONObject object){
        sGoodsthunmUrl = object.getString("thumb");
        if (sShopCount == 0){
            ScircleTextView.setVisibility(View.GONE);
        }
    }

    /**
     * 建造者
     * @param sGoodsId
     * @return
     */
    public static GoodsDetailDelegate create(@NonNull int sGoodsId) {
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_DOODS_ID,sGoodsId);
        final GoodsDetailDelegate detailDelegate = new GoodsDetailDelegate();
        detailDelegate.setArguments(bundle);
        return detailDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        if (bundle != null){
            sGoodsId = bundle.getInt(ARG_DOODS_ID);
            Log.i("sGoodsId", sGoodsId+"");
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_goodsdetail;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        ScircleTextView.setCircleBackground(Color.RED);
    }

    //查询商品信息 同时修改商品热度加1
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RestClient.builder()
                //.setUrl("goods_detail.json")
                .setUrl("goodsinfo/queryById/"+sGoodsId)
                .loader(getContext())
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        String status = JSONObject.parseObject(response).getString("status");
                        if (status!=null&&!status.isEmpty()&&status.equals("ok"));{
                            final JSONObject object = JSONObject.parseObject(response).getJSONObject("data");
                            Log.i("GoodsInfoDelegate***********", object.toString());
                            releaseName = object.getString("goodsinfo_userName");
                            initGoods(object);
                            initBanner(object);
                            // initGoodsInfo(object);
                            setShopcartCount(object);
                        }
                    }
                })
                .build()
                .get();
    }

    /**
     * 初始化商品信息
     * @param object
     */
    private void initGoods(JSONObject object) {
        String title = object.getString("goodsinfo_name");
        Double price = object.getDouble("goodsinfo_price");
        String label = object.getString("goodsinfo_lable");
        String[] split = label.split("-");

        Log.i("split", "initGoods: "+split.length);
        String label1 = "";
        String label2 = "";
        String label3 = "";
        String label4 = "";

        label = split[0];
        if (split.length>1){
            label2 = split[1];
        }
        if (split.length>2){
            label3 = split[2];
        }
        if (split.length>3){
            label4 = split[3];
        }

        String desc = object.getString("goodsinfo_desc");

        String thumb = object.getString("goodsinfo_thumb");

        String area = object.getString("goodsinfo_area");


        String time = object.getString("goodsinfo_time");

        String banners = object.getString("goodsinfo_banners");


        tv_title.setText(title);
        tv_price.setText(String.valueOf(price));
        tv_label1.setText(label);
        tv_label2.setText(label2);
        tv_label3.setText(label3);
        tv_label4.setText(label4);

        tv_time.setText(time);
        tv_desc.setText(desc);
        tv_address.setText(area);

        //INTEGERS.add(thumb);


        if (banners==null){
            INTEGERS.add(thumb);
        }else {
            String[] banner = banners.split(";");
            for (int i = 0;i<banner.length;i++){
                INTEGERS.add(banner[i]);
            }
        }


    }

    /**
     * 初始化banner
     * @param jsonObject
     */

    private void initBanner(JSONObject jsonObject){
//        INTEGERS.add(R.mipmap.lc_1);
//        INTEGERS.add(R.mipmap.lc_2);
//        INTEGERS.add(R.mipmap.lc_3);
//        INTEGERS.add(R.mipmap.lc_4);
//        INTEGERS.add(R.mipmap.icon_app);
        sBanner.setPages(new HolderCreator(),INTEGERS)
                .setPageIndicator(new int[]{R.drawable.dot_normal,R.drawable.dot_foucs})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
              //  .setOnItemClickListener(this)
                .setCanLoop(true);

//        final JSONArray array = jsonObject.getJSONArray("banners");
//        final List<String> images = new ArrayList<>();
//        final int size = array.size();
//        Log.i("size", size+"");
//        for (int i = 0; i < size; i++) {
//            JSONObject pictures = array.getJSONObject(i);
//            String url = pictures.getString("pictures_url");
//            images.add(url);
//        }
//        sBanner.setPages(new HolderCreator(),INTEGERS)
//                .setPageIndicator(new int[]{R.drawable.dot_normal,R.drawable.dot_foucs})
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
//                .startTurning(3000)
//                .setCanLoop(true);
    }

}
