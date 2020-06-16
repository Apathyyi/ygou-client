package sy.bishe.ygou.delegate.friends.contanct;

import android.graphics.Color;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
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

public class MsgNotifyAdpater extends MultiRecyclerAdapter {

    private AddFriendLisenter lisenter = null;
     /**
     * 默认
     *
     * @param data
     */
    protected MsgNotifyAdpater(List<MultipleitemEntity> data,AddFriendLisenter lisenter) {
        super(data);
        this.lisenter = lisenter;
        addItemType(ItemType.NOTIFY, R.layout.item_pullmsg);
    }

    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);
        String imgUrl = item.getField(MultipleFields.IMAGE_URL);
        String name = item.getField(MultipleFields.NAME);
        String content = item.getField(MultipleFields.TEXT);
        boolean tag = item.getField(MultipleFields.TAG);
        int id = item.getField(MultipleFields.ID);
        //ui
        CircleImageView imageView = helper.getView(R.id.item_main_img);
        AppCompatTextView tv_name = helper.getView(R.id.item_main_username);
        AppCompatTextView tv_content = helper.getView(R.id.item_main_content);
        AppCompatButton btn_botify = helper.getView(R.id.item_main_bt);
        AppCompatTextView tv_time = helper.getView(R.id.item_main_time);


        tv_time.setVisibility(View.GONE);
        Glide.with(mContext)
                .load(imgUrl).into(imageView);

        tv_name.setText(name);
        tv_content.setText(content);
        btn_botify.setVisibility(View.VISIBLE);
        if (tag){
            btn_botify.setBackgroundColor(Color.WHITE);
            btn_botify.setEnabled(false);
            btn_botify.setText("已同意");
        }else{
            btn_botify.setText("同意");
        }
        btn_botify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_botify.setEnabled(false);
                btn_botify.setBackgroundColor(Color.WHITE);
                btn_botify.setText("已同意");
                lisenter.agree(id);
            }
        });
    }
}
