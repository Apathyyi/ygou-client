package sy.bishe.ygou.ui.recycler;

import androidx.annotation.ColorInt;

import com.choices.divider.DividerItemDecoration;

public class BaseDecoration extends DividerItemDecoration {

        private BaseDecoration(@ColorInt int color,int size){
            setDividerLookup(new DividerLookupImpl(color,size));
        }
        public static BaseDecoration create(@ColorInt int color,int size){
            return new BaseDecoration(color,size);
        }

}
