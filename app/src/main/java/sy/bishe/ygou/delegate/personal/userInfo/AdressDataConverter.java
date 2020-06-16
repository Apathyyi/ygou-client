package sy.bishe.ygou.delegate.personal.userInfo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class AdressDataConverter extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final JSONArray array = JSONObject.parseObject(getsJsonData()).getJSONArray("data");
        if (array!=null){
            final int size = array.size();
            for (int i = 0; i <size ; i++) {
                final JSONObject object = array.getJSONObject(i);
                final int id = object.getInteger("address_id");
                final String name = object.getString("address_name");
                final String phone = object.getString("address_phone");
                final String address  =object.getString("address_content");
                final String isDefault = object.getString("address_default");
                final String area = object.getString("address_area");

                final MultipleitemEntity entity = MultipleitemEntity.builder()
                        .setItemType(ItemType.ITEM_ADRESS)
                        .setField(MultipleFields.ID,id)
                        .setField(MultipleFields.NAME,name)
                        .setField(MultipleFields.TAG,isDefault)
                        .setField(AdressFields.ADRESS,address)
                        .setField(AdressFields.PHONE,phone)
                        .setField(AdressFields.AREA,area)
                        .build();
                ENTITIES.add(entity);
            }
        }

        return ENTITIES;
    }
}
