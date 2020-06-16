package sy.bishe.ygou.delegate.friends.contanct;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class RecommendConvert extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final ArrayList<MultipleitemEntity> list = new ArrayList<>();
        final JSONObject jsonObject = JSON.parseObject(getsJsonData()).getJSONObject("jsonObject");
        JSONArray jsonArray = jsonObject.getJSONArray("recommend");
        if (!jsonArray.isEmpty()){
            final int size = jsonArray.size();
            for (int i = 0; i < size; i++) {
                final JSONObject data = jsonArray.getJSONObject(i);
                final String imageUrl = data.getString("friend_img");
                final String name = data.getString("_");
                final String content = "你好！我是";
                final boolean tag = data.getBoolean("friend_agree");
                final int id = data.getInteger("friend_id");
                final MultipleitemEntity entity = MultipleitemEntity.builder()
                        .setField(MultipleFields.ITEM_TYPE, ItemType.RECOMMEND)
                        .setField(MultipleFields.ID, id)
                        .setField(MultipleFields.NAME, name)
                        .setField(MultipleFields.IMAGE_URL, imageUrl)
                        .setField(MultipleFields.TEXT, content+name)
                        .setField(MultipleFields.TAG, tag)
                        .build();
                list.add(entity);
        }
        }
        return list;
    }
}
