package sy.bishe.ygou.ui.recycler;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

public class MulitipleViewHolder extends BaseViewHolder {

    public MulitipleViewHolder(View view) {
        super(view);
    }
    public static MulitipleViewHolder create(View view){
        return new MulitipleViewHolder(view);
    }
}
