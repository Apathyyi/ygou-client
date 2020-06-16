package sy.bishe.ygou.ui.recycler;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
@SuppressWarnings("unused")
public class TransLuncentBehavior extends CoordinatorLayout.Behavior<Toolbar> {
    //top
    private int mDistanceY = 0;
    //颜色变化
    private static final int SHOW_SPEED = 3;
    private final RgbValye RGB_VALUE = RgbValye.create(255,255,255);

    public TransLuncentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull Toolbar child, @NonNull View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull Toolbar child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return true;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull Toolbar child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        mDistanceY +=dy;
        final int targetHeight = child.getBottom();

        if (mDistanceY >0&&mDistanceY<=targetHeight){
            final float scale = (float)mDistanceY/targetHeight;
            final float alpha = scale * 255;
            child.setBackgroundColor(Color.argb ((int) alpha,RGB_VALUE.red(),RGB_VALUE.green(),RGB_VALUE.blue()));
        }else if (mDistanceY > targetHeight){
            child.setBackgroundColor(Color.rgb(RGB_VALUE.red(),RGB_VALUE.green(),RGB_VALUE.blue()));
        }
    }
}
