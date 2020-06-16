package sy.bishe.ygou.delegate.personal.myrelease;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;

import sy.bishe.ygou.R;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class MyBuyAdapter extends MultiRecyclerAdapter {

    private MyBuyEventLisenter lisenter = null;

    /**
     * 默认
     *
     * @param data
     */
    public MyBuyAdapter(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.MY_BUY, R.layout.item_buy);
    }


    public MyBuyAdapter(List<MultipleitemEntity> data,MyBuyEventLisenter lisenter) {
        super(data);
        this.lisenter = lisenter;
        addItemType(ItemType.MY_BUY, R.layout.item_buy);
    }
    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);

        switch (helper.getItemViewType()){
            case ItemType.MY_BUY:
                String title = item.getField(ReleaseFields.TITLE);
                String desc = item.getField(ReleaseFields.DESC);
                String type = item.getField(ReleaseFields.TYPE);
                double price = item.getField(ReleaseFields.PRICE);
                int count = item.getField(ReleaseFields.COUNT);
                String contact = item.getField(ReleaseFields.CONATCT);
                String time = item.getField(ReleaseFields.TIME);

                //
                AppCompatTextView tv_title = helper.getView(R.id.myBuy_title);
                AppCompatTextView tv_desc = helper.getView(R.id.myBuy_desc);
                AppCompatTextView tv_type = helper.getView(R.id.myBuy_type);
                AppCompatTextView tv_price = helper.getView(R.id.myBuy_price);
                AppCompatTextView tv_count = helper.getView(R.id.myBuy_count);
                AppCompatTextView tv_contatc = helper.getView(R.id.myBuy_contact);
                AppCompatTextView tv_time = helper.getView(R.id.myBuy_time);

                AppCompatTextView btn_delete = helper.getView(R.id.myBuy_delete);
                int adapterPosition = helper.getAdapterPosition();
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lisenter.deleteBuy(adapterPosition);
                    }
                });

                AppCompatTextView tv_edit = helper.getView(R.id.myBuy_edit);
                tv_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lisenter.editBuy(adapterPosition);
                    }
                });


                tv_title.setText(title);
                tv_type.setText(type);
                tv_desc.setText(desc);
                tv_price.setText(String.valueOf(price));
                tv_count.setText(String.valueOf(count));
                tv_contatc.setText(contact);
                tv_time.setText(time);



        }


    }
}
