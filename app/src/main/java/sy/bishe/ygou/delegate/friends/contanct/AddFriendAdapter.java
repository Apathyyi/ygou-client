package sy.bishe.ygou.delegate.friends.contanct;

import android.view.View;
import android.widget.RelativeLayout;

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

public class AddFriendAdapter extends MultiRecyclerAdapter {

    private AddFriendLisenter lisenter = null;
    /**
     * 默认
     *
     * @param data
     */
    public AddFriendAdapter(List<MultipleitemEntity> data,AddFriendLisenter lisenter) {
        super(data);
        addItemType(ItemType.ADD_FRIEND, R.layout.item_contact);
        this.lisenter = lisenter;
    }

    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);

        switch (helper.getItemViewType()){
            case ItemType.ADD_FRIEND:

                String imgurl = item.getField(MultipleFields.IMAGE_URL);
                String name = item.getField(MultipleFields.NAME);
                String signature = item.getField(AddFriendFields.SIGNATURE);

                CircleImageView userImg = helper.getView(R.id.item_contact_img);

                AppCompatTextView tv_name  =helper.getView(R.id.item_contact_username);

                AppCompatTextView tv_sig  =helper.getView(R.id.item_contact_signature);

                RelativeLayout relativeLayout = helper.getView(R.id.btn_addfriend);

                AppCompatTextView tv_add_send = helper.getView(R.id.tv_add_send);


                relativeLayout.setVisibility(View.VISIBLE);

                relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_add_send.setText("已发送");
                        tv_add_send.setClickable(false);
                        lisenter.addByName(name);
                    }
                });

                Glide.with(mContext)
                        .load(imgurl)
                        .into(userImg);

                tv_name.setText(name);
                tv_sig.setText(signature);
        }
    }
}
