package sy.bishe.ygou.ui.recycler;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import sy.bishe.ygou.delegate.buttons.MultipleFields;

public class MultipleitemEntity implements MultiItemEntity {
    //使用软引用保证数据及时回收
    private final ReferenceQueue<LinkedHashMap<Object,Object>> ITEM_QUENE = new ReferenceQueue<>();
    private final LinkedHashMap<Object,Object> MULTIPLE_FIELDS = new LinkedHashMap<>();
    private final SoftReference<LinkedHashMap<Object,Object>> FIELDS_REFERENCE = new SoftReference<>(MULTIPLE_FIELDS,ITEM_QUENE);

     public MultipleitemEntity(LinkedHashMap<Object,Object> fields) {
         FIELDS_REFERENCE.get().putAll(fields);
    }
    public static MultipleEntityBuilder builder(){
         return new MultipleEntityBuilder();
    }
    @Override
    public int getItemType() {
        return (int) FIELDS_REFERENCE.get().get(MultipleFields.ITEM_TYPE);
    }
    @SuppressWarnings("unchecked")
    public final <T> T getField(Object key){
         return (T) FIELDS_REFERENCE.get().get(key);
    }
    public final LinkedHashMap<?,?> getFields(){
         return FIELDS_REFERENCE.get();
    }
    public final MultipleitemEntity setFields(Object key,Object val){
         FIELDS_REFERENCE.get().put(key,val);
         return this;
    }

    public final MultipleitemEntity setField(MultipleFields tag, boolean b) {
        FIELDS_REFERENCE.get().put(tag,b);
        return this;
    }
}
