package sy.bishe.ygou.delegate.friends.dynamic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class DynamicDataConvert extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final ArrayList<MultipleitemEntity> list = new ArrayList<>();
        final JSONArray jsonArray = JSON.parseObject(getsJsonData()).getJSONArray("data");
        final int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = jsonArray.getJSONObject(i);
            final String imageUrl = data.getString("thumb");
            final String name = data.getString("name");
            final String content = data.getString("content");
            final String time = data.getString("time");
            final String id = data.getString("id");

            final MultipleitemEntity entity = MultipleitemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, ItemType.DYNAMIC)
                    .setField(MultipleFields.ID,id)
                    .setField(MultipleFields.NAME,name)
                    .setField(MultipleFields.IMAGE_URL,imageUrl)
                    .setField(MultipleFields.TEXT,content)
                    .setField(DynamicFields.TIME,time)
                    .build();
            list.add(entity);
        }
        return list;
    }
}
