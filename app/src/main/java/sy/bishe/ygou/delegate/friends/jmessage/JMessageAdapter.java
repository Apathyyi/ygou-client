package sy.bishe.ygou.delegate.friends.jmessage;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import cn.jpush.im.android.api.model.Message;
import de.hdodenhof.circleimageview.CircleImageView;
import sy.bishe.ygou.R;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class JMessageAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Message>  datas;

    public JMessageAdapter(Context context,List<Message> datas) {
        this.context = context;
        this.datas = datas;
        this.inflater = LayoutInflater.from(context);
    }

    public JMessageAdapter(List<MultipleitemEntity> data) {
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_chat_detail,null);
            holder = new ViewHolder();
            holder.myImg = convertView.findViewById(R.id.item_jg_details_img);
            holder. myHead = convertView.findViewById(R.id.item_jg_details_head);
            holder. myHead1 = convertView.findViewById(R.id.item_jg_details_head1);
            holder. tv_myContent = convertView.findViewById(R.id.item_jg_details_content);
            holder.tv_myTime = convertView.findViewById(R.id.item_jg_details_time);
            holder.tv_tc = convertView.findViewById(R.id.item_jg_details_tc);
            holder.tv_tc1 = convertView.findViewById(R.id.item_jg_details_tc1);
            holder.tv_state = convertView.findViewById(R.id.item_jg_details_state);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Message message = datas.get(position);

        String time = String.valueOf(message.getCreateTime());
        String content = message.getContent().toString();
        holder.tv_myTime.setText(time);
        //是自己的消息
        if (message.getFromUser().equals(YGouPreferences.getCustomAppProfile("userName"))){
            //右边
            holder.myHead1.setVisibility(View.VISIBLE);
            holder.myHead.setVisibility(View.GONE);
            holder.tv_myContent.setBackgroundResource(R.drawable.bg_rectangle_corner_right);
            holder.tv_myContent.setTextColor(Color.BLACK);
            holder.tv_tc.setVisibility(View.VISIBLE);
            holder.tv_tc1.setVisibility(View.GONE);
            holder.tv_state.setVisibility(View.VISIBLE);
            if (message.haveRead()){
                holder.tv_state.setText("已读");
                holder.tv_state.setTextColor(Color.BLUE);
            }else {
                holder.tv_state.setText("未读");
                holder.tv_state.setTextColor(Color.GRAY);
            }
        }else {
            holder.tv_state.setVisibility(View.GONE);
            holder.myHead.setVisibility(View.VISIBLE);
            holder.myHead1.setVisibility(View.GONE);
            holder.tv_myContent.setTextColor(Color.RED);
            holder.tv_myContent.setBackgroundResource(R.drawable.bg_rectangle_corner_left);
            holder.tv_tc.setVisibility(View.GONE);
            holder.tv_tc1.setVisibility(View.VISIBLE);
        }
        holder.myImg.setVisibility(View.GONE);
        holder.tv_myContent.setVisibility(View.VISIBLE);
        holder.tv_myContent.setText(content);
//        Glide.with(context)
//                .load(message.get)
//                .into(myHead);
//        Glide.with(mContext)
//                .load(imgHead)
//                .into(myHead1);
        return convertView;
    }

    private static class ViewHolder
    {
        RoundedImageView myImg ;
        CircleImageView myHead ;
        CircleImageView myHead1;
        AppCompatTextView tv_myContent ;
        AppCompatTextView tv_myTime ;
        AppCompatTextView tv_tc ;
        AppCompatTextView tv_tc1;
        AppCompatTextView tv_state;
    }
}

