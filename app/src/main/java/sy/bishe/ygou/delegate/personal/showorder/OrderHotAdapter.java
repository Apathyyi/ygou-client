package sy.bishe.ygou.delegate.personal.showorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

import sy.bishe.ygou.R;


public class OrderHotAdapter extends BaseAdapter {
    private List<Map<String,String>> list;
    private Context context;
    private LayoutInflater inflater;

    public OrderHotAdapter(Context context, List<Map<String, String>> list) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderHotAdapter.ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_showorder_hot,null);
            holder = new OrderHotAdapter.ViewHolder();
            holder.tv_rank = convertView.findViewById(R.id.hot_rank);
            holder.img_goods = convertView.findViewById(R.id.hot_img);
            holder.tv_title = convertView.findViewById(R.id.hot_title);
            holder.tv_text = convertView.findViewById(R.id.hot_text);
            convertView.setTag(holder);
        }else {
            holder = (OrderHotAdapter.ViewHolder) convertView.getTag();
        }
        Map<String,String> map = list.get(position);
        String img = map.get("img");
        String rank = map.get("rank");
        String title = map.get("title");
        String text = map.get("text");
        holder.tv_rank.setText(rank);
        holder.tv_title.setText(title);
        holder.tv_text.setText(text);
        Glide.with(context).load(img).into(holder.img_goods);
        return convertView;
    }

    class ViewHolder{
        AppCompatTextView tv_rank ;
        AppCompatImageView img_goods;
        AppCompatTextView tv_title;
        AppCompatTextView tv_text;
    }
}
