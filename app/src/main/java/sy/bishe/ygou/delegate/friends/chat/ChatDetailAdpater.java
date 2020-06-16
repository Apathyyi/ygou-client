package sy.bishe.ygou.delegate.friends.chat;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.yokeyword.fragmentation.SupportFragmentDelegate;
import sy.bishe.ygou.R;
import sy.bishe.ygou.base.BaseSupportActivity;
import sy.bishe.ygou.delegate.friends.contanct.ContactInfoDelegate;
import sy.bishe.ygou.ui.main.MainActivity;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class ChatDetailAdpater extends MultiRecyclerAdapter {

    private SupportFragmentDelegate delegate = null;
    /**
     * 默认
     *
     * @param data
     */
    protected ChatDetailAdpater(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.CHATDETAIL, R.layout.item_chat_detail);
    }
    protected ChatDetailAdpater(List<MultipleitemEntity> data, SupportFragmentDelegate delegate) {
        super(data);
        Log.i("delegate", "ChatDetailAdpater:*********** "+delegate);
        addItemType(ItemType.CHATDETAIL, R.layout.item_chat_detail);
        this.delegate = delegate;
        addItemType(ItemType.CHATDETAIL, R.layout.item_chat_detail);
    }
    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();
    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case ItemType.CHATDETAIL:
                /**
                 * 数据
                 */
                //发送的图片
                String imgMine = item.getField(ChatFields.IMGMINE);
                //头像  "http://img4.imgtn.bdimg.com/it/u=1852855172,190902392&fm=26&gp=0.jpg";
                String imgHead = "http://192.168.43.191:8080/avater/IMG_20200503_213442.jpg";
               //内容
                String content = item.getField(ChatFields.CONTENT);
                //时间
                String time = item.getField(ChatFields.TIME);

                //是否是自己发的消息
                String fromName  = item.getField(ChatFields.FROMNAME);
                String type = item.getField(ChatFields.TYPE);
                /**
                 * ui
                 */
                RoundedImageView myImg = helper.getView(R.id.item_jg_details_img);
                CircleImageView myHead = helper.getView(R.id.item_jg_details_head);
                CircleImageView myHead1 = helper.getView(R.id.item_jg_details_head1);
                AppCompatTextView tv_myContent = helper.getView(R.id.item_jg_details_content);
                AppCompatTextView tv_myTime = helper.getView(R.id.item_jg_details_time);
                AppCompatTextView tv_tc = helper.getView(R.id.item_jg_details_tc);
                AppCompatTextView tv_tc1 = helper.getView(R.id.item_jg_details_tc1);
                AppCompatTextView tv_state = helper.getView(R.id.item_jg_details_state);

                //消息是自己发的
                tv_myTime.setText(time);
                tv_myTime.setVisibility(View.VISIBLE);
                if (fromName.equals(YGouPreferences.getCustomAppProfile("userName"))){
                    //右边
                    myHead1.setVisibility(View.VISIBLE);
                    myHead.setVisibility(View.GONE);
                    tv_myContent.setBackgroundResource(R.drawable.bg_rectangle_corner_right);
                    tv_myContent.setTextColor(Color.BLACK);
                    tv_tc.setVisibility(View.VISIBLE);
                    tv_tc1.setVisibility(View.GONE);
                    tv_state.setVisibility(View.VISIBLE);
                }else {
                    tv_state.setVisibility(View.GONE);
                    myHead.setVisibility(View.VISIBLE);
                    myHead1.setVisibility(View.GONE);
                    tv_myContent.setTextColor(Color.BLACK);
                    tv_myContent.setBackgroundResource(R.drawable.bg_rectangle_corner_left);
                    tv_tc.setVisibility(View.GONE);
                    tv_tc1.setVisibility(View.VISIBLE);
                }
                myImg.setVisibility(View.GONE);
                tv_myContent.setVisibility(View.VISIBLE);
                tv_myContent.setText(content);
                Glide.with(mContext)
                        .load(imgHead)
                        .into(myHead);
                Glide.with(mContext)
                        .load("https://pic4.zhimg.com/50/f9a937f76_qhd.jpg")
                        .into(myHead1);
                //获取聊天对象的名字
                myHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("对方头像", "onClick: "+fromName);
                        ContactInfoDelegate contactInfoDelegate = ContactInfoDelegate.create(fromName);
                        delegate.start(contactInfoDelegate);
                    }
                });
                myHead1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("自己头像", "onClick: ");
                        ContactInfoDelegate contactInfoDelegate = ContactInfoDelegate.create(YGouPreferences.getCustomAppProfile("userName"));
                        BaseSupportActivity baseSupportActivity = (MainActivity) v.getContext();
                        baseSupportActivity.start(contactInfoDelegate);
                    }
                });
        }
    }
}
