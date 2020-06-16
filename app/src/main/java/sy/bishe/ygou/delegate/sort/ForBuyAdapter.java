package sy.bishe.ygou.delegate.sort;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;

import java.util.List;

import sy.bishe.ygou.R;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class ForBuyAdapter extends MultiRecyclerAdapter {

    /**
     * 默认
     *
     * @param data
     */
    public ForBuyAdapter(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.SHOW_BUY, R.layout.item_show_buy);
    }

    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case ItemType.SHOW_BUY:

               AppCompatImageView imageView = helper.getView(R.id.show_buy_userImg);
               AppCompatTextView tv_title = helper.getView(R.id.show_buy_title);
               AppCompatTextView tv_price = helper.getView(R.id.show_buy_price);
               AppCompatTextView tv_contact = helper.getView(R.id.show_buy_contact);


               String img = item.getField(MultipleFields.IMAGE_URL);
               String title = item.getField(MultipleFields.TITLE);
               String price = item.getField(SortDetailFields.PRICE);
               String contact = item.getField(MultipleFields.TEXT);

                Glide.with(mContext)
                        .load(img)
                        .into(imageView);
                tv_title.setText(title);
                tv_price.setText("￥"+price);
                tv_contact.setText(contact);
        }
    }
}
