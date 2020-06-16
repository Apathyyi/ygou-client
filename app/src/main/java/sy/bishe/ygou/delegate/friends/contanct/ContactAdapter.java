package sy.bishe.ygou.delegate.friends.contanct;

import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sy.bishe.ygou.R;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class ContactAdapter extends MultiRecyclerAdapter {
    /**
     * 默认
     *
     * @param data
     */
    protected ContactAdapter(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.CONTACT, R.layout.item_contact);
    }

    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case ItemType.CONTACT:
                //数据
                String imgUrl = item.getField(MultipleFields.IMAGE_URL);
                String name = item.getField(MultipleFields.NAME);
                String signature = item.getField(AddFriendFields.SIGNATURE);
                //ui
                CircleImageView imageView = helper.getView(R.id.item_contact_img);
                AppCompatTextView tvName = helper.getView(R.id.item_contact_username);
                AppCompatTextView tvSig = helper.getView(R.id.item_contact_signature);



                tvSig.setText(signature);

                tvName.setText(name);
                Glide.with(mContext)
                        .load(imgUrl)
                        .into(imageView) ;
        }
    }
}
