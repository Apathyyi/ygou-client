package sy.bishe.ygou.delegate.search;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.delegate.detail.GoodsDetailDelegate;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class DataItemClickLisentner extends SimpleClickListener {

    private   YGouDelegate DELEGATE;


    public DataItemClickLisentner(YGouDelegate delegate) {
        this.DELEGATE = delegate;
    }
    public static DataItemClickLisentner create(YGouDelegate delegate){

        return new DataItemClickLisentner(delegate);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        Log.i("数据", "onItemClick: "+baseQuickAdapter.getData().get(position));
        MultipleitemEntity entity  = (MultipleitemEntity) baseQuickAdapter.getData().get(position);
        String id = entity.getField(MultipleFields.ID);
        Log.i("数据", "onItemClick: "+baseQuickAdapter.getData().get(position)+"ID*********"+id);
        DELEGATE.getSupportDelegate().start(GoodsDetailDelegate.create(Integer.parseInt(id)));
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
