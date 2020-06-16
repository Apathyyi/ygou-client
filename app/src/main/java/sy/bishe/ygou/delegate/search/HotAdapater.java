package sy.bishe.ygou.delegate.search;

import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;

import sy.bishe.ygou.R;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class HotAdapater extends MultiRecyclerAdapter {
    /**
     * 默认
     *
     * @param data
     */
    public HotAdapater(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.SRAECH_HOT, R.layout.item_search_hot);
    }

    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case ItemType.SRAECH_HOT:
                String name = item.getField(MultipleFields.NAME);
                AppCompatTextView tv_name = helper.getView(R.id.tv_hot_name);
                tv_name.setText(name);
        }
    }
}
