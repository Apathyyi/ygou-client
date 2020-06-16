package sy.bishe.ygou.delegate.sort;

import android.graphics.Color;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import java.util.List;

import me.yokeyword.fragmentation.SupportHelper;
import sy.bishe.ygou.R;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class SortRecyclerAdapter extends MultiRecyclerAdapter {
    private final SortDlegate DELEGATE;
    private int sPreposition = 0;
    /**
     * 默认
     *
     * @param data
     * @param delegate
     */
    protected SortRecyclerAdapter(List<MultipleitemEntity> data, SortDlegate delegate) {
        super(data);
        this.DELEGATE = delegate;
        addItemType(ItemType.VERTICAL_LIST, R.layout.item_vertical_menu_list);
    }

    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case ItemType.VERTICAL_LIST:
                final String text = item.getField(MultipleFields.TEXT);
                final boolean isClicked = item.getField(MultipleFields.TAG);
                final AppCompatTextView name = helper.getView(R.id.tv_vertical_itemnanme);
                final View line = helper.getView(R.id.view_line);
                final View itemView = helper.itemView;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int currentPostion = helper.getAdapterPosition();
                        getData().get(sPreposition).setFields(MultipleFields.TAG,false);
                        notifyItemChanged(sPreposition);
                        //更新选中的
                        item.setFields(MultipleFields.TAG,true);
                        notifyItemChanged(currentPostion);
                        sPreposition = currentPostion;
                        final int contentId = getData().get(currentPostion).getField(MultipleFields.ID);
                        showContent(contentId);
                    }
                });

                if (!isClicked){
                    line.setVisibility(View.INVISIBLE);
                    name.setTextColor(ContextCompat.getColor(mContext,R.color.we_chat_black));
                    itemView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.item_background));
                }else {
                    line.setVisibility(View.VISIBLE);
                    name.setTextColor(ContextCompat.getColor(mContext,R.color.app_main));
                    line.setBackgroundColor(ContextCompat.getColor(mContext,R.color.app_main));
                    itemView.setBackgroundColor(Color.WHITE);
                }
                helper.setText(R.id.tv_vertical_itemnanme,text);
                break;
                default:
                    break;
        }
    }
    public void showContent(int contentId){
        final ContentDelegate delegate = ContentDelegate.newInstance(contentId);
        switchContent(delegate);
    }

    private void switchContent(ContentDelegate delegate) {
        final YGouDelegate contentDlegate = SupportHelper.findFragment(DELEGATE.getChildFragmentManager(),ContentDelegate.class);
        if (contentDlegate != null){
            contentDlegate.getSupportDelegate().replaceFragment(delegate,false);
        }
    }
}
