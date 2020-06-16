package sy.bishe.ygou.delegate.sort;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;

import butterknife.BindView;
import sy.bishe.ygou.R;
import sy.bishe.ygou.bean.SectionBean;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.net.callback.IFailure;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;


//右边栏
public class ContentDelegate extends YGouDelegate {

    @BindView(R.id.rv_list_content)
    RecyclerView sRecycleView = null;

    private static final String ARG_CONTENT_ID = "CONTENT_ID";//传入的contentID
    private int sContentId = -1;

    private List<SectionBean> sData = null;//每个右边栏的数据

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null){
            sContentId = args.getInt(ARG_CONTENT_ID);
        }
    }

    public static ContentDelegate newInstance(int contentId){
        final Bundle args = new Bundle();
        args.putInt(ARG_CONTENT_ID,contentId);
        final ContentDelegate delegate = new ContentDelegate();
        delegate.setArguments(args);
        return delegate;
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate__list_content;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        final StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        sRecycleView.setLayoutManager(manager);
        final YGouDelegate delegate = getParentDelegate();
        sRecycleView.addOnItemTouchListener(SortItemClickListener.create(delegate));
        initData();

    }

    /**
     * 初始化
     */
    private void initData(){
        Log.i("sContentId", "initData: "+sContentId);
        RestClient.builder()
                .setUrl("sortcontent/query/"+sContentId)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("SORT_CONTENT", response);
                        sData = new SectionDataConverter().convert(response);
                        final SectionAdapter sectionAdapter = new SectionAdapter(R.layout.item_section_content,R.layout.item_sectiion_hrader,sData);
                        sRecycleView.setAdapter(sectionAdapter);
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Log.i("失败", "onFailure: ");
                    }
                })
                .build()
                .get();
    }
}
