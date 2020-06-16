package sy.bishe.ygou.delegate.buttons;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;

import sy.bishe.ygou.delegate.base.YGouDelegate;

public abstract class ButtonItemDelegate extends YGouDelegate implements View.OnKeyListener {
    private long sExitTime = 0;
    private static final int EXIT_TIME = 2000;
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
//            if ((System.currentTimeMillis() - sExitTime) >EXIT_TIME){
//                Toast.makeText(getContext(),"双击推出"+getString(R.string.app_name),Toast.LENGTH_SHORT).show();
//                sExitTime = System.currentTimeMillis();
//            }else {
//             //   _mActivity.finish();
//                if (sExitTime != 0){
//                    sExitTime = 0;
//                }
//            }
//            return true;
//        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        final  View rootView = getView();
        if (rootView != null){
            rootView.setFocusableInTouchMode(true);
            rootView.requestFocus();
            rootView.setOnKeyListener(this);
        }
    }

    @Override
    public Object setLayout() {
        return null;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {

    }
}
