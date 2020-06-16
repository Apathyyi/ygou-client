package sy.bishe.ygou.ui.recycler;

import android.graphics.Color;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sy.bishe.ygou.R;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.delegate.index.IndexFileds;
import sy.bishe.ygou.delegate.sort.SortDetailFields;

public class MultiRecyclerAdapter extends BaseMultiItemQuickAdapter<MultipleitemEntity,MulitipleViewHolder>{

    //确保滑动不会导致重新加载
    private boolean sIsInitbanner = false;
    /**
     * 默认
     * @param data
     */
    public MultiRecyclerAdapter(List<MultipleitemEntity> data) {
        super( data);
        init();
    }

    /**
     * list构造
     * @param data
     * @return
     */
    public static MultiRecyclerAdapter create(List<MultipleitemEntity> data){
        return new MultiRecyclerAdapter(data);

    }

    /**
     * converter 构造
     * @param converter
     * @return
     */
    public static MultiRecyclerAdapter create(DataConverter converter){
        return new MultiRecyclerAdapter(converter.convert());
    }

    /**
     * 初始化
     */
    public void init(){
        addItemType(ItemType.INDEX, R.layout.multiple_text_img);
        addItemType(ItemType.LOWPRICE, R.layout.multiple_text_img);
//        setSpanSizeLookup(this);
        openLoadAnimation();
        isFirstOnly(false);
    }

    @Override
    protected MulitipleViewHolder createBaseViewHolder(View view) {
        return MulitipleViewHolder.create(view);
    }

    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        switch (helper.getItemViewType()){
            case ItemType.INDEX:
                final String name= item.getField(MultipleFields.NAME);
                final String imgUrl = item.getField(MultipleFields.IMAGE_URL);
                final String price = item.getField(SortDetailFields.PRICE);
                final String userName = item.getField(IndexFileds.USERNAME);
                final String userImgUrl =  item.getField(IndexFileds.USERIMG);
                final String hot = item.getField(SortDetailFields.HOT);
                final String check = item.getField(IndexFileds.CHECK);
                //ui
                AppCompatTextView tv_name = helper.getView(R.id.index_goods_name);
                AppCompatImageView img = helper.getView(R.id.index_goods_img);
                AppCompatTextView tv_price = helper.getView(R.id.index_goods_price);
                CircleImageView user_img = helper.getView(R.id.index_user_img);
                AppCompatTextView tv_userName = helper.getView(R.id.index_user_name);
                IconTextView icon_check = helper.getView(R.id.index_user_check);
                AppCompatTextView tv_hot = helper.getView(R.id.index_goods_hot);

                if (check.equals("1"));{
                     icon_check.setTextColor(Color.GREEN);
                     }
                tv_userName.setText(userName);
                tv_hot.setText(hot);
                tv_name.setText(name);
                tv_price.setText(price);
                tv_hot.setText(hot);
                RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(1));
                Glide.with(mContext)
                        .load(imgUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .centerCrop()
                        // .apply(options)
                        .into(img);
                Glide.with(mContext)
                        .load(userImgUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .centerCrop()
                        // .apply(options)
                        .into(user_img);
        }
    }

//    @Override
//    public int getSpanSize(GridLayoutManager gridLayoutManager, int i) {
//        return Integer.parseInt(getData().get(i).getField(MultipleFields.SPAN_SIZE));
//    }
}
