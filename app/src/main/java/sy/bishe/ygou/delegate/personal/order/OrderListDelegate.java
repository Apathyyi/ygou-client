package sy.bishe.ygou.delegate.personal.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.buttons.ButtonDelegate;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.delegate.personal.PersonalDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class OrderListDelegate extends YGouDelegate implements OrderTagLisenter{

    public static final String ORDER_ID = "ORDER_ID";

    Bundle bundle = new Bundle();

    private OrderTagLisenter orderTagLisenter = null;

    private OrderListAdapter adapter = null;

    private static final String ARG_ORDER_TYPE = "ARG_ORDER_TYPE";

    //订单类型
    private String sType = null;

    @BindView(R2.id.rv_order_list)
    RecyclerView sRecycleView = null;

    final CharSequence[] items = {"确认", "取消"};

    @BindView(R2.id.ll_go_shop)
    LinearLayout ll_shop = null;

    @OnClick(R2.id.go_shop)
   void goShop(){
        getSupportDelegate().startWithPop(new ButtonDelegate());
        Log.i("当前", "goShop: "+this.toString());
      //  Log.i("当前父类", "goShop: "+this.getParentDelegate().toString());   null
      //  Log.i("父类", "goShop: "+getParentDelegate().toString()); null
        Log.i("当前支持", "goShop: "+this.getSupportDelegate().toString());

        Log.i("支持", "goShop: "+getSupportDelegate().toString());
        Log.i("栈顶", "goShop: "+getTopFragment());

    }

    @OnClick(R2.id.order_back)
    void OnclickBack(){
        getFragmentManager().popBackStack();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_order_list;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        sType = args.getString(PersonalDelegate.ORDER_TYPE);
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        sRecycleView.setLayoutManager(manager);
        orderTagLisenter = this;

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RequestBody requestBody = RequestBody.create(YGouApp.JSON,sType);
        RestClient.builder()
                .setUrl("order/query/"+ YGouPreferences.getCustomAppProfile("userId"))
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        Log.i("response", "onSuccess: "+response);
                        final List<MultipleitemEntity> entities = new OrderListDataConverter().setsJsonData(response).convert();
                        if (entities.size()==0){
                            ll_shop.setVisibility(View.VISIBLE);
                        }else {
                            ll_shop.setVisibility(View.GONE);
                        }
                        adapter = new OrderListAdapter(entities,orderTagLisenter);
                        sRecycleView.setAdapter(adapter);
                    }
                })
                .build()
                .post();
    }

    //催发货
    @Override
    public void send(int position) {
        Toast.makeText(getContext(),"提醒发货",Toast.LENGTH_SHORT).show();


        MultipleitemEntity item = adapter.getItem(position);
        //获得发布者名字
        String  relese_name = item.getField(OrderItemFields.RELEASENAME);
        JMessageClient.enterSingleConversation(YGouApp.APP_NAME+relese_name);


        Log.i("订单持有者的名字", "receive: "+relese_name);
        //发送商品标题
        String goods_name = item.getField(MultipleFields.TITLE);
        //商品图片地址
        String img = item.getField(MultipleFields.IMAGE_URL);

        Toast.makeText(getContext(),"发货提醒",Toast.LENGTH_SHORT).show();
        //给用户发消息提醒收获
        //创建会话
        Conversation singleConversation = Conversation.createSingleConversation(YGouApp.APP_NAME + relese_name);
        if (singleConversation==null){
            Log.i("conversation", "onCreate: *****************null");
            Toast.makeText(getContext(),"消息发送失败",Toast.LENGTH_SHORT).show();
        }else {
            String targetId = singleConversation.getTargetId();
            Log.i("目标消息对象名字是", "receive: "+targetId);
            //创建消息 将商品名称传过去
            TextContent textContent = new TextContent("我购买的商品：<"+goods_name+">还没发货,快点噢！");
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

    //确认收货
    @Override
    public void receive(int position) {
        Toast.makeText(getContext(),"确认收货",Toast.LENGTH_SHORT).show();
        MultipleitemEntity item = adapter.getItem(position);
        //获得订单id
        String orderId = item.getField(MultipleFields.ID);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("是否确认收货");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确认
                if (which == 0) {
                    //修改订单状态
                    RestClient.builder()
                            .setUrl("order/updateTagToReceiveById/"+orderId)
                            .onSuccess(new ISuccess() {
                                @Override
                                public void onSuccess(String response) {
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

    //去评价
    @Override
    public void evl(int position) {
        Toast.makeText(getContext(),"去评价",Toast.LENGTH_SHORT).show();
        MultipleitemEntity item = adapter.getItem(position);
        //获得订单id
        String orderId = item.getField(MultipleFields.ID);
        Log.i("商品列表传入的ID", "evl: "+orderId);
        bundle.putString(ORDER_ID,orderId);
        final EvlDelegate delegate = new EvlDelegate();
        delegate.setArguments(bundle);
        getSupportDelegate().start(delegate);

    }

    //删除订单
    @Override
    public void delete(int position) {
        Toast.makeText(getContext(),"删除",Toast.LENGTH_SHORT).show();
        MultipleitemEntity item = adapter.getItem(position);
        String orderId = item.getField(MultipleFields.ID);
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
                                        adapter.remove(position);
                                        adapter.notifyDataSetChanged();
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
    //查看评价
    @Override
    public void look(int position) {
        Toast.makeText(getContext(),"查看",Toast.LENGTH_SHORT).show();
        MultipleitemEntity item = adapter.getItem(position);
        //获得订单id
        String orderId = item.getField(MultipleFields.ID);

        bundle.putString(ORDER_ID,orderId);
        final ShowOrderDetailDelegate delegate = new ShowOrderDetailDelegate();
        delegate.setArguments(bundle);

        getSupportDelegate().start(delegate);

    }
}
