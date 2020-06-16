package sy.bishe.ygou.ui.banner;

import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ImageHolder implements Holder<String> {
    private AppCompatImageView sImageView = null;
    @Override
    public View createView(Context context) {
        sImageView = new AppCompatImageView(context);
        return sImageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        Glide.with(context)
                .load(data)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .centerCrop()
                .into(sImageView);
    }
}
