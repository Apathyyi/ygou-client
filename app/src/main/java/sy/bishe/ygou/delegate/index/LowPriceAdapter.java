package sy.bishe.ygou.delegate.index;

import java.util.List;

import sy.bishe.ygou.R;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public  class LowPriceAdapter extends MultiRecyclerAdapter {

    /**
     * 默认
     *
     * @param data
     */
    public LowPriceAdapter(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.LOWPRICE, R.layout.multiple_text_img);
    }

    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);

    }
}
