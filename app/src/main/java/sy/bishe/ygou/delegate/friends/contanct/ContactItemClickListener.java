package sy.bishe.ygou.delegate.friends.contanct;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class ContactItemClickListener extends SimpleClickListener {
    private final YGouDelegate DELEGATE;
    public ContactItemClickListener(YGouDelegate delegate) {
        DELEGATE = delegate;
    }
    public static SimpleClickListener create(YGouDelegate buttonDelegate) {
        return new ContactItemClickListener(buttonDelegate);
    }
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final MultipleitemEntity entity = (MultipleitemEntity) baseQuickAdapter.getData().get(position);
        final String name = entity.getField(MultipleFields.NAME);
        final ContactInfoDelegate detailDelegate = ContactInfoDelegate.create(name);
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
