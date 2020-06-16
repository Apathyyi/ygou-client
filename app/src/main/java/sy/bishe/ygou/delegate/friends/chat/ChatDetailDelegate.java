package sy.bishe.ygou.delegate.friends.chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.bean.MyMessage;
import sy.bishe.ygou.delegate.base.YGouDelegate;

public class ChatDetailDelegate extends YGouDelegate {

    private static  final String ARG_FRIEND_NAME = "ARG_FRIEND_NAME";  //聊天对象名字
    private static  String sUserName ;

    private ChatDetailAdpater jMessageAdapter = null; //消息列表适配器

   private  Conversation conversation = null; //会话

    List<MyMessage> list = new ArrayList<>(); //消息列表

    @BindView(R2.id.tv_target_account)
    AppCompatTextView tv_target_account = null;

    @BindView(R2.id.recyc)
    RecyclerView recyclerView = null;
    @BindView(R2.id.et_send)
    AppCompatEditText edt_message = null;

    /**
     * 点击输入框
     */
    @OnClick(R2.id.layout_edit)
    void onLcickedit(){
        edt_message.requestFocus();
        showSoftInput(getContext(),edt_message);
        new Handler().sendEmptyMessageDelayed(0,250);
    }

    /**
     * 返回
     */
    @OnClick(R2.id.chatDetail_back)
         void back(){
            getFragmentManager().popBackStack();
    }

    /**
     * x显示键盘
     * @param context
     * @param view
     */

    public  void showSoftInput(Context context, View view) {
        recyclerView.scrollToPosition(jMessageAdapter.getItemCount()-1);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view,0);
    }

    //隐藏键盘
    public  void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    /**
     * 发送消息
     */
    @OnClick(R2.id.btn_message_send)
    void sendMessage(){
        String content = edt_message.getText().toString();
        Message m = conversation.createSendMessage(new TextContent(content));
        m.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0){
                    Log.i("gotResult", "gotResult: "+"发送成功");

                }else {
                    Log.i("gotResult", "gotResult: "+"发送失败");
                }
            }
        });
        JMessageClient.sendMessage(m);
        edt_message.setText("");
        MyMessage myMessage = new MyMessage();
        long createTime = m.getCreateTime();
        String fromID = m.getFromID();
        String type = m.getContentType().toString();
        Date date = new Date(createTime);
        int hours = date.getHours();
        int minutes = date.getMinutes();
        String time = "" + hours + ":" + minutes;
        String targetID = m.getTargetID();
        myMessage.setTarget_name(targetID);
        myMessage.setFrom_name(fromID);
        myMessage.setTime(time);
        myMessage.setType(type);
        MessageContent c = m.getContent();
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(c);
        String text = jsonObject.getString("text");
        myMessage.setText(text);
        Log.i("MYMESSGE", "onLazyInitView: " + myMessage.toString());
        list.add(myMessage);
        refresh();
    }

    /**
     * 建筑者
     * @param sUserName
     * @return
     */
    public static ChatDetailDelegate create(@NonNull String sUserName) {
        final Bundle bundle = new Bundle();
        bundle.putString(ARG_FRIEND_NAME,sUserName);
        final ChatDetailDelegate detailDelegate = new ChatDetailDelegate();
        detailDelegate.setArguments(bundle);
        return detailDelegate;
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_chat_detail;
    }
    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        tv_target_account.setText(sUserName);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
//        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        View decorView = _mActivity.getWindow().getDecorView();
//        View contentView = _mActivity.findViewById(Window.ID_ANDROID_CONTENT);
//        decorView.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(decorView, contentView));

    }

    /**
     * 谈起键盘时顶出上面视图
     * @param decorView
     * @param contentView
     * @return
     */
    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(View decorView, View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JMessageClient.registerEventReceiver(this);
        final Bundle bundle = getArguments();
        if (bundle != null){
            sUserName = bundle.getString(ARG_FRIEND_NAME);
            Log.i("sUserName", sUserName+"");
        }
        JMessageClient.enterSingleConversation(YGouApp.APP_NAME+sUserName);
    }

    /**
     * 接受离线消息
     * @param event
     */
    public void onEvent(OfflineMessageEvent event){
        Log.i("接收离线消息", "onEvent: ");
        Conversation conversation = event.getConversation();
        List<Message> offlineMessageList = event.getOfflineMessageList();
            for (Message m : offlineMessageList
            ) {
                m.setHaveRead(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {

                    }
                });
                MyMessage myMessage = new MyMessage();
                long createTime = m.getCreateTime();
                String fromID = m.getFromID();
                String type = m.getContentType().toString();
                Date date = new Date(createTime);
                int hours = date.getHours();
                int minutes = date.getMinutes();
                String time = "" + hours + ":" + minutes;
                String targetID = m.getTargetID();
                myMessage.setTarget_name(targetID);
                myMessage.setFrom_name(fromID);
                myMessage.setTime(time);
                myMessage.setType(type);
                MessageContent content = m.getContent();
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(content);
                String text = jsonObject.getString("text");
                myMessage.setText(text);
                Log.i("MYMESSGE", "onLazyInitView: " + myMessage.toString());
                list.add(myMessage);
            }
        refresh();
    }

    /**
     * 接受在线消息
     * @param event
     */
    public void onEvent(MessageEvent event){
        Log.i("接收在线消息", "onEvent: ");
        Message m = event.getMessage();
        MyMessage myMessage = new MyMessage();
        long createTime = m.getCreateTime();
        String fromID = m.getFromID();
        String type = m.getContentType().toString();
        Date date = new Date(createTime);
        int hours = date.getHours();
        int minutes = date.getMinutes();
        String time = "" + hours + ":" + minutes;
        String targetID = m.getTargetID();
        myMessage.setTarget_name(targetID);
        myMessage.setFrom_name(fromID);
        myMessage.setTime(time);
        myMessage.setType(type);
        MessageContent content = m.getContent();
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(content);
        String text = jsonObject.getString("text");
        myMessage.setText(text);
        list.add(myMessage);
        jMessageAdapter.getData().clear();
        jMessageAdapter.getData().addAll(new ChatDetailDataConvert().setsJsonData(JSON.toJSONString(list)).convert());
        jMessageAdapter.notifyItemRangeChanged(0,jMessageAdapter.getItemCount());
    }

    /**
     * s刷新
     */

    private void refresh() {
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                jMessageAdapter.getData().clear();
                jMessageAdapter.getData().addAll(new ChatDetailDataConvert().setsJsonData(JSON.toJSONString(list)).convert());
                jMessageAdapter.notifyItemRangeChanged(0,jMessageAdapter.getItemCount());
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        conversation = Conversation.createSingleConversation(YGouApp.APP_NAME+sUserName);
        if (conversation==null){
            Log.i("conversation", "onCreate: *****************null");
        }
        if (conversation!=null) {
            Log.d("conversation", conversation.getTitle());
            List<Message> allMessage = conversation.getAllMessage();
            Log.i("消息条数", "" + allMessage.size());
            for (Message m : allMessage
            ) {
                m.haveRead();
                MyMessage myMessage = new MyMessage();
                long createTime = m.getCreateTime();
                String fromID = m.getFromID();
                String type = m.getContentType().toString();
                Date date = new Date(createTime);
                int hours = date.getHours();
                int minutes = date.getMinutes();
                String time = "" + hours + ":" + minutes;
                String targetID = m.getTargetID();
                myMessage.setTarget_name(targetID);
                myMessage.setFrom_name(fromID);
                myMessage.setTime(time);
                myMessage.setType(type);
                MessageContent content = m.getContent();
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(content);
                String text = jsonObject.getString("text");
                myMessage.setText(text);
                Log.i("MYMESSGE", "onLazyInitView: " + myMessage.toString());
                list.add(myMessage);
            }
            Log.i("getSupportDelegate父类界面", "onLazyInitView: *********************"+getParentDelegate());
            jMessageAdapter = new ChatDetailAdpater(new ChatDetailDataConvert().setsJsonData(JSON.toJSONString(list)).convert(),getSupportDelegate());
            recyclerView.setAdapter(jMessageAdapter);
            recyclerView.scrollToPosition(jMessageAdapter.getItemCount()-1);
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        JMessageClient.exitConversation();
        //销毁
        JMessageClient.unRegisterEventReceiver(this);
    }
}
