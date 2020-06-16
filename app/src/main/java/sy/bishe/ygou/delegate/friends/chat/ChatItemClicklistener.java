package sy.bishe.ygou.delegate.friends.chat;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class ChatItemClicklistener extends SimpleClickListener {
    private final YGouDelegate DELEGATE;

    public ChatItemClicklistener(YGouDelegate delegate) {
        DELEGATE = delegate;
    }
    public static SimpleClickListener create(YGouDelegate buttonDelegate) {
        return new ChatItemClicklistener(buttonDelegate);
    }
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final MultipleitemEntity entity = (MultipleitemEntity) baseQuickAdapter.getData().get(position);
        final String name = entity.getField(MultipleFields.NAME);
        entity.setFields(ChatFields.COUNT,0);
        baseQuickAdapter.notifyDataSetChanged();
        final ChatDetailDelegate detailDelegate = ChatDetailDelegate.create(name);

        DELEGATE.getSupportDelegate().start(detailDelegate);
       // DELEGATE.getSupportDelegate().start(new ContanctDelegate());
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
