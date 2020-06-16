package sy.bishe.ygou.delegate.search;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class HotDataConvert extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final ArrayList<MultipleitemEntity> list = new ArrayList<>();
        final JSONArray jsonArray = JSON.parseObject(getsJsonData()).getJSONArray("data");
        final int size = jsonArray.size();
        for (int i = 0;i<size;i++){
             final String name = jsonArray.getString(i);
             final MultipleitemEntity entity = MultipleitemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, ItemType.SRAECH_HOT)
                    .setField(MultipleFields.NAME,name)
                    .build();
            list.add(entity);
        }
        return list;
    }
}
