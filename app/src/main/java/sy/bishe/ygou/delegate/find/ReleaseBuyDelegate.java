package sy.bishe.ygou.delegate.find;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import butterknife.OnClick;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.delegate.buttons.ButtonItemDelegate;

public class ReleaseBuyDelegate extends ButtonItemDelegate {


    /**
     * 发布
     */
    @OnClick(R2.id.to_release)
    void toRelease(){
        getParentDelegate().getSupportDelegate().start(new ReleaseDelegate());
    }

    /**
     * 求购
     */
    @OnClick(R2.id.to_buy)
    void toBuy(){
        getParentDelegate().getSupportDelegate().start(new BuyDelegate());
    }

    @Override
    public Object setLayout() {
        return R.layout.releasebuyelegate;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        super.OnBindView(bundle, rootView);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }
}
