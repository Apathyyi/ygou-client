package sy.bishe.ygou.delegate.search;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class SearchDataConverter extends DataConverter {
    public static final String TAG_SEARCH_HISTORY = "searcj_history";

    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final String jsonStr = YGouPreferences.getCustomAppProfile(TAG_SEARCH_HISTORY);
        if (!jsonStr.equals("")){
            final JSONArray array = JSONArray.parseArray(jsonStr);
            final int size = array.size();
            if (size>5){
                for (int i = 0; i < 5; i++) {
                    final String text = array.getString(i);
                    final MultipleitemEntity entity =  MultipleitemEntity.builder()
                            .setItemType(ItemType.ITEM_SEARCH)
                            .setField(MultipleFields.TEXT,text)
                            .build();
                    ENTITIES.add(entity);
                }
            }else {
                for (int i = 0; i < size; i++) {
                    final String text = array.getString(i);
                    final MultipleitemEntity entity =  MultipleitemEntity.builder()
                            .setItemType(ItemType.ITEM_SEARCH)
                            .setField(MultipleFields.TEXT,text)
                            .build();
                    ENTITIES.add(entity);
                }
            }

        }
        return ENTITIES;
    }
}
