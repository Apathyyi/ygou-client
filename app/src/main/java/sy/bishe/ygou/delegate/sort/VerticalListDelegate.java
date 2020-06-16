package sy.bishe.ygou.delegate.sort;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;


//左侧竖直栏
public class VerticalListDelegate extends YGouDelegate {

    @BindView(R2.id.rv_vertical_menu_list)
    RecyclerView sRecycleView = null;
    @Override
    public Object setLayout() {
        return R.layout.delegate_vertical_list;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        initRecycleView();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RestClient.builder()
                .setUrl("sortvertical/query")
                .loader(getContext())
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("SORT",response);
                        final List<MultipleitemEntity> data = new VerticalDataConverter().setsJsonData(response).convert();
                        final SortDlegate delegate = getParentDelegate();
                        final SortRecyclerAdapter adapter = new SortRecyclerAdapter(data,delegate);
                        sRecycleView.setAdapter(adapter);
                    }
                })
                .build()
                .get();
    }

    private void initRecycleView(){
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        sRecycleView.setLayoutManager(manager);
        sRecycleView.setItemAnimator(null);
    }
}
