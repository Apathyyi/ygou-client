package sy.bishe.ygou.delegate.personal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.friends.chat.ChatDelegate;
import sy.bishe.ygou.delegate.friends.contanct.ContanctDelegate;
import sy.bishe.ygou.delegate.friends.dynamic.DynamicDelegate;

//消息
public class FriendDelegate extends YGouDelegate {


    @BindView(R2.id.tv_message_total_amount)
    AppCompatTextView tv_message_count = null;

    @OnClick(R2.id.friend_back)
    void back(){
        getFragmentManager().popBackStack();
    }

    /**
     * 消息界面
     */
    @OnClick(R2.id.ll_to_message)
    void toMessage(){
        tv_message_count.setVisibility(View.GONE);
        getSupportDelegate().start(new ChatDelegate());
    }

    /**
     * 好友界面
     * @return
     */
    @OnClick(R2.id.ll_to_friend)
    void toFriend(){
       getSupportDelegate().start(new ContanctDelegate());
    }

    /**
     * 动态界面
     * @return
     */
    @OnClick(R2.id.ll_to_dynamic)
    void toDynamic(){
       getSupportDelegate().start(new DynamicDelegate());
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_find_choose;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        //消息列表数  有多少人是有未读消息的
        List<Conversation> conversationList = JMessageClient.getConversationList();
        int counts = 0;
        for (Conversation c:conversationList
        ) {
            if (c.getUnReadMsgCnt()!=0){
                counts ++;
            }
        }
        Log.i("未读消息数", "onLazyInitView: "+counts);
        if (counts==0){
            tv_message_count.setVisibility(View.GONE);
        }else {
            tv_message_count.setVisibility(View.VISIBLE);
            tv_message_count.setText(String.valueOf(counts));
        }
    }
}
