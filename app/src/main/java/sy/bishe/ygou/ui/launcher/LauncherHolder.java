package sy.bishe.ygou.ui.launcher;
import android.content.Context;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import com.bigkoo.convenientbanner.holder.Holder;

public class LauncherHolder implements Holder<Integer>{
    private AppCompatImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new AppCompatImageView(context);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, Integer data) {
        imageView.setBackgroundResource(data);
    }
}
