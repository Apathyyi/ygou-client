package sy.bishe.ygou.delegate.sort;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.recycler.BaseDecoration;

/**
 * 求购
 */
public class ForBuyDelegate extends YGouDelegate {


    @BindView(R2.id.show_buy_rv)
    RecyclerView rv_buy = null;

    /**
     * 返回
     */
    @OnClick(R2.id.show_buy_back)
    void back(){
        getFragmentManager().popBackStack();
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_show_buy;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rv_buy.setLayoutManager(manager);
        final YGouDelegate delegate = this;
        rv_buy.addOnItemTouchListener(ForBuyClickLisenter.create(delegate));
        rv_buy.addItemDecoration(BaseDecoration.create(ContextCompat.getColor(getContext(),R.color.app_background),1));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {

        RestClient.builder()
                .setUrl("release/query")
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        String status = jsonObject.getString("status");
                        if (!status.isEmpty()&&status.equals("ok")){
                            ForBuyAdapter forBuyAdapter = new ForBuyAdapter(new ForBuyDataConvert().setsJsonData(response).convert());
                            rv_buy.setAdapter(forBuyAdapter);
                        }
                    }
                })
                .build()
                .get();
    }
}
