package sy.bishe.ygou.delegate.personal.myrelease;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import de.hdodenhof.circleimageview.CircleImageView;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.delegate.personal.order.OrderItemFields;
import sy.bishe.ygou.delegate.personal.order.OrderListDelegate;
import sy.bishe.ygou.delegate.personal.order.ShowOrderDetailDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class MyReleaseDelegate extends YGouDelegate implements MyReleaseEventLisenter,MyBuyEventLisenter{

    final CharSequence[] items = {"确认", "取消"};
    //默认是发布
    private boolean currentState = true;
    //发布监听器
    private MyReleaseEventLisenter Rlisenter;
    private MyReleaseAdapter Radapter;

    //求购监听器
    private MyBuyEventLisenter Blisenter;
    private MyBuyAdapter Badapter;



    @BindView(R2.id.ll_release_null)
    LinearLayout ll_release_null = null;


    @BindView(R2.id.rv_release)
    RecyclerView recyclerView = null;

    @BindView(R2.id.rv_buy)
    RecyclerView recyclerView_buy = null;

    @BindView(R2.id.user_img)
    CircleImageView user_img = null;

    /**
     * 返回
     */
    @OnClick(R2.id.release_icon_back)
    void back(){
        getFragmentManager().popBackStack();
    }

    @BindView(R2.id.btn_myrelease)
    AppCompatTextView btn_release= null;
    /*
    发布信息
     */
    @OnClick(R2.id.btn_myrelease)
    void toMyrelease(){
        //如果当前不是发布页
        if (!currentState){
            btn_buy.setTextColor(Color.BLACK);
            btn_release.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
            btn_release.setTextColor(Color.RED);

            currentState = true;
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView_buy.setVisibility(View.GONE);
        }else {

        }
    }

    @BindView(R2.id.btn_myrelease_buy)
    AppCompatTextView btn_buy = null;
    /**
     * 求购信息
     */
    @OnClick(R2.id.btn_myrelease_buy)
    void toMyBuy(){
        //如果当前是发布页
        if (currentState){
            btn_buy.setTextColor(Color.RED);
            btn_buy.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
            btn_release.setTextColor(Color.BLACK);

            currentState = false;
            recyclerView.setVisibility(View.GONE);
            recyclerView_buy.setVisibility(View.VISIBLE);
            RestClient.builder()
                    .setUrl("user/queryBuy/"+YGouPreferences.getCustomAppProfile("userId"))
                    .onSuccess(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            JSONObject jsonObject = JSONObject.parseObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("ok")){
                                Badapter = new MyBuyAdapter(new MyBuyDataConvert().setsJsonData(response).convert(),Blisenter);
                                recyclerView_buy.setAdapter(Badapter);
                            }
                        }
                    })
                    .build()
                    .get();
        }else {

        }
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_my_release;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView_buy.setLayoutManager(manager);
        Rlisenter=this;
        Blisenter = this;

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {

        RestClient.builder()
                .setUrl("user/queryRelease/"+ YGouPreferences.getCustomAppProfile("userName"))
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        JSONObject jsonObject = JSONObject.parseObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("ok")){
                            Radapter = new MyReleaseAdapter(new MyReleaseDataConvert().setsJsonData(response).convert(),Rlisenter);
                            recyclerView.setAdapter(Radapter);
                            String userImg = YGouPreferences.getCustomAppProfile("userImg");
                            Glide.with(getContext())
                                    .load(userImg)
                                    .into(user_img);
                        }else {
                            ll_release_null.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .build()
                .get();
    }

    /**
     * 修改我的求购
     */
    @Override
    public void editBuy(int position) {
        Toast.makeText(getContext(),"编辑求购",Toast.LENGTH_SHORT).show();
        MultipleitemEntity item = Badapter.getItem(position);
        int id = item.getField(MultipleFields.ID);
        getSupportDelegate().start(EditBuyDelegate.create(id));
        Toast.makeText(getContext(),"修改",Toast.LENGTH_SHORT).show();


    }

    /**
     * 删除我的求购
     */
    @Override
    public void deleteBuy(int position) {

        Toast.makeText(getContext(),"编辑求购",Toast.LENGTH_SHORT).show();
        MultipleitemEntity item = Badapter.getItem(position);

        int id = item.getField(MultipleFields.ID);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("是否删除求购");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确认
                if (which == 0) {
                    RestClient.builder()
                            .setUrl("release/delete/"+id)
                            .onSuccess(new ISuccess() {
                                @Override
                                public void onSuccess(String response) {
                                    String status = JSONObject.parseObject(response).getString("status");
                                    if (status.equals("ok")){
                                        Badapter.remove(position);
                                        Badapter.notifyDataSetChanged();
                                    }
                                }
                            })
                            .build()
                            .get();
                    dialog.dismiss();
                }
                //取消
                else {
                    dialog.dismiss();
                }
            }
        }).create().show();

    }

    /**
     * 删除在售的商品
     * @param position
     */
    @Override
    public void deleteOnsell(int position) {

        Toast.makeText(getContext(),"删除订单位置*******"+position,Toast.LENGTH_SHORT).show();
        MultipleitemEntity item = Radapter.getItem(position);
        //发布的商品的id
        int goods_id = item.getField(MultipleFields.ID);

        Log.i("goods_id", "deleteOnsell: "+goods_id);
        //弹出提示框删除
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("是否下架商品");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确认
                if (which == 0) {
                    //删除商品
                    RestClient.builder()
                            .setUrl("goodsinfo/delete/" + goods_id)
                            .onSuccess(new ISuccess() {
                                @Override
                                public void onSuccess(String response) {
                                    String status = JSONObject.parseObject(response).getString("status");
                                    if (status.equals("ok")) {
                                        Radapter.remove(position);
                                        Radapter.notifyDataSetChanged();
                                    }
                                }
                            })
                            .build()
                            .get();
                    Log.i("下架商品", "onClick: ");
                         dialog.dismiss();
                }
                //取消
                else {
                    dialog.dismiss();
                }
            }
        }).create().show();

    }

    /**
     * 删除已完结的订单
     * @param position
     */
    @Override
    public void deleteOnorder(int position) {
        MultipleitemEntity item = Radapter.getItem(position);
        int orderId = item.getField(MultipleFields.ID);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("是否删除订单");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确认
                if (which == 0) {
                    RestClient.builder()
                            .setUrl("order/delete/"+orderId)
                            .onSuccess(new ISuccess() {
                                @Override
                                public void onSuccess(String response) {
                                    String status = JSONObject.parseObject(response).getString("status");
                                    if (status.equals("ok")){
                                        Radapter.remove(position);
                                        Radapter.notifyDataSetChanged();
                                    }
                                }
                            })
                            .build()
                            .get();
                    dialog.dismiss();
                }
                //取消
                else {
                    dialog.dismiss();
                }
            }
        }).create().show();
    }

    /**
     * 确认发货
     * @param position
     */
    @Override
    public void send(int position) {
        MultipleitemEntity item = Radapter.getItem(position);
        //获得订单id
        int orderId = item.getField(MultipleFields.ID);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("是否确认发货");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确认
                if (which == 0) {
                    RestClient.builder()
                            .setUrl("order/updateTagToSendById/"+orderId)
                            .onSuccess(new ISuccess() {
                                @Override
                                public void onSuccess(String response) {
                                    Log.i("response", "onSuccess: "+response);
                                        Toast.makeText(getContext(),"发送成功",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .build()
                            .get();

                    dialog.dismiss();
                }
                //取消
                else {
                    dialog.dismiss();
                }
            }
        }).create().show();
        Toast.makeText(getContext(),"发货",Toast.LENGTH_SHORT).show();


    }

    /**
     * 提醒收货
     * @param position
     */
    @Override
    public void receive(int position) {
        MultipleitemEntity item = Radapter.getItem(position);
        //获得订单持有者名字
        String  user_name = item.getField(OrderItemFields.NAME);

        JMessageClient.enterSingleConversation(YGouApp.APP_NAME+user_name);
        Log.i("订单持有者的名字", "receive: "+user_name);
        //发送商品标题
        String goods_name = item.getField(MultipleFields.TITLE);
        //商品图片地址
        String img = item.getField(MultipleFields.IMAGE_URL);

        Toast.makeText(getContext(),"收货",Toast.LENGTH_SHORT).show();
        //给用户发消息提醒收获
        //创建会话
        Conversation singleConversation = Conversation.createSingleConversation(YGouApp.APP_NAME + user_name);
        if (singleConversation==null){
            Log.i("conversation", "onCreate: *****************null");
            Toast.makeText(getContext(),"消息发送失败",Toast.LENGTH_SHORT).show();
        }else {
            String targetId = singleConversation.getTargetId();
            Log.i("目标消息对象名字是", "receive: "+targetId);
            //创建消息 将商品名称传过去
            TextContent textContent = new TextContent("你购买的商品：<"+goods_name+">已发货,记得签收噢！");
            Message sendMessage = singleConversation.createSendMessage(textContent);
            sendMessage.getFromUser();
            sendMessage.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i==0){
                        //发送成功
                        Toast.makeText(getContext(),"发送成功",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            JMessageClient.sendMessage(sendMessage);
        }
    }

    /**
     * 提醒评价
     * @param position
     */
    @Override
    public void evl(int position) {
        Toast.makeText(getContext(),"评价",Toast.LENGTH_SHORT).show();
        MultipleitemEntity item = Radapter.getItem(position);
        //获得订单持有者名字
        String  user_name = item.getField(OrderItemFields.NAME);
        //发送商品标题
        String goods_name = item.getField(MultipleFields.TITLE);
        //商品图片地址
        String img = item.getField(MultipleFields.IMAGE_URL);

        Toast.makeText(getContext(),"收货",Toast.LENGTH_SHORT).show();
        //给用户发消息提醒收获
        //创建会话
        Conversation singleConversation = Conversation.createSingleConversation(YGouApp.APP_NAME + user_name);
        if (singleConversation==null){
            Log.i("conversation", "onCreate: *****************null");
            Toast.makeText(getContext(),"消息发送失败",Toast.LENGTH_SHORT).show();
        }else {
            //创建消息 将商品名称传过去
            TextContent textContent = new TextContent("你对购买的商品：<"+goods_name+">还满意吗?,记得评价五星好评噢！");
            Message sendMessage = singleConversation.createSendMessage(textContent);
            sendMessage.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i==0){
                        //发送成功
                        Toast.makeText(getContext(),"发送成功",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            JMessageClient.sendMessage(sendMessage);
        }
    }

    /**
     * 查看评价
     * @param position
     */
    @Override
    public void look(int position) {
        Toast.makeText(getContext(),"查看",Toast.LENGTH_SHORT).show();
        MultipleitemEntity item = Radapter.getItem(position);
        //获得订单id
        int orderId = item.getField(MultipleFields.ID);
        final ShowOrderDetailDelegate detailDelegate =new ShowOrderDetailDelegate();
        Bundle bundle = new Bundle();
        bundle.putString(OrderListDelegate.ORDER_ID,String.valueOf(orderId));
        detailDelegate.setArguments(bundle);
        Log.i("goodsId:", ""+orderId+"****position:"+position);
        getSupportDelegate().start(detailDelegate);
    }

}
