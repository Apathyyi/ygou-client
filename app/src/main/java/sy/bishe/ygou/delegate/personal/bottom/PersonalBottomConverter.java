package sy.bishe.ygou.delegate.personal.bottom;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class PersonalBottomConverter extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final ArrayList<MultipleitemEntity> list = new ArrayList<>();
        final JSONArray jsonArray = JSONObject.parseObject(getsJsonData()).getJSONArray("data");
        final int size = jsonArray.size();
        for (int i = 0;i<size;i++){
            final JSONObject object = jsonArray.getJSONObject(i);
            final int id = object.getInteger("adv_id");
            final String title = object.getString("adv_title");
            final String desc = object.getString("adv_desc");
            final String thumb = object.getString("adv_thumb");
            final MultipleitemEntity entity = MultipleitemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, ItemType.PERSONAL_BOTTOM)
                    .setField(MultipleFields.IMAGE_URL,thumb)
                    .setField(MultipleFields.ID,id)
                    .setField(MultipleFields.TITLE,title)
                    .setField(PersonalItemFields.DESC,desc)
                    .setField(PersonalItemFields.POSITION,i)
                    .build();
            list.add(entity);
        }
        return list;
    }
}
