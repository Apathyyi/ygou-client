package sy.bishe.ygou.delegate.personal.bottom;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import sy.bishe.ygou.R;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class PersonalBottomAdapter extends MultiRecyclerAdapter {
    /**
     * 默认
     *
     * @param data
     */
    public PersonalBottomAdapter(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.PERSONAL_BOTTOM,R.layout.item_personal_bottom);
    }
    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();
    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case ItemType.PERSONAL_BOTTOM:
                final String title = item.getField(MultipleFields.TITLE);
                final String desc = item.getField(PersonalItemFields.DESC);
                final String thumb = item.getField(MultipleFields.IMAGE_URL);
                final AppCompatTextView tvTiltle = helper.getView(R.id.tv_item_personal_bottom_title);
                final AppCompatTextView tvDesc = helper.getView(R.id.tv_item_personal_bottom_desc);
                final AppCompatImageView imageView = helper.getView(R.id.image_item_personal_bottom);
                tvTiltle.setText(title);
                tvDesc.setText(desc);
                Glide.with(mContext)
                        .load(thumb)
                        .into(imageView);
                break;
                default:
                    break;
        }
    }
}
