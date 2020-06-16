package sy.bishe.ygou.delegate.search;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.delegate.sort.SortDetailDelegate;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class HistoriLisenter extends SimpleClickListener {
    private YGouDelegate DELEGATE;


    public HistoriLisenter(YGouDelegate delegate) {
        this.DELEGATE = delegate;
    }
    public static HistoriLisenter create(YGouDelegate delegate){

        return new HistoriLisenter(delegate);
    }
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Log.i("数据", "onItemClick: "+baseQuickAdapter.getData().get(position));
        MultipleitemEntity entity  = (MultipleitemEntity) baseQuickAdapter.getData().get(position);
        String text = entity.getField(MultipleFields.TEXT);
        Log.i("数据", "onItemClick: "+baseQuickAdapter.getData().get(position)+"ID*********"+text);
        DELEGATE.getSupportDelegate().start(SortDetailDelegate.create(-1,text));
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
