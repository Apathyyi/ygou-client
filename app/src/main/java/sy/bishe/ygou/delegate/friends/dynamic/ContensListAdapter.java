package sy.bishe.ygou.delegate.friends.dynamic;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;
import java.util.Map;

import sy.bishe.ygou.R;

public class ContensListAdapter extends BaseAdapter {


    private List<Map<String,String>> list;
    private Context context;
    private LayoutInflater inflater;

    public ContensListAdapter(List<Map<String, String>> list, Context context) {
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
        Log.i("getCount", getCount()+"");
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_contents,null);
            holder = new ViewHolder();
            holder.tv_contentsName = convertView.findViewById(R.id.contents_name);
            holder.tv_content_text = convertView.findViewById(R.id.contents_text);
            holder.tv_contents_toName = convertView.findViewById(R.id.contents_to_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Map<String,String> map = list.get(position);
        String name = map.get("contents_name");
        String toname = map.get("contents_to_name");
        String text = map.get("contents_text");
        holder.tv_contentsName.setText(name);
        holder.tv_contents_toName.setText(toname);
        holder.tv_content_text.setText(text);
        return convertView;
    }
    class ViewHolder{
        AppCompatTextView tv_contentsName ;
        AppCompatTextView tv_contents_toName;
        AppCompatTextView tv_content_text;
    }
}
