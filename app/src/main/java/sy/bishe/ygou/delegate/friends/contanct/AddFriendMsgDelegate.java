package sy.bishe.ygou.delegate.friends.contanct;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
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

public class AddFriendMsgDelegate extends YGouDelegate implements TextWatcher ,AddFriendLisenter {



    private AddFriendLisenter lisenter = this;

    /**
     * 返回
     */
    @OnClick(R2.id.addfriend_back)
        void back(){
            getFragmentManager().popBackStack();
    }


    AddFriendAdapter addFriendAdapter = null;

    @BindView(R2.id.addfriend_search)
    AppCompatEditText edit_search = null;


    @BindView(R2.id.btn_addfriend_serach)
    AppCompatTextView tv_search = null;


    @OnClick(R2.id.btn_addfriend_serach)
    void search(){
        String name = edit_search.getText().toString();
        Log.i("搜索的名字是", "search: "+name);
        RestClient
                .builder()
                .setUrl("user/queryByName/"+name)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        String status = jsonObject.getString("status");
                        if (status!=null&&status.equals("ok")){
                            addFriendAdapter = new AddFriendAdapter(new AddFriendDataConvert().setsJsonData(response).convert(),lisenter);
                            rv_add.setAdapter(addFriendAdapter);
                            addFriendAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .build()
                .get();

    }

    @BindView(R2.id.rv_addfriend)
    RecyclerView rv_add = null;


    @Override
    public Object setLayout() {
        return R.layout.delegate_add_msg;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_add.setLayoutManager(linearLayoutManager);

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
        rv_add.addItemDecoration(itemDecoration);
       // rv_add.addItemDecoration(BaseDecoration.create(ContextCompat.getColor(getContext(),R.color.app_background),4));
        edit_search.addTextChangedListener(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.i("发生变化", "onTextChanged: "+s);
        RestClient
                .builder()
                .setUrl("user/queryByLikeName/"+s)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        String status = jsonObject.getString("status");
                        if (status!=null&&status.equals("ok")){
                            addFriendAdapter = new AddFriendAdapter(new AddFriendDataConvert().setsJsonData(response).convert(),lisenter);
                            rv_add.setAdapter(addFriendAdapter);
                            addFriendAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .build()
                .get();

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void agree(int friend_id) {

    }

    @Override
    public void add(int friend_id) {

    }

    @Override
    public void addByName(String name) {
        RequestBody requestBody = RequestBody.create(YGouApp.JSON,name);
        RestClient.builder()
                .setUrl("user/addFriend/"+ YGouPreferences.getCustomAppProfile("userId"))
                .setBody(requestBody)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        String status = jsonObject.getString("status");
                        if (status!=null&&status.equals("ok")){
                            Toast.makeText(getContext(),"已发送请求",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .build()
                .post();
    }
}
