package sy.bishe.ygou.delegate.buttons;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import me.yokeyword.fragmentation.ISupportFragment;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.bean.ButtonBean;
import sy.bishe.ygou.delegate.base.YGouDelegate;

public abstract class BaseButoonDelegate extends YGouDelegate implements View.OnClickListener{
    //标题和图标
    private final ArrayList<ButtonBean> TAB_BEANS = new ArrayList<>();
    //页面的视图
    private final ArrayList<ButtonItemDelegate> ITEM_DELEGATES = new ArrayList<>();
    //
    private final LinkedHashMap<ButtonBean,ButtonItemDelegate> ITEMS = new LinkedHashMap<>();
    //当前
    private int sCurrentDelegate = 0;
    //首页
    private int sIndexDelegate = 0;
    //颜色
    private  int sCilickedColor = Color.RED;
    //设置一个item
    public abstract LinkedHashMap<ButtonBean,ButtonItemDelegate> setItems(ItemBuilder builder);
    //设置首页是第几个
    public abstract int setindexDelegate();
    public abstract int setClickColor();
    @BindView(R2.id.button_bar)
    LinearLayoutCompat sButtonBar = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sIndexDelegate = setindexDelegate();
        if (setClickColor()!= 0){
            sCilickedColor = setClickColor();
        }
        final ItemBuilder builder = ItemBuilder.builder();
        final LinkedHashMap<ButtonBean,ButtonItemDelegate> items = setItems(builder);
        ITEMS.putAll(items);
        for (Map.Entry<ButtonBean,ButtonItemDelegate> item: ITEMS.entrySet()) {
            final ButtonBean key = item.getKey();
            final ButtonItemDelegate value = item.getValue();
            TAB_BEANS.add(key);
            ITEM_DELEGATES.add(value);

        }
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_button;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        final int size = ITEMS.size();
        for (int i = 0;i<size;i++){
            LayoutInflater.from(getContext()).inflate(R.layout.button_item_icon_text,sButtonBar);
            final RelativeLayout relativeLayout = (RelativeLayout) sButtonBar.getChildAt(i);
            relativeLayout.setTag(i);
            //点击事件
            relativeLayout.setOnClickListener(this);
            final IconTextView itemIcon = (IconTextView) relativeLayout.getChildAt(0);
            final AppCompatTextView itemTitle =(AppCompatTextView) relativeLayout.getChildAt(1);
            final ButtonBean buttonBean = TAB_BEANS.get(i);
            itemIcon.setText(buttonBean.getICON());
            itemTitle.setText(buttonBean.getTITLE());
            if (i == sIndexDelegate){
                itemIcon.setTextColor(sCilickedColor);
                itemTitle.setTextColor(sCilickedColor);
            }
        }
        final ISupportFragment[] delegateArray = ITEM_DELEGATES.toArray(new ISupportFragment[size]);
        //加载多个根
        getSupportDelegate().loadMultipleRootFragment(R.id.button_bar_delegate_container,sIndexDelegate,delegateArray);
    }

    //点击不同的button
    @Override
    public void onClick(View v) {
        final int tag = (int) v.getTag();
        resetColor();
        final RelativeLayout item = (RelativeLayout) v;
        final IconTextView itemIcon = (IconTextView) item.getChildAt(0);
        itemIcon.setTextColor(sCilickedColor);
        final AppCompatTextView itemTitle =(AppCompatTextView) item.getChildAt(1);
        itemTitle.setTextColor(sCilickedColor);
        //要显示的fragment,要隐藏得fragment
//        getSupportDelegate().showHideFragment(ITEM_DELEGATES.get(tag),ITEM_DELEGATES.get(sCurrentDelegate));
        showHideFragment(ITEM_DELEGATES.get(tag),ITEM_DELEGATES.get(sCurrentDelegate));
        sCurrentDelegate = tag;
    }

    /**
     * 重置颜色
     */
    private void resetColor(){
        final  int count = sButtonBar.getChildCount();
        for (int i = 0;i<count;i++){
            final RelativeLayout item = (RelativeLayout) sButtonBar.getChildAt(i);
            final IconTextView itemIcon = (IconTextView) item.getChildAt(0);
            itemIcon.setTextColor(Color.GRAY);
            final AppCompatTextView itemTitle =(AppCompatTextView) item.getChildAt(1);
            itemTitle.setTextColor(Color.GRAY);
        }
    }
}
