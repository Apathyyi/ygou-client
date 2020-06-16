package sy.bishe.ygou.ui.recycler;

import java.util.LinkedHashMap;

import sy.bishe.ygou.delegate.buttons.MultipleFields;

public class MultipleEntityBuilder {
    private static final LinkedHashMap<Object,Object> FIELDS = new LinkedHashMap<>();

    public MultipleEntityBuilder() {
        FIELDS.clear();
    }

    /**
     * 设置类型
     * @param itemType
     * @return
     */
    public final MultipleEntityBuilder setItemType(int itemType){
        FIELDS.put(MultipleFields.ITEM_TYPE,itemType);
        return this;
    }

    /**
     * 单个数据
     * @param key
     * @param val
     * @return
     */
    public final MultipleEntityBuilder setField(Object key,Object val){
        FIELDS.put(key,val);
        return this;
    }

    /**
     * map数据
     * @param map
     * @return
     */
    public final MultipleEntityBuilder setFields(LinkedHashMap<?,?> map){
        FIELDS.putAll(map);
        return this;
    }
    public final MultipleitemEntity build(){
        return new MultipleitemEntity(FIELDS);
    }
}
