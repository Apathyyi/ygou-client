package sy.bishe.ygou.pay;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import androidx.appcompat.widget.AppCompatTextView;

import sy.bishe.ygou.R;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private GridView gridView;
    private static int ROW_NUMBER = 3;

    private String[] arr = new String[]{
            "1","2","3",
            "4","5","6",
            "7","8","9",
            "清空","0","delete"
    };

    public GridAdapter(Context context , GridView gridView) {
        this.gridView = gridView;
        this.context = context;
    }

    @Override
    public int getCount() {

        return arr.length;
    }

    @Override
    public Object getItem(int position) {
        return arr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder;
        if (convertView==null){
            holder = new MyHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gridview_number,null);
            holder.view =(AppCompatTextView) convertView.findViewById(R.id.tv_grid_number);
            if (position == 11){
                holder.view.setBackgroundResource(R.mipmap.bg_delete);
                holder.view.setTextColor(Color.GRAY);
            }else {
                holder.view.setText(arr[position]);
            }
        }
        return convertView;
    }
    class MyHolder{
        AppCompatTextView view;
    }
}
