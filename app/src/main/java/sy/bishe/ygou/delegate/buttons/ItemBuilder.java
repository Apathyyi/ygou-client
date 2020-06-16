package sy.bishe.ygou.delegate.buttons;

import java.util.LinkedHashMap;

import sy.bishe.ygou.bean.ButtonBean;

public class ItemBuilder {
    private final LinkedHashMap<ButtonBean,ButtonItemDelegate> ITEMS = new LinkedHashMap<>();
    public static ItemBuilder builder(){
        return new ItemBuilder();
    }
    //一次加一个
    public final ItemBuilder addItem(ButtonBean buttonBean,ButtonItemDelegate delegate){
        ITEMS.put(buttonBean,delegate);
        return this;
    }
    //一次加所有
    public final ItemBuilder addItems(LinkedHashMap<ButtonBean,ButtonItemDelegate> items){
        ITEMS.putAll(items);
        return  this;
    }
    public final LinkedHashMap<ButtonBean,ButtonItemDelegate> build(){
        return ITEMS;
    }



}
