package sy.bishe.ygou.delegate.friends.dynamic;

import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import sy.bishe.ygou.R;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

import static sy.bishe.ygou.delegate.friends.dynamic.DynamicDelegate.LIST_CONTETS;

public class DynamicAdpater extends MultiRecyclerAdapter {
    //默认全部显示 不隐藏
    private boolean isExpansion= true;
    final int MAX_LINE = 6;
    /**
     * 默认
     *
     * @param data
     */
    protected DynamicAdpater(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.DYNAMIC, R.layout.item_dynamic);
    }
    public static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate();
    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case ItemType.DYNAMIC:
                //数据
                String name = item.getField(MultipleFields.NAME);
                String imgUrl = item.getField(MultipleFields.IMAGE_URL);
                String content = item.getField(MultipleFields.TEXT);
                String time = item.getField(DynamicFields.TIME);
                Log.i("TIME", time);
                String id = item.getField(MultipleFields.ID);
                //ui
                AppCompatImageView imageView = helper.getView(R.id.dynamic_contact_img);//头像
                AppCompatTextView tv_name = helper.getView(R.id.dynamic_contact_name);//昵称
                AppCompatTextView tv_content = helper.getView(R.id.dynamic_content);//内容
                AppCompatTextView tv_time = helper.getView(R.id.dynamic_time);//时间
                AppCompatTextView tv_show = helper.getView(R.id.dynamic_show);//全文展示
                AppCompatTextView tv_comments = helper.getView(R.id.btn_comments);//点击评论
                MyListView list_contents = helper.getView(R.id.list_contents);//评论listview
                list_contents .setDividerHeight(0);
                Log.i("LIST", LIST_CONTETS.size()+"");
                ContensListAdapter adapter = new ContensListAdapter(LIST_CONTETS,mContext);
                list_contents.setAdapter(adapter);
                tv_name.setText(name);
                tv_time.setText(time);
                tv_comments.setText("**");
                imageView.setImageResource(R.mipmap.u1);
                /**
                 * 全文收起逻辑0
                 */
                //全部展示
                tv_content.setMaxLines(MAX_LINE);
                tv_content.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_content.setMaxLines(Integer.MAX_VALUE);
                        int line = tv_content.getLineCount();
                        Log.i("LINE", line+"");
                        //大于6行
                        if (line >= MAX_LINE){
                            tv_content.setMaxLines(MAX_LINE);
                            tv_show.setVisibility(View.VISIBLE);
                            tv_show.setText("全文");
                            //隐藏状态
                            isExpansion = true;
                        }else {
                            tv_show.setVisibility(View.GONE);
                        }
                    }
                },1000);
                tv_content.setText(content);
                //点击全文显示
                tv_show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //全文状态
                        if (isExpansion){
                            isExpansion = false;
                            tv_content.setMaxLines(Integer.MAX_VALUE);
                            tv_show.setText("收起");
                        }
                        else {
                            isExpansion = true;
                            tv_content.setMaxLines(MAX_LINE);
                            tv_show.setText("全文");
                        }
                    }
                });
                /**
                 * 评论
                 */
                tv_comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("点击评论", "评论");
                        //显示输入法弹框
                        showMore(v);
                    }
                });
        }
    }
    private void showMore(View v) {
        InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(mContext, R.style.dialog_center);
        inputTextMsgDialog.show();
        inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
            @Override
            public void onTextSend(String msg) {
                //点击发送按钮后，回调此方法，msg为输入的值
            }
        });
    }
}
