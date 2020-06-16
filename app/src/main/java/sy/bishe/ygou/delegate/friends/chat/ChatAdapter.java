package sy.bishe.ygou.delegate.friends.chat;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sy.bishe.ygou.R;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class ChatAdapter extends MultiRecyclerAdapter {
    /**
     * 默认
     *
     * @param data
     */
    protected ChatAdapter(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.CHAT,R.layout.item_chat);
    }
    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case ItemType.CHAT:
                //数据
                String friendImgUrl = item.getField(MultipleFields.IMAGE_URL);
                String friendName = item.getField(MultipleFields.NAME);
                String chatContent = item.getField(ChatFields.CONTENT);
                String chatTime = item.getField(ChatFields.TIME);
                int count = item.getField(ChatFields.COUNT);
                //ui
                CircleImageView imgFriend = helper.getView(R.id.item_main_img);
                AppCompatTextView tvName = helper.getView(R.id.item_main_username);
                AppCompatTextView tvContent = helper.getView(R.id.item_contact_text);
                AppCompatTextView tvTime = helper.getView(R.id.item_contact_time);
                AppCompatTextView tvCount = helper.getView(R.id.item_contact_count);
                if (count==0){
                    tvCount.setVisibility(View.GONE);
                }else {
                    tvCount.setText(String.valueOf(count));
                }
                tvName.setText(friendName);
                tvContent.setText(chatContent);
                tvTime.setText(chatTime);
                Glide.with(mContext)
                        .load(friendImgUrl)
                        .into(imgFriend) ;
        }
    }
}
