package sy.bishe.ygou.delegate.personal.myrelease;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;

import java.util.List;

import sy.bishe.ygou.R;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.delegate.personal.order.OrderItemFields;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class MyReleaseAdapter extends MultiRecyclerAdapter {

    private MyReleaseEventLisenter lisenter = null;
    /**
     * 默认
     *
     * @param data
     */
    public MyReleaseAdapter(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.MY_RELEASE, R.layout.item_myrelease);
    }

    public MyReleaseAdapter(List<MultipleitemEntity> data,MyReleaseEventLisenter lisenter) {
        super(data);
        this.lisenter = lisenter;
        addItemType(ItemType.MY_RELEASE, R.layout.item_myrelease);
    }
    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case ItemType.MY_RELEASE:
                String imgurl = item.getField(MultipleFields.IMAGE_URL);
                String title = item.getField(MultipleFields.TITLE);
                Double price = item.getField(OrderItemFields.PRICE);
                int count = item.getField(OrderItemFields.COUNT);
                String time = item.getField(OrderItemFields.TIME);
                String tag = item.getField(OrderItemFields.TAG);


                AppCompatImageView img = helper.getView(R.id.myrelease_img);

                AppCompatTextView tv_title = helper.getView(R.id.myrelease_title);

                AppCompatTextView tv_price = helper.getView(R.id.myrelease_price);

                AppCompatTextView tv_count = helper.getView(R.id.myrelease_count);

                AppCompatTextView tv_time = helper.getView(R.id.myrelease_time);

                AppCompatTextView tv_tag = helper.getView(R.id.myrelease_tag);


                //下架商品
                AppCompatTextView tv_delete = helper.getView(R.id.myrelease_delete);
                //确认发货
                AppCompatTextView tv_send = helper.getView(R.id.myrelease_send);
                //查看评价
                AppCompatTextView tv_look = helper.getView(R.id.myrelease_look);
                //收货提醒
                AppCompatTextView tv_receive = helper.getView(R.id.myrelease_receive_alter);
                //评价提醒
                AppCompatTextView tv_evl = helper.getView(R.id.myrelease_eval_alter);
                //删除订单
                AppCompatTextView tv_delete_order = helper.getView(R.id.myrelease_delete_order);

                Glide.with(mContext)
                        .load(imgurl)
                        .into(img);
                tv_title.setText(title);
                tv_price.setText(String.valueOf(price));
                tv_count.setText(String.valueOf(count));
                tv_time.setText(time);
                tv_tag.setText(tag);
                int adapterPosition = helper.getAdapterPosition();
                if (tag.equals("在售")){
                    tv_delete.setVisibility(View.VISIBLE);
                    tv_send.setVisibility(View.GONE);
                    tv_look.setVisibility(View.GONE);
                    tv_receive.setVisibility(View.GONE);
                    tv_evl.setVisibility(View.GONE);
                    tv_delete_order.setVisibility(View.GONE);
                    tv_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lisenter.deleteOnsell(adapterPosition);
                        }
                    });
                }else if (tag.equals("待审核")){
                    tv_delete.setVisibility(View.VISIBLE);
                    tv_send.setVisibility(View.GONE);
                    tv_look.setVisibility(View.GONE);
                    tv_receive.setVisibility(View.GONE);
                    tv_evl.setVisibility(View.GONE);
                    tv_delete_order.setVisibility(View.GONE);
                }else if (tag.equals("被驳回")){
                    tv_delete.setVisibility(View.VISIBLE);
                    tv_send.setText("驳回！重新发布");
                    tv_send.setVisibility(View.VISIBLE);
                    tv_look.setVisibility(View.GONE);
                    tv_receive.setVisibility(View.GONE);
                    tv_evl.setVisibility(View.GONE);
                    tv_delete_order.setVisibility(View.GONE);
                }

                else if (tag.equals("待发货")){
                    tv_delete.setVisibility(View.GONE);
                    tv_send.setVisibility(View.VISIBLE);
                    tv_look.setVisibility(View.GONE);
                    tv_receive.setVisibility(View.GONE);
                    tv_evl.setVisibility(View.GONE);
                    tv_delete_order.setVisibility(View.GONE);
                    tv_send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tv_send.setText("已确认");
                            lisenter.send(adapterPosition);
                        }
                    });
                }else if (tag.equals("待收货")){
                    tv_delete.setVisibility(View.GONE);
                    tv_send.setVisibility(View.GONE);
                    tv_look.setVisibility(View.GONE);
                    tv_receive.setVisibility(View.VISIBLE);
                    tv_evl.setVisibility(View.GONE);
                    tv_delete_order.setVisibility(View.GONE);
                    tv_receive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lisenter.receive(adapterPosition);
                        }
                    });

                }else if (tag.equals("待评价")){
                    tv_delete.setVisibility(View.GONE);
                    tv_send.setVisibility(View.GONE);
                    tv_look.setVisibility(View.GONE);
                    tv_receive.setVisibility(View.GONE);
                    tv_evl.setVisibility(View.VISIBLE);
                    tv_delete_order.setVisibility(View.GONE);
                    tv_evl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lisenter.evl(adapterPosition);
                        }
                    });

                }else if (tag.equals("已评价")){

                    tv_delete.setVisibility(View.GONE);
                    tv_send.setVisibility(View.GONE);
                    tv_look.setVisibility(View.VISIBLE);
                    tv_receive.setVisibility(View.GONE);
                    tv_evl.setVisibility(View.GONE);
                    //商家不允许删除订单
                 //   tv_delete_order.setVisibility(View.VISIBLE);
//                    tv_delete_order.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            lisenter.deleteOnorder(adapterPosition);
//                        }
//                    });
                    tv_look.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lisenter.look(adapterPosition);
                        }
                    });
                }

        }
    }
}
