package sy.bishe.ygou.delegate.sort;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import sy.bishe.ygou.R;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;
import sy.bishe.ygou.utils.view.MyTextView;

public class SortDetailAdapter extends MultiRecyclerAdapter {
    /**
     * 默认
     *
     * @param data
     */
    protected SortDetailAdapter(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.SORTDETAIL,R.layout.item_sortdetail);
    }

    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        switch (helper.getItemViewType()){
            case ItemType.SORTDETAIL:
                //entity
                final String name= item.getField(MultipleFields.NAME);
                final String imgUrl = item.getField(MultipleFields.IMAGE_URL);
                final String desc = item.getField(MultipleFields.TEXT);
                final String price = item.getField(SortDetailFields.PRICE);
                final String hot = item.getField(SortDetailFields.HOT);
                final String lable = item.getField(SortDetailFields.LABLE);
                final String address = item.getField(SortDetailFields.ADDRESS);
                final String time = item.getField(SortDetailFields.TIME);
                //ui
                AppCompatTextView tv_name = helper.getView(R.id.tv_sortdetail_name);
                AppCompatImageView img = helper.getView(R.id.img_sortdetail);
                MyTextView tv_desc = helper.getView(R.id.tv_sortdetail_desc);
                AppCompatTextView tv_lable = helper.getView(R.id.tv_sortdetail_lable);
                AppCompatTextView tv_price = helper.getView(R.id.tv_sortdetail_price);
                AppCompatTextView tv_hot = helper.getView(R.id.tv_sortdetail_hot);
                AppCompatTextView tv_adress = helper.getView(R.id.tv_sortdetail_address);
                AppCompatTextView tv_time = helper.getView(R.id.tv_sortdetail_time);
                tv_time.setText(time);
                tv_adress.setText(address);
                tv_lable.setText(lable);
                tv_name.setText(name);
                tv_desc.setText(desc);
                tv_desc.setSelected(true);
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
                break;
        }
    }
}
