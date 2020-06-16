package sy.bishe.ygou.ui.loader;

import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDialog;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import sy.bishe.ygou.R;
import sy.bishe.ygou.utils.dim.DimenUtil;

public class YGouLoader {
    private static final int LOADER_SIZE_SCALE = 6;
    private static final int LOADER_OFFSET_SCALE = 10;
    private static final ArrayList<AppCompatDialog> LOADERS = new ArrayList<>();
    private static final String DEFAULT_LOADER = LoaderStyle.BallClipRotatePulseIndicator.name();

    public static void showLoading(Context context,Enum<LoaderStyle> type){
        showLoading(context,type.name());
    }

    public static void showLoading(Context context,String type){
        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.dialog);
        final AVLoadingIndicatorView avLoadingIndicatorView = LoaderCreator.create(type,context);
        dialog.setContentView(avLoadingIndicatorView);
        int devicewidth = DimenUtil.getScreenWidth();
        int deviceHeight = DimenUtil.getScreenHeight();
        final Window dialogWindow = dialog.getWindow();
        if (dialogWindow!=null){
            final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = devicewidth / LOADER_SIZE_SCALE;
            lp.height = deviceHeight / LOADER_SIZE_SCALE;
            lp.height = lp.height+deviceHeight/LOADER_OFFSET_SCALE;
            lp.gravity = Gravity.CENTER;
        }
     // LOADERS.add(dialog);
      //  dialog.show();
    }
    public static void showLoading(Context context){
       // showLoading(context,DEFAULT_LOADER);
    }
    public static void stopLoading(){
        for (AppCompatDialog dialog: LOADERS
             ) {
            if (dialog != null){
            if (dialog.isShowing()){
                dialog.cancel();
            }
}
        }
    }
}
