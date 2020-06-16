package sy.bishe.ygou.delegate.sort;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import sy.bishe.ygou.R;
import sy.bishe.ygou.delegate.buttons.ButtonItemDelegate;

public  class SortDlegate extends ButtonItemDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_sort;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {

    }
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final VerticalListDelegate delegate = new VerticalListDelegate();
        getSupportDelegate().loadRootFragment(R.id.vertical_list_container, delegate);
        getSupportDelegate().loadRootFragment(R.id.sort_content_container,ContentDelegate.newInstance(1),false,false);
    }
}
