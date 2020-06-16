package sy.bishe.ygou.delegate.find;

/*
 *Create By 小群子    2018/12/10
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sy.bishe.ygou.R;
import sy.bishe.ygou.bean.ImgFileBean;

/**
 * 图片上传适配器
 */

public class LoadPicAdapter extends RecyclerView.Adapter<LoadPicAdapter.MyViewHolder> {

    Context context;
    List<ImgFileBean> fileList = null;
    View view;
    int picNum = 8;//列表的图片个数最大值

    public LoadPicAdapter(Context context, List<ImgFileBean> fileList) {
        this.context = context;
        this.fileList = fileList;
    }

    public LoadPicAdapter(Context context, List<ImgFileBean> fileList, int picNum) {
        this.context = context;
        this.fileList = fileList;
        this.picNum = picNum;
    }

    /**
     * 接口
     */
    public interface OnItemClickListener {
        void click(View view, int positon);

        void del(View view);
    }

    OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(context).inflate(R.layout.load_item_pic, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //通过默认设置第一个为空文件为添加退保，且在文件个数小于最大限制值的情况。当图片个数等于最大限制值，第一个则不是添加按钮
        if (position == 0&&fileList.get(position).getBitmap()==null) {
            holder.ivPic.setImageResource(R.mipmap.camera);//加号图片
            holder.ivPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.click(view, position);
                }
            });
            holder.ivDel.setVisibility(View.INVISIBLE);
            holder.bg_progressbar.setVisibility(View.GONE);

        } else {
//            Uri uri = Uri.parse(fileList.get(position).getFile().getPath());
//            holder.ivPic.setImageURI(uri);

           holder.ivPic.setImageBitmap(fileList.get(position).getBitmap());
            //使用压缩后的图片进行填充到界面上
            holder.ivDel.setVisibility(View.VISIBLE);
            holder.bg_progressbar.setVisibility(View.VISIBLE);
            holder.bg_progressbar.setProgress(fileList.get(position).getPg());
        }

        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断图片是否上传，上传后将无法删除
                if (fileList.get(position).isUpload()) {
                    Toast.makeText(context, "该图片已上传！", Toast.LENGTH_SHORT).show();
                } else {
                    fileList.remove(position);
                    if (fileList.size()==picNum-1&&fileList.get(0).getBitmap()!=null){
                        fileList.add(0,new ImgFileBean());
                    }//如果数量达到最大数时，前面的加号去掉，然后再减去时，则添加前面的加号
                    notifyDataSetChanged();
                    if (listener!=null){
                        listener.del(view);//传递接口，计算图片个数显示在界面中
                    }

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivPic)
        AppCompatImageView ivPic;
        @BindView(R.id.ivDel)
        ImageView ivDel;
        @BindView(R.id.bg_progressbar)
        ProgressBar bg_progressbar;
        View view;

        MyViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }
}
