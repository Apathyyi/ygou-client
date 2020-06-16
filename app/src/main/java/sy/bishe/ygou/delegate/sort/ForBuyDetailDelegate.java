package sy.bishe.ygou.delegate.sort;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.friends.chat.ChatDetailDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;

public class ForBuyDetailDelegate extends YGouDelegate {

    @BindView(R2.id.show_buy_detail_title)
    AppCompatTextView tv_title;

    @BindView(R2.id.show_buy_detail_desc)
    AppCompatTextView tv_desc;

    @BindView(R2.id.show_buy_detail_price)
    AppCompatTextView tv_price;

    @BindView(R2.id.show_buy_detail_contact)
    AppCompatTextView tv_contact;

    @BindView(R2.id.show_buy_detail_name)
    AppCompatTextView tv_name;


    @BindView(R2.id.show_buy_detail_time)
    AppCompatTextView tv_time;

    @BindView(R2.id.show_buy_detail_img)
    CircleImageView imageView;

    /**
     * 返回
     */
    @OnClick(R2.id.show_buy_detail_send)
    void send(){
        getSupportDelegate().start(ChatDetailDelegate.create(tv_name.getText().toString()));
    }

    private static final String ARG_BUY_ID = "ARG_BUY_ID";
    private int sId = -1;

    public static ForBuyDetailDelegate create(int buy_id) {

        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_BUY_ID,buy_id);
        final ForBuyDetailDelegate detailDelegate = new ForBuyDetailDelegate();
        detailDelegate.setArguments(bundle);
        return detailDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        if (bundle != null){
            sId = bundle.getInt(ARG_BUY_ID);
            Log.i("sGoodsId", sId+"");
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.show_buy_detail;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {

        RestClient.builder()
                .setUrl("release/queryById/"+sId)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response);

                        String status = jsonObject.getString("status");
                        if (!status.isEmpty()&&status.equals("ok")){

                            JSONObject data = jsonObject.getJSONObject("data");

                            String release_title = data.getString("release_title");
                            String release_desc = data.getString("release_desc");
                            String release_price = data.getString("release_price");
                            String release_contact = data.getString("release_contact");
                            String user_img = data.getString("release_user_img");
                            String release_time = data.getString("release_time");

                            tv_title.setText(release_title);

                            tv_desc.setText(release_desc);
                            tv_price.setText(release_price);
                            tv_contact.setText(release_contact);
                            tv_time.setText(release_time);

                            Glide.with(getContext())
                                    .load(user_img)
                                    .into(imageView);

                            JSONObject userName = jsonObject.getJSONObject("jsonObject");
                            String name= userName.getString("userName");
                            tv_name.setText(name);


                        }
                    }
                })
                .build()
                .get();

    }
}
