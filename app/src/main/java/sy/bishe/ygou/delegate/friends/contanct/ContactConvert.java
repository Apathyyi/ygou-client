package sy.bishe.ygou.delegate.friends.contanct;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class ContactConvert extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final ArrayList<MultipleitemEntity> list = new ArrayList<>();
        final JSONArray jsonArray = JSONObject.parseObject(getsJsonData()).getJSONArray("data");
        final int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = jsonArray.getJSONObject(i);
            final String imageUrl = data.getString("user_img");
            final String name = data.getString("user_name");
            final String id = data.getString("user_id");
            final String user_signature = data.getString("user_signature");

            final MultipleitemEntity entity = MultipleitemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, ItemType.CONTACT)
                    .setField(MultipleFields.ID,id)
                    .setField(MultipleFields.NAME,name)
                    .setField(AddFriendFields.SIGNATURE,user_signature)
                    .setField(MultipleFields.IMAGE_URL,imageUrl)
                    .build();
            list.add(entity);
        }
        return list;
    }
}
