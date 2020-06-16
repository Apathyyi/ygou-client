package sy.bishe.ygou.delegate.search;

import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;

import sy.bishe.ygou.R;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class SearchAdapter extends MultiRecyclerAdapter {
    /**
     * 默认
     *
     * @param data
     */
    protected SearchAdapter(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.ITEM_SEARCH, R.layout.item_search);
    }

    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case ItemType.ITEM_SEARCH:
                final AppCompatTextView viewSearchItem = helper.getView(R.id.tv_search_item);
                final String history = item.getField(MultipleFields.TEXT);
                viewSearchItem.setText(history);
                break;
                default:
                    break;
        }
    }
}
