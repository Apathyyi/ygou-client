package sy.bishe.ygou.delegate.index;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.delegate.detail.GoodsDetailDelegate;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class IndexItemClickListener extends SimpleClickListener {
    private final YGouDelegate DELEGATE;

    public IndexItemClickListener(YGouDelegate delegate) {
        this.DELEGATE = delegate;
    }
    public static SimpleClickListener create(YGouDelegate delegate){
        return new IndexItemClickListener(delegate);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //entity
        final MultipleitemEntity entity = (MultipleitemEntity) baseQuickAdapter.getData().get(position);
        final int goodsId = Integer.parseInt(entity.getField(MultipleFields.ID));
        final GoodsDetailDelegate detailDelegate = GoodsDetailDelegate.create(goodsId);
        Log.i("goodsId:", ""+goodsId+"****position:"+position);
        DELEGATE.getSupportDelegate().start(detailDelegate);
//        final GoodsDetailDelegate detailDelegate = new GoodsDetailDelegate();
//        DELEGATE.start(detailDelegate);
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
//        //entity
//        final MultipleitemEntity entity = (MultipleitemEntity) baseQuickAdapter.getData().get(position);
//        final int goodsId = Integer.parseInt(entity.getField(MultipleFields.ID));
//        Log.i("goodsId:", ""+goodsId+"****position:"+position);
//        final GoodsDetailDelegate detailDelegate = GoodsDetailDelegate.create(goodsId);
//        detailDelegate.getSupportDelegate().start(detailDelegate);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
