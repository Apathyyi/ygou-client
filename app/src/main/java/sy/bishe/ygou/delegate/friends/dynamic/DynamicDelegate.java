package sy.bishe.ygou.delegate.friends.dynamic;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;


/**
 * 动态  暂未实现
 */
public class DynamicDelegate extends YGouDelegate {

    public static final List<Map<String, String>> LIST_CONTETS = new ArrayList<Map<String,String>>();
    @BindView(R2.id.dynamic_rc)
    RecyclerView recyclerView = null;
    @BindView(R2.id.dynamic_img)
    AppCompatImageView img_background = null;
    @BindView(R2.id.dynamic_name)
    AppCompatTextView tv_name =  null;

    private DynamicAdpater adapter = null;
    @Override
    public Object setLayout() {
        return R.layout.delegate_dynamic;
    }
    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RestClient.builder()
                .setUrl("contents.json")
                .loader(getContext())
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("LISTVIEW", response);
                        JSONArray jsonArray = JSONObject.parseObject(response).getJSONArray("data");
                        int size = jsonArray.size();
                        for (int i = 0; i < size ; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String name = object.getString("name");
                            String toName = object.getString("toname");
                            String text = object.getString("text");
                            Map<String,String > map = new HashMap<>();
                            map.put("contents_name",name);
                            map.put("contents_to_name",toName);
                            map.put("contents_text",text);
                            LIST_CONTETS.add(map);
                        }
                    }
                })
                .build()
                .get();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RestClient.builder()
                .setUrl("dynamic.json")
                .loader(getContext())
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response", response);
                        final ArrayList<MultipleitemEntity> list = new DynamicDataConvert().setsJsonData(response).convert();
                        adapter = new DynamicAdpater(list);
                        recyclerView.setAdapter(adapter);
                    }
                })
                .build()
                .get();
    }

}
