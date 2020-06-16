package sy.bishe.ygou.delegate.friends.chat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class ChatDelegate extends YGouDelegate {

    ChatAdapter sAdapter = null;

    @BindView(R2.id.list_more_rv)
    RecyclerView sRecyclerView = null;


    @OnClick(R2.id.title_bar_back)
    void back(){
        getFragmentManager().popBackStack();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_friend_main_root;
    }
    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        sRecyclerView.setLayoutManager(manager);
        final DividerItemDecoration itemDecoration = new DividerItemDecoration();
        itemDecoration.setDividerLookup(new DividerItemDecoration.DividerLookup() {
            @Override
            public Divider getVerticalDivider(int position) {
                return null;
            }
            @Override
            public Divider getHorizontalDivider(int position) {
                return new Divider.Builder()
                        .size(2)
                        .margin(20,20)
                        .color(Color.GRAY)
                        .build();
            }
        });
        sRecyclerView.addItemDecoration(itemDecoration);
        final YGouDelegate yGouDelegate = this;
        sRecyclerView.addOnItemTouchListener(ChatItemClicklistener.create(yGouDelegate));

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        //获取消息列表
        List<Conversation> conversationList = JMessageClient.getConversationList();
        //从列表获取名字
        List<String> userNames = new ArrayList<>();
        //内容
        List<String> texts = new ArrayList<>();
        //未读消息数
        List<Integer> counts = new ArrayList<>();
        //时间
        List<String>times = new ArrayList<>();
        for (Conversation c:conversationList
             ) {
            //未读信息数量
            int unReadMsgCnt = c.getUnReadMsgCnt();
            counts.add(unReadMsgCnt);
            //消息发送对象的id 名字
            String targetId = c.getTargetId();
            //去掉YGOU
            String name = targetId.substring(5);
            Message latestMessage = c.getLatestMessage();
            if (latestMessage == null){

            }else {
                long createTime = latestMessage.getCreateTime();
                Date date = new Date(createTime);
                int month = date.getMonth();
                int day = date.getDay();
                int hours = date.getHours();
                int minutes = date.getMinutes();
                String time = month+"-"+day+"-"+hours+":"+minutes;
                times.add(time);
                Log.i("latestMessage", "onLazyInitView: "+latestMessage);
                MessageContent content = latestMessage.getContent();
                Log.i("content", "onLazyInitView: "+content);
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(content);
                Log.i("jsonObject", "onLazyInitView: "+jsonObject.toJSONString());
                //消息内容
                String text = jsonObject.getString("text");
                texts.add(text);
                //消息名字
                userNames.add(name);
            }
        }
//        //查询信息

        JSONArray jsonArray =  JSONObject.parseArray(JSON.toJSONString(userNames));
        RequestBody requestBody = RequestBody.create(YGouApp.JSON,jsonArray.toJSONString());
        RestClient.builder()
                .setUrl("user/queryByNames")
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response", "onSuccess: "+response);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("texts",texts);
                        jsonObject.put("times",times);
                        jsonObject.put("counts",counts);
                        final JSONArray jsonArray = JSONObject.parseObject(response).getJSONArray("data");
                        jsonObject.put("datas",jsonArray);
                        final ArrayList<MultipleitemEntity> list = new ChatDataConvert().setsJsonData(jsonObject.toJSONString()).convert();
                        sAdapter = new ChatAdapter(list);
                        sRecyclerView.setAdapter(sAdapter);

                    }
                })
                .build()
                .post();

    }
}
