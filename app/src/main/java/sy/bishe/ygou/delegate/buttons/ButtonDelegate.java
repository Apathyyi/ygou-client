package sy.bishe.ygou.delegate.buttons;

import android.graphics.Color;

import java.util.LinkedHashMap;

import sy.bishe.ygou.bean.ButtonBean;
import sy.bishe.ygou.delegate.cart.ShopCartDelegate;
import sy.bishe.ygou.delegate.find.ReleaseBuyDelegate;
import sy.bishe.ygou.delegate.index.IndexDlegate;
import sy.bishe.ygou.delegate.personal.PersonalDelegate;
import sy.bishe.ygou.delegate.sort.SortDlegate;


//主页页面
public class ButtonDelegate extends BaseButoonDelegate {

    @Override
    public LinkedHashMap<ButtonBean, ButtonItemDelegate> setItems(ItemBuilder builder) {
        final LinkedHashMap<ButtonBean,ButtonItemDelegate> items = new LinkedHashMap<>();
        items.put(new ButtonBean("{fa-home}","主页"),new IndexDlegate());
        items.put(new ButtonBean("{fa-sort}","分类"),new SortDlegate());
        items.put(new ButtonBean("{fa-plus}","发布"),new ReleaseBuyDelegate());
        items.put(new ButtonBean("{fa-shopping-cart}","购物车"),new ShopCartDelegate());
        items.put(new ButtonBean("{fa-user}","我的"),new PersonalDelegate());
        return builder.addItems(items).build();
    }

    @Override
    public int setindexDelegate() {
        return 0;
    }

    @Override
    public int setClickColor() {
        return Color.parseColor("#ffff8800");
    }



}
