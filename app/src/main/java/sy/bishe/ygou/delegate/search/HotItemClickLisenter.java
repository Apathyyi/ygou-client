package sy.bishe.ygou.delegate.search;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.delegate.sort.SortDetailDelegate;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class HotItemClickLisenter extends SimpleClickListener {

    private YGouDelegate DELEGATE;


    public HotItemClickLisenter(YGouDelegate delegate) {
        this.DELEGATE = delegate;
    }
    public static HotItemClickLisenter create(YGouDelegate delegate){

        return new HotItemClickLisenter(delegate);
    }
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        MultipleitemEntity entity  = (MultipleitemEntity) baseQuickAdapter.getData().get(position);
        String name = entity.getField(MultipleFields.NAME);
        Log.i("数据", "onItemClick: "+name);
        DELEGATE.getSupportDelegate().start(SortDetailDelegate.create(0,name));
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
