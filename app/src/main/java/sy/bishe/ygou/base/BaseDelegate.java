package sy.bishe.ygou.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragmentDelegate;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

public abstract class BaseDelegate extends SwipeBackFragment {
    /**
     * 绑定资源文件
     */
    @SuppressWarnings("SpellCheckingInspection")
    public final SupportFragmentDelegate DELEGATE = new SupportFragmentDelegate(this);
//    protected FragmentActivity _mActivity = null;
    private Unbinder unbinder = null;
    public abstract Object setLayout();
    /**
     * 强制子类实现绑定
     * @param bundle
     * @param rootView
     */
    public abstract void OnBindView(@Nullable Bundle bundle, View rootView);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View rootview = null;
        if (setLayout() instanceof Integer){
            rootview = layoutInflater.inflate((Integer)setLayout(),viewGroup,false);
        }else if (setLayout() instanceof View){
            rootview = (View)setLayout();
        }
        if (rootview!=null){
            unbinder = ButterKnife.bind(this,rootview);
            OnBindView(bundle,rootview);
        }
        return rootview;
    }

    /**
     * 返回代理的activity
     * @return
     */
    public final BaseSupportActivity getSupportActivity(){
        return (BaseSupportActivity) _mActivity;
    }
    /**
     * 解除绑定
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null){
            unbinder.unbind();
        }
    }

}
