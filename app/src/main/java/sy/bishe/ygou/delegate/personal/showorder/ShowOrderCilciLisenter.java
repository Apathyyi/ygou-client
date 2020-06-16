package sy.bishe.ygou.delegate.personal.showorder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import me.yokeyword.fragmentation.SupportFragmentDelegate;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.delegate.personal.order.OrderListDelegate;
import sy.bishe.ygou.delegate.personal.order.ShowOrderDetailDelegate;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class ShowOrderCilciLisenter extends SimpleClickListener {

    private final SupportFragmentDelegate DELEGATE;

    public ShowOrderCilciLisenter(SupportFragmentDelegate delegate) {
        this.DELEGATE = delegate;
    }

    public static ShowOrderCilciLisenter create(SupportFragmentDelegate delegate){
        return new ShowOrderCilciLisenter(delegate);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final MultipleitemEntity entity = (MultipleitemEntity) baseQuickAdapter.getData().get(position);
        final int orderId = entity.getField(MultipleFields.ID);
        final ShowOrderDetailDelegate detailDelegate =new ShowOrderDetailDelegate();
        Bundle bundle = new Bundle();
        bundle.putString(OrderListDelegate.ORDER_ID,String.valueOf(orderId));
        detailDelegate.setArguments(bundle);
        Log.i("goodsId:", ""+orderId+"****position:"+position);
        DELEGATE.start(detailDelegate);

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
