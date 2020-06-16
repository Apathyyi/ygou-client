package sy.bishe.ygou.delegate.friends.contanct;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class AddFriendDelegate  extends YGouDelegate implements AddFriendLisenter{

    private AddFriendLisenter addFriendLisenter = this; // 点击监听
    @BindView(R2.id.title_bar_title)
    AppCompatTextView tv_title = null;

    @BindView(R2.id.pull_msg_rv)
    RecyclerView rv_notify = null;

    @BindView(R2.id.pull_msg_rv2)
    RecyclerView rv_recommend = null;

    @BindView(R2.id.title_bar_img)
    AppCompatImageView img = null;


    @BindView(R2.id.title_options_img)
    AppCompatImageView img_option =  null;



    private MsgNotifyAdpater notifyAdpater = null;//好友通知适配

    private MsgrecommendAdpater recommendAdpater = null;//好友推荐适配


    /**
     * 返回
     */
    @OnClick(R2.id.title_bar_back)
    void back(){
        getFragmentManager().popBackStack();
    }

    /**
     * 添加
     */
    @OnClick(R2.id.title_options_img)

    void addFriend(){
        getSupportDelegate().start(new AddFriendMsgDelegate());
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_newfriend;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        tv_title.setText("新的好友");
        tv_title.setTextSize(18);
        img.setVisibility(View.GONE);
        img_option.setVisibility(View.VISIBLE);

        LinearLayoutManager notify = new LinearLayoutManager(getContext());
        rv_notify.setLayoutManager(notify);
        LinearLayoutManager recommned = new LinearLayoutManager(getContext());
        rv_recommend.setLayoutManager(recommned);
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
        rv_notify.addItemDecoration(itemDecoration);
        rv_recommend.addItemDecoration(itemDecoration);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RestClient.builder()
                .setUrl("friend/notify/"+ YGouPreferences.getCustomAppProfile("userId"))
               // .setUrl("notify.json")
                .loader(getContext())
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response", "onSuccess: "+response);
                        notifyAdpater = new MsgNotifyAdpater(new NotifyDataConvert().setsJsonData(response).convert(),addFriendLisenter);
                        rv_notify.setAdapter(notifyAdpater);
                        recommendAdpater = new MsgrecommendAdpater(new RecommendConvert().setsJsonData(response).convert(),addFriendLisenter);
                        rv_recommend.setAdapter(recommendAdpater);
                    }
                })
                .build()
                .get();
    }

    /**
     * 同意
     * @param friend_id
     */
    @Override
    public void agree(int friend_id) {
        //同意成为好友
        Log.i("同意添加", "agree: "+friend_id);
        RequestBody requestBody = RequestBody.create(YGouApp.JSON, JSONObject.toJSONString(friend_id));
        RestClient.builder()
                .setUrl("friend/addAgree/"+YGouPreferences.getCustomAppProfile("userId"))
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("返回情况", "onSuccess: "+response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        String status = jsonObject.getString("status");
                        if (status!=null&&status.equals("ok")){
                            Toast.makeText(getContext(),"已加为好友",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .build()
                .post();

    }

    /**
     * 添加
     * @param friend_id
     */
    @Override
    public void add(int friend_id) {
        //添加请求
        Log.i("添加请求", "agree: "+friend_id);
        RequestBody requestBody = RequestBody.create(YGouApp.JSON, JSONObject.toJSONString(friend_id));
        RestClient.builder()
                .setBody(requestBody)
                .setUrl("friend/updateToAdd/"+YGouPreferences.getCustomAppProfile("userId"))
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("返回情况", "onSuccess: "+response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        String status = jsonObject.getString("status");
                        if (status!=null&&status.equals("ok")){
                            Toast.makeText(getContext(),"请求成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .build()
                .post();
    }

    @Override
    public void addByName(String name) {


    }
}
