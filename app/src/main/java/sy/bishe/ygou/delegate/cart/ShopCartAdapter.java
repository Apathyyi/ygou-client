package sy.bishe.ygou.delegate.cart;

import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.List;

import sy.bishe.ygou.R;
import sy.bishe.ygou.app.config.YGou;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class ShopCartAdapter extends MultiRecyclerAdapter {
    private boolean sIsSelectedAll = false;
    //加减增加
    private ICartItemListener iCartItemListener = null;
    //总价
    private double sTotalPrice = 0.00;

    public double getsTotalPrice() {
        return sTotalPrice;
    }

    public void setsTotalPrice(double sTotalPrice) {
        this.sTotalPrice = sTotalPrice;
    }

    public void setiCartItemListener(ICartItemListener iCartItemListener) {
        this.iCartItemListener = iCartItemListener;
    }

    public void setsIsSelectedAll(boolean sIsSelectedAll) {
        this.sIsSelectedAll = sIsSelectedAll;
    }

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();
    /**
     * 默认
     *
     * @param data
     */
    protected ShopCartAdapter(List<MultipleitemEntity> data) {
        super(data);
        //初始化总价
//        for (MultipleitemEntity entity:data){
//            final double price = entity.getField(ShopCartItemFields.PRICE);
//            final int count = entity.getField(ShopCartItemFields.COUNT);
//            final double total = price * count;
//            sTotalPrice = sTotalPrice + total;
//        }
        addItemType(ItemType.SHOP_CART, R.layout.shop_cart_list);
    }

    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case ItemType.SHOP_CART:
                //数据
                final int id = item.getField(MultipleFields.ID);
                final String thumb = item.getField(MultipleFields.IMAGE_URL);
                final String title = item.getField(ShopCartItemFields.TITLE);
                final String desc = item.getField(ShopCartItemFields.DESC);
                final int count = item.getField(ShopCartItemFields.COUNT);
                final double price = item.getField(ShopCartItemFields.PRICE);
                //ui
                final AppCompatImageView imageView = helper.getView(R.id.image_item_shop_cart);
                final AppCompatTextView tvTitle = helper.getView(R.id.tv_item_shop_cart_title);
                final AppCompatTextView tvDesc = helper.getView(R.id.tv_item_shop_cart_desc);
                final AppCompatTextView tvPrice = helper.getView(R.id.tv_item_shop_cart_price);
                final IconTextView iconMinus = helper.getView(R.id.icon_item_minus);
                final IconTextView iconPlus = helper.getView(R.id.icon_item_plus);
                final AppCompatTextView tvCount = helper.getView(R.id.tv_item_shop_cart_count);
                final IconTextView iconSelected = helper.getView(R.id.icon_item_shop_cart);
                //勾选全选时  单个状态选中
                item.setFields(ShopCartItemFields.IS_SELECTED,sIsSelectedAll);
                final boolean isSelected = item.getField(ShopCartItemFields.IS_SELECTED);
                tvTitle.setText(title);
                tvDesc.setText(desc);
                tvCount.setText(String.valueOf(count));
                tvPrice.setText(String.valueOf(price));
                Glide.with(mContext)
                        .load(thumb)
                        .into(imageView);
                if (isSelected){
                    iconSelected.setTextColor(ContextCompat.getColor(YGou.getApplication(),R.color.app_main));
                }else {
                    iconSelected.setTextColor(Color.GRAY);
                }
                //选择
                iconSelected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final boolean currentSleected = item.getField(ShopCartItemFields.IS_SELECTED);
                        if (currentSleected){
                            iconSelected.setTextColor(Color.GRAY);
                            item.setFields(ShopCartItemFields.IS_SELECTED,false);
                            final int  currentCount = item.getField(ShopCartItemFields.COUNT);
                            final double itemTotal = currentCount * price;
                            sTotalPrice -= itemTotal;
                            iCartItemListener.onItemClick(sTotalPrice);

                        }else {
                            iconSelected.setTextColor(ContextCompat.getColor(YGou.getApplication(),R.color.app_main));
                            item.setFields(ShopCartItemFields.IS_SELECTED,true);
                            final int  currentCount = item.getField(ShopCartItemFields.COUNT);
                            final double itemTotal = currentCount * price;
                            sTotalPrice += itemTotal;
                            iCartItemListener.onItemClick(sTotalPrice);
                        }
                    }
                });
                //减号
                iconMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int  currentCount = item.getField(ShopCartItemFields.COUNT);
                        if (Integer.parseInt(tvCount.getText().toString())>1){
                            RestClient.builder()
                                    .setUrl("cart/sub/"+id)
                                    .loader(mContext)
                                    .onSuccess(new ISuccess() {
                                        @Override
                                        public void onSuccess(String response) {
                                            JSONObject jsonObject = JSONObject.parseObject(response);
                                            //修改成功
                                            if (jsonObject.getString("status").equals("ok")){
                                                int countNum = Integer.parseInt(tvCount.getText().toString());
                                                countNum -- ;
                                                tvCount.setText(String.valueOf(countNum));
                                                if (iCartItemListener != null){
                                                    sTotalPrice = sTotalPrice - price;
                                                    final double itemTotal = countNum * price;
                                                    if (item.getField(ShopCartItemFields.IS_SELECTED)) {
                                                        iCartItemListener.onItemClick(itemTotal);
                                                    }
                                                }
                                            }
                                            else {
                                                Toast.makeText(mContext,"添加失败",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .build()
                                    .post();
                        }
                    }
                });
                //加号
                iconPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int currentCount = item.getField(ShopCartItemFields.COUNT);
                        RestClient.builder()
                                .setUrl("cart/add/"+id)
                                .loader(mContext)
                                .onSuccess(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        JSONObject jsonObject = JSONObject.parseObject(response);
                                        //修改成功
                                        if (jsonObject.getString("status").equals("ok")) {
                                            int countNum = Integer.parseInt(tvCount.getText().toString());
                                            countNum++;
                                            tvCount.setText(String.valueOf(countNum));
                                            if (iCartItemListener != null) {
                                                sTotalPrice = sTotalPrice + price;
                                                final double itemTotal = countNum * price;
                                                if (item.getField(ShopCartItemFields.IS_SELECTED)) {
                                                    iCartItemListener.onItemClick(itemTotal);
                                                }
                                            }
                                        }
                                    }
                                })
                                .build()
                                .post();
                    }
                });
                break;
                default:
                    break;
        }
    }
}
