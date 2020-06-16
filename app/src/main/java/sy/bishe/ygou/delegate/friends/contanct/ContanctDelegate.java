package sy.bishe.ygou.delegate.friends.contanct;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;
import sy.bishe.ygou.ui.refresh.RefreshHandler;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class ContanctDelegate extends YGouDelegate {

    @BindView(R2.id.fm_contact_rv)
    RecyclerView sRecyclerView = null;

    @BindView(R2.id.fm_contact_refresh)
    SwipeRefreshLayout sSwipeRefreshLayout = null;


    @BindView(R2.id.title_bar_title)
    AppCompatTextView tv_title = null;

    @BindView(R2.id.title_bar_img)
    AppCompatImageView img = null;

    /**
     * 返回
     */

    @OnClick(R2.id.title_bar_back)
    void back(){
        getFragmentManager().popBackStack();
    }


    @BindView(R2.id.title_options_img)
    AppCompatImageView img_option =  null;

    ContactAdapter adapter = null;

    private RefreshHandler Srefreshhandler = null;

    @SuppressLint("ResourceAsColor")
    private void initrefreshlayout(){
        sSwipeRefreshLayout.setColorSchemeColors(
                Color.BLUE,
                Color.RED,
                Color.BLACK);
        sSwipeRefreshLayout.setProgressViewOffset(true,120,300);
    }

    @OnClick(R2.id.fm_contact_msg)
    void onClickNewfriend(){
       getSupportDelegate().start(new AddFriendDelegate());
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_friend_contact;
    }


    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        tv_title.setText("好友列表");
        tv_title.setTextSize(18);
        img_option.setVisibility(View.GONE);
        img.setVisibility(View.VISIBLE);

        String userImg = YGouPreferences.getCustomAppProfile("userImg");
        Glide.with(getContext())
                .load(userImg)
                .into(img);

        Srefreshhandler = RefreshHandler.create(sSwipeRefreshLayout,sRecyclerView,new ContactConvert(),getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        sRecyclerView.setLayoutManager(manager);
        //分割线
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
        sRecyclerView.addOnItemTouchListener(ContactItemClickListener.create(yGouDelegate));
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RestClient.builder()
                .setUrl("friend/query/"+ YGouPreferences.getCustomAppProfile("userId"))
                .loader(getContext())
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response", "onSuccess: "+response);
                        final ArrayList<MultipleitemEntity> list = new ContactConvert().setsJsonData(response).convert();
                        adapter = new ContactAdapter(list);
                        sRecyclerView.setAdapter(adapter);
                    }
                })
                .build()
                .get();

    }
}
