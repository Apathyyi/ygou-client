package sy.bishe.ygou.delegate.personal.order;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.bean.ContentsBean;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.friends.dynamic.ContensListAdapter;
import sy.bishe.ygou.delegate.friends.dynamic.InputTextMsgDialog;
import sy.bishe.ygou.delegate.friends.dynamic.MyListView;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class ShowOrderDetailDelegate extends YGouDelegate implements AdapterView.OnItemClickListener {

    public  final List<Map<String, String>> EVL_LIST_CONTETS = new ArrayList<Map<String,String>>();

    private ContensListAdapter adapter;


    private String oederId ;

    @OnClick(R2.id.evl_show_back)
    void back(){

    }

    @BindView(R2.id.evl_show_userImg)
    CircleImageView userImg  = null;

    @BindView(R2.id.evl_show_userName)
    AppCompatTextView tv_userName = null;

    @BindView(R2.id.evl_show_goodsImg)
    AppCompatImageView goodsImg = null;

    @BindView(R2.id.evl_show_goodsName)
    AppCompatTextView tv_goodsName = null;

    @BindView(R2.id.evl_show_goodsPrice)
    AppCompatTextView tv_goodsPrive= null;

    @BindView(R2.id.evl_show_time)
    AppCompatTextView tv_time = null;

    @BindView(R2.id.evl_user_address)
    AppCompatTextView tv_address = null;
    @BindView(R2.id.evl_show_content)
    AppCompatTextView tv_show_content = null;


    @BindView(R2.id.evl_list_contents)
    MyListView listView = null;


    @OnClick(R2.id.btn_edit_comments)
    void editContents(){
        showMore("");
    }

    private void showMore(String toName) {
        InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(getContext(), R.style.dialog_center);
        inputTextMsgDialog.show();
        inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
            @Override
            public void onTextSend(String msg) {
                //点击发送按钮后，回调此方法，msg为输入的值
                Map<String,String > map = new HashMap<>();
                String userName = YGouPreferences.getCustomAppProfile("userName");
                map.put("contents_name",userName);
                map.put("contents_to_name",toName);
                map.put("contents_text",msg);
                EVL_LIST_CONTETS.add(map);
                adapter.notifyDataSetChanged();

                ContentsBean contentsBean = new ContentsBean();
                contentsBean.setContents_name(userName);
                contentsBean.setContents_to_name(toName);
                contentsBean.setContents_order_id(Integer.parseInt(oederId));
                contentsBean.setContents_text(msg);

                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(contentsBean);
                RequestBody requestBody = RequestBody.create(YGouApp.JSON,jsonObject.toJSONString());
                RestClient.builder()
                        .setUrl("contents/add")
                        .setBody(requestBody)
                        .onSuccess(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                JSONObject result = JSONObject.parseObject(response);
                                String status = result.getString("status");
                                if (status.equals("ok")){
                                    Toast.makeText(getContext(),"发送成功",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .build()
                        .post();
                inputTextMsgDialog.dismiss();

            }
        });
    }
    @Override
    public Object setLayout() {
        return R.layout.evl_show;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        listView .setDividerHeight(1);
        listView.setOnItemClickListener(this);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments!=null){
            oederId = arguments.getString(OrderListDelegate.ORDER_ID);
            Log.i("传入的商品id", "onCreate: "+oederId);
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        initEvl();
        initContens();
    }


    private void initEvl() {
        RestClient.builder()
                .setUrl("evaluate/queryByOrderId/"+oederId)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        Log.i("晒单评价详情", "onSuccess: "+response);
                        String status = jsonObject.getString("status");
                        if (status.equals("ok")){
                            JSONObject data = jsonObject.getJSONObject("data");
                            String evl_goods_img = data.getString("evl_goods_img");
                            String evl_goods_name = data.getString("evl_goods_name");
                            String evl_goods_price = data.getString("evl_goods_price");
                            String evl_time = data.getString("evl_time");
                            String evl_location = data.getString("evl_location");
                            String evl_content = data.getString("evl_content");

                            Glide.with(getContext())
                                    .load(evl_goods_img)
                                    .into(goodsImg);

                            tv_goodsName.setText(evl_goods_name);
                            tv_goodsPrive.setText(evl_goods_price);
                            tv_address.setText(evl_location);
                            tv_time.setText(evl_time);
                            tv_show_content.setText(evl_content);
                            JSONObject extr = jsonObject.getJSONObject("jsonObject");
                            JSONObject user = extr.getJSONObject("user");
                            String user_img = user.getString("user_img");
                            String userName = user.getString("user_name");

                            Glide.with(getContext())
                                    .load(user_img)
                                    .into(userImg);

                            tv_userName.setText(userName);
                        }
                    }
                })
                .build()
                .get();
    }
    private void initContens() {
        EVL_LIST_CONTETS.clear();
        RestClient.builder()
                .setUrl("contents/queryBuOrderId/"+oederId)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("评论详情", "onSuccess: "+response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("ok")){
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            int size = jsonArray.size();
                            for (int i = 0; i < size ; i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String name = object.getString("contents_name");
                                String toName = object.getString("contents_to_name");
                                String text = object.getString("contents_text");
                                Map<String,String > map = new HashMap<>();
                                map.put("contents_name",name);
                                map.put("contents_to_name",toName);
                                map.put("contents_text",text);
                                EVL_LIST_CONTETS.add(map);
                            }
                            Log.i("LIST", EVL_LIST_CONTETS.size()+"");
                             adapter = new ContensListAdapter(EVL_LIST_CONTETS,getContext());
                            listView.setAdapter(adapter);
                        }
                    }
                }).build()
                .get();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String,String > map = (Map<String, String>) adapter.getItem(position);
        String contents_name = map.get("contents_name");
        Log.i("想要回复的消消息的名字", "onItemClick: "+contents_name);
        //是自己的消息
        if (contents_name.equals(YGouPreferences.getCustomAppProfile("userName"))){
                return;
        }else {
            showMore(contents_name);
        }

    }
}
