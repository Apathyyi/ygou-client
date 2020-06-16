package sy.bishe.ygou.delegate.sort;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class ForBuyClickLisenter extends SimpleClickListener {


    private final YGouDelegate DELEGATE;

    public ForBuyClickLisenter(YGouDelegate delegate) {
        this.DELEGATE = delegate;
    }

    public static ForBuyClickLisenter create(YGouDelegate delegate){

        return new ForBuyClickLisenter(delegate);
    }
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final MultipleitemEntity entity = (MultipleitemEntity) baseQuickAdapter.getData().get(position);
        final int goodsId = entity.getField(MultipleFields.ID);
        final ForBuyDetailDelegate detailDelegate = ForBuyDetailDelegate.create(goodsId);
        DELEGATE.getSupportDelegate().start(detailDelegate);
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
