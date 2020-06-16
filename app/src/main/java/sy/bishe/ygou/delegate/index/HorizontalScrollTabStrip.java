package sy.bishe.ygou.delegate.index;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义滚动横向导航
 * 
 * @author 秋风
 * 
 */
@SuppressLint("HandlerLeak")
public class HorizontalScrollTabStrip extends HorizontalScrollView {
    /** tag标题 */
    private List<String> mTitles;
    /** 标签容器 */
    private LinearLayout tabsContainer;
    /** 屏幕宽度 */
    private int mScreenWidth;
    /** 默认显示的标签数量 */
    public int mDefaultShowTagCount = 5;
    /** 标签数量 */
    private int tabCount;
    /** 当前位置 */
    private int currentPosition = 0;
    /** 标签默认宽度 */
    private int mDefaultTagWidth;
    /** 当前点击控件索引 */
    private int mTagIndex = -1;
    /** 标签布局参数 */
    private LinearLayout.LayoutParams mTagLayoutParams;

    /** 激活时颜色 */
    private Integer mActiveColor = Color.BLACK;


    /** 默认颜色 */
    private Integer mDefaultColor = Color.GRAY;

    public HorizontalScrollTabStrip(Context context) {
        this(context, null);
    }

    public HorizontalScrollTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScrollTabStrip(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        mTitles = new ArrayList<String>();
        mScreenWidth = getScreenWidth();
        initLayout();
        addView(tabsContainer);
        resetLayoutParams();

    }

    /** 重置布局 */
    private void resetLayoutParams() {
        mDefaultTagWidth = (int) (mScreenWidth / (mDefaultShowTagCount + 0.7));
        if (null == mTagLayoutParams) {
            mTagLayoutParams = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        }
        mTagLayoutParams.weight = 0f;
        mTagLayoutParams.width = mDefaultTagWidth;
    }

    /**
     * 初始化LinearLayout
     */
    private void initLayout() {
        tabsContainer = new LinearLayout(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(lp);
    }

    /**
     * 获取屏幕宽度
     * 
     * @return
     */
    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Service.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 设置标签
     * 
     * @param titles 标题
     */
    public void setTags(List<String> titles) {
        mTitles.clear();
        if (null == titles || titles.size() < 2) {
            return;
        }
        mTitles = titles;
        int size = mTitles.size();
        for (int i = 0; i < size; i++) {
            tabsContainer.addView(newTag(mTitles.get(i), i));
        }
        changeActiveTag(0);
    }

    /**
     * 设置标签
     * 
     * @param titles 标题
     * @param defshowTags 默认显示数量
     */
    public void setTags(List<String> titles, int defshowTags) {
        mTitles.clear();
        if (null == titles || titles.size() < 2) {
            return;
        }
        mTitles = titles;
        mDefaultShowTagCount = defshowTags;
        int size = mTitles.size();
        for (int i = 0; i < size; i++) {
            tabsContainer.addView(newTag(mTitles.get(i), i));
        }
        changeActiveTag(0);
    }

    /**
     * 设置标签
     * 
     * @param titles 标题
     * @param defshowTags 默认显示数量
     * @param showPosition 当前显示下标
     */
    public void setTags(List<String> titles, int defshowTags, int showPosition) {
        mTitles.clear();
        if (null == titles || titles.size() < 2) {
            return;
        }
        mTitles = titles;
        mDefaultShowTagCount = defshowTags;
        resetLayoutParams();
        int size = mTitles.size();
        for (int i = 0; i < size; i++) {
            tabsContainer.addView(newTag(mTitles.get(i), i));
        }
        changeActiveTag(showPosition);
    }

    /**
     * new一个新的tag
     * 
     * @param tag
     * @param position
     */
    private TextView newTag(String tag, final int position) {
        TextView tv = new TextView(getContext());
        tv.setText(tag);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setFocusable(true);
        tv.setSingleLine();
        tv.setLayoutParams(mTagLayoutParams);
        tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                changeActiveTag(position);
            }
        });
        return tv;
    }


    /**
     * 切换高亮标签
     * 
     * @param position
     */
    protected void changeActiveTag(int position) {
        isScrool = false;
        int size = tabsContainer.getChildCount();
        /** 当前控件索引 */
     //   mTagIndex = -1;
        int[] location = new int[2];
        for (int i = 0; i < size; i++) {
            TextView tv = (TextView) tabsContainer.getChildAt(i);

            if (i == position) {
                mTagIndex = position;
                tv.getLocationOnScreen(location);
                tv.setTextColor(mActiveColor);
                Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
                tv.setTypeface(font);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            } else{
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tv.setTextColor(mDefaultColor);
                Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
                tv.setTypeface(font);
            }


        }
        // 点击tag的时候，判断 当前tag的位置，如果未完全显示，则移动到该tag完全显示，左右都需要移动
        // 通过获取控件的location（相对于屏幕的位置来判断当前控件的位置）
        // 如果设置动画的时间为1秒（1000）
        motionTimes = 10;
        motionLength = 0;
        if (location[0] < 0) {
            // 判断是否是第一个，如果是第一个只位移相差的偏移量
            if (mTagIndex == 0) {
                motionLength = location[0];
            } else {
                // 如果前边还有，则多位移一个位置
                motionLength = location[0] - mDefaultTagWidth / 2;
            }
        } else if (mScreenWidth - mDefaultTagWidth < location[0]) {
            // 说明点击的标签没有在屏幕中完全显示，需要向左侧偏移
            // 判断是否是最后一个tag，如果是，只位移相差的偏移量
            if (mTagIndex < tabsContainer.getChildCount()) {
                motionLength = mDefaultTagWidth - (mScreenWidth - location[0])
                        + mDefaultTagWidth / 2;
            } else {
                // 如果后面还存在未显示的tag，则多位移一个位置
                motionLength = mDefaultTagWidth - (mScreenWidth - location[0]);
            }
        }
        if (null != mListener) {
            if (motionLength == 0) {
                mListener.changeLine(location[0],true);
            } else {
                mListener.changeLine(location[0] - motionLength,true);
            }
        }
        mHandler.sendEmptyMessage(0);
    }

    /** 动画运动时间 */
    private int motionTimes = 10;
    /** 动画运动间距 */
    private int motionLength = 0;
    /** 是否允许滚动事件onScrollChanged的触发 */
    private boolean isScrool = false;
    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (motionTimes > 0) {
                mScrollBy();
                motionTimes--;
                mHandler.sendEmptyMessageDelayed(0, 20);
            } else {
                isScrool = true;
            }
        }
    };
    /**监听tag改变*/
    private TagChangeListener mListener;

    public void setOnTagChangeListener(TagChangeListener listener) {
        mListener = listener;
    }

    public int getmTagIndex() {
        return mTagIndex;
    }

    /** 标签改变监听 */
    public interface TagChangeListener {
        /**
         * 切换横线显示位置
         * 
         * @param location_x 偏移量
         * @param isClick 是否是点击触发
         */
        void changeLine(int location_x,boolean isClick);
    }

    /**
     * 控制导航位移
     */
    private void mScrollBy() {
        this.scrollBy(motionLength / 10, 0);
    };

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (isScrool) {
            int[] location =new int[2];
            View v=tabsContainer.getChildAt(mTagIndex);
            v.getLocationInWindow(location);

            if (null != mListener) {
                mListener.changeLine(location[0],false);
            }
        }

    }
}