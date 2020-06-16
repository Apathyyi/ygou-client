package sy.bishe.ygou.delegate.search;

import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;

import sy.bishe.ygou.R;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

class DataSearchAdapter extends MultiRecyclerAdapter {
    /**
     * 默认
     *
     * @param data
     */
    public DataSearchAdapter(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.DATA_SEARCH, R.layout.item_data_search);
    }

    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case ItemType.DATA_SEARCH:
                AppCompatTextView tv_name = helper.getView(R.id.search_name);
                AppCompatTextView tv_label= helper.getView(R.id.search_label1);
                AppCompatTextView tv_label2 = helper.getView(R.id.search_label2);
                AppCompatTextView tv_label3 = helper.getView(R.id.search_label3);
                String name = item.getField(MultipleFields.NAME);
                String lable1 = item.getField(SearchFields.LABEL0);
                String lable2 = item.getField(SearchFields.LABEL1);
                String lable3 = item.getField(SearchFields.LABEL2);
                tv_name.setText(name);
                tv_label.setText(lable1);
                tv_label2.setText(lable2);
                tv_label3.setText(lable3);
        }
    }
}
