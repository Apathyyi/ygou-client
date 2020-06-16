package sy.bishe.ygou.delegate.sort;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class ForBuyDataConvert extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final ArrayList<MultipleitemEntity> list = new ArrayList<>();
        final JSONArray jsonArray = JSON.parseObject(getsJsonData()).getJSONArray("data");
        if (jsonArray != null) {
            final int size = jsonArray.size();
            for (int i = 0; i < size; i++) {
                final JSONObject data = jsonArray.getJSONObject(i);
                final String release_title = data.getString("release_title");
                final String release_price = data.getString("release_price");
                final String release_contact = data.getString("release_contact");
                final String release_user_img = data.getString("release_user_img");
                final int id = data.getInteger("id");

                final MultipleitemEntity entity = MultipleitemEntity.builder()
                        .setItemType(ItemType.SHOW_BUY)
                        .setField(MultipleFields.ID,id)
                        .setField(MultipleFields.IMAGE_URL,release_user_img)
                        .setField(MultipleFields.TITLE,release_title)
                        .setField(SortDetailFields.PRICE,release_price)
                        .setField(MultipleFields.TEXT,release_contact)
                        .build();
                list.add(entity);
            }
        }
        return list;
    }
}
