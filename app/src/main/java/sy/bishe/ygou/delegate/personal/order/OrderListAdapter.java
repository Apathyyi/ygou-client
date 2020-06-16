package sy.bishe.ygou.delegate.personal.order;

import android.view.View;

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

public class OrderListAdapter extends MultiRecyclerAdapter {
    private OrderTagLisenter lisenter;
    /**
     * 默认
     *
     * @param data
     */
    protected OrderListAdapter(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.ORDER_LIST, R.layout.item_order_list);
    }
    protected OrderListAdapter(List<MultipleitemEntity> data,OrderTagLisenter lisenter) {
        super(data);
        this.lisenter = lisenter;
        addItemType(ItemType.ORDER_LIST, R.layout.item_order_list);
    }
    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();
    @SuppressWarnings("SetTextI18n")
    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case ItemType.ORDER_LIST:
                final AppCompatImageView imageView = helper.getView(R.id.image_order_list);
                final AppCompatTextView title = helper.getView(R.id.tv_order_list_title);
                final AppCompatTextView price = helper.getView(R.id.tv_order_price);
                final AppCompatTextView time = helper.getView(R.id.tv_order_list_time);
                final AppCompatImageView img_release = helper.getView(R.id.order_release_userImg);
                final AppCompatTextView tv_releaseName = helper.getView(R.id.order_release_userName);
                final AppCompatTextView tv_count = helper.getView(R.id.tv_order_count);
                final AppCompatTextView tv_lable = helper.getView(R.id.order_lable);
                final AppCompatTextView tv_ensure = helper.getView(R.id.order_ensure);
                final AppCompatTextView tv_transcast = helper.getView(R.id.tv_transportation_costs);
                final AppCompatTextView tv_transcount = helper.getView(R.id.tv_transportation_count);
                final AppCompatTextView tv_totalcount = helper.getView(R.id.order_totalCount);
                final AppCompatTextView tv_totalprice = helper.getView(R.id.total_price);
                final AppCompatTextView tv_tag = helper.getView(R.id.order_tag);

                 AppCompatTextView tv_delete = helper.getView(R.id.order_delete);

                AppCompatTextView tv_send = helper.getView(R.id.order_send);

                AppCompatTextView tv_receive = helper.getView(R.id.order_receive);

                AppCompatTextView tv_evl = helper.getView(R.id.order_evl);

                AppCompatTextView tv_look = helper.getView(R.id.order_look);




                final String titleVal = item.getField(MultipleFields.TITLE);
                final String timeVal = item.getField(OrderItemFields.TIME);
                final double priceVal = item.getField(OrderItemFields.PRICE);
                final String imageUrl = item.getField(MultipleFields.IMAGE_URL);
                final String name = item.getField(OrderItemFields.RELEASENAME);
                final String img = item.getField(OrderItemFields.IMG);
                final String lables = item.getField(OrderItemFields.LABLE);
                final String transcast = item.getField(OrderItemFields.TRANSCAST);
                final String transcount = item.getField(OrderItemFields.TRANSCOUNT);
                final int totalcount = item.getField(OrderItemFields.TOTALCOIUNT);
                final double totalprice = item.getField(OrderItemFields.TOTALPRICE);
                final int count = item.getField(OrderItemFields.COUNT);
                final String tag = item.getField(OrderItemFields.TAG);


                Glide.with(mContext)
                        .load(img)
                        .apply(OPTIONS)
                        .into(img_release);
                tv_releaseName.setText(name);
                tv_count.setText("x"+String.valueOf(count));
                String[] split = lables.split(";");
                String label = "";
                int size = split.length;
               for (int i =0;i<size;i++){
                    label += split[i]+" ";
               }
                tv_lable.setText(label);
//                tv_ensure.setText();
                tv_transcast.setText("￥"+transcast);
                tv_transcount.setText(transcount);
                tv_totalcount.setText("共"+String.valueOf(totalcount)+"件商品");
                tv_totalprice.setText(String.valueOf(totalprice));
                Glide.with(mContext)
                        .load(imageUrl)
                        .apply(OPTIONS)
                        .into(imageView);
                title.setText(titleVal);
                time.setText("时间："+timeVal);
                price.setText("￥"+String.valueOf(priceVal));
                int adapterPosition = helper.getAdapterPosition();

                if (tag.equals("待发货")){
                    tv_send.setVisibility(View.VISIBLE);
                    tv_delete.setVisibility(View.GONE);
                    tv_evl.setVisibility(View.GONE);
                    tv_receive.setVisibility(View.GONE);
                    tv_look.setVisibility(View.GONE);
                    tv_send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lisenter.send(adapterPosition);
                            tv_send.setText("已提醒");
                        }
                    });

                }else if (tag.equals("待收货")){
                    tv_receive.setVisibility(View.VISIBLE);
                    tv_delete.setVisibility(View.GONE);
                    tv_evl.setVisibility(View.GONE);
                    tv_send.setVisibility(View.GONE);
                    tv_look.setVisibility(View.GONE);
                    tv_receive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lisenter.receive(adapterPosition);
                            tv_receive.setText("已确认");
                        }
                    });

                }else if (tag.equals("待评价")){
                    tv_evl.setVisibility(View.VISIBLE);
                    tv_send.setVisibility(View.GONE);
                    tv_delete.setVisibility(View.GONE);
                    tv_receive.setVisibility(View.GONE);
                    tv_evl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lisenter.evl(adapterPosition);
                        }
                    });

                }else if (tag.equals("已评价")){
                    tv_delete.setVisibility(View.VISIBLE);
                    tv_send.setVisibility(View.GONE);
                    tv_evl.setVisibility(View.GONE);
                    tv_receive.setVisibility(View.GONE);
                    tv_look.setVisibility(View.VISIBLE);
                    tv_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lisenter.delete(adapterPosition);
                        }
                    });
                    tv_look.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lisenter.look(adapterPosition);
                        }
                    });
                }
                tv_tag.setText(tag);

        }
    }
}
