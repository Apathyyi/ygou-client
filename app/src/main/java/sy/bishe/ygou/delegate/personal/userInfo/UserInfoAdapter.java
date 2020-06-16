package sy.bishe.ygou.delegate.personal.userInfo;

import java.util.List;

import sy.bishe.ygou.R;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class UserInfoAdapter extends MultiRecyclerAdapter{


    /**
     * 默认
     *
     * @param data
     */
    protected UserInfoAdapter(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.ITEM_AVATAR, R.layout.arrow_item_avatar);
    }

    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case ItemType.ITEM_AVATAR:
                final String imgUrl = item.getField(MultipleFields.IMAGE_URL);

        }
    }
}
