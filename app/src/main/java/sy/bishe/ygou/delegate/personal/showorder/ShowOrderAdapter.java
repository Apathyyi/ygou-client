package sy.bishe.ygou.delegate.personal.showorder;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sy.bishe.ygou.R;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.delegate.personal.order.OrderItemFields;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class ShowOrderAdapter extends MultiRecyclerAdapter {
    /**
     * 默认
     *
     * @param data
     */
    public ShowOrderAdapter(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.SHOWORDER, R.layout.item_showorder);
    }

    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);

        switch (helper.getItemViewType()){
            case ItemType.SHOWORDER:
                final AppCompatImageView goods_img = helper.getView(R.id.show_goods_img);
                final AppCompatTextView tv_content  = helper.getView(R.id.show_content);
                final AppCompatTextView tv_time  = helper.getView(R.id.show_time);
                final CircleImageView user_img   = helper.getView(R.id.show_uer_img);

                final AppCompatTextView tv_user_name  = helper.getView(R.id.show_user_name);
                final AppCompatTextView tv_show_address  = helper.getView(R.id.show_address);

               String userImg =  item.getField(MultipleFields.IMAGE_URL);
                String goodsImg = item.getField(OrderItemFields.IMG);
                String content = item.getField(MultipleFields.TEXT);
                String time = item.getField(MultipleFields.TIME);
                String userName = item.getField(MultipleFields.NAME);
                String address  =item.getField(OrderItemFields.ADDRESS);

                Glide.with(mContext)
                        .load(goodsImg)
                        .into(goods_img);

                Glide.with(mContext)
                        .load(userImg)
                        .into(user_img);

                tv_user_name.setText(userName);
                tv_content.setText(content);
                tv_show_address.setText(address);
                tv_time.setText(time);



        }
    }
}
