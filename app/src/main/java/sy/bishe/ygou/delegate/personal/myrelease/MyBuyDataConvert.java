package sy.bishe.ygou.delegate.personal.myrelease;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class MyBuyDataConvert extends DataConverter {


    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final JSONArray jsonArray = JSONObject.parseObject(getsJsonData()).getJSONArray("data");
        if (jsonArray!=null&&!jsonArray.isEmpty()){
            final int size = jsonArray.size();
            for (int i = 0; i <size ; i++) {
                final JSONObject object = jsonArray.getJSONObject(i);
                final String type = object.getString("release_type");
                final String title = object.getString("release_title");
                final String desc = object.getString("release_desc");
                final int count = object.getInteger("release_count");
                final double price = object.getDouble("release_price");
                final String contact = object.getString("release_contact");
                final String time = object.getString("release_time");
                final String user_img = object.getString("release_user_img");

                final int id = object.getInteger("id");

                final MultipleitemEntity entity = MultipleitemEntity.builder()
                        .setField(MultipleFields.ID,id)
                        .setField(ReleaseFields.COUNT,count)
                        .setField(ReleaseFields.TYPE,type)
                        .setField(ReleaseFields.TITLE,title)
                        .setField(ReleaseFields.DESC,desc)
                        .setField(ReleaseFields.PRICE,price)
                        .setField(ReleaseFields.CONATCT,contact)
                        .setField(ReleaseFields.USERIMG,user_img)
                        .setField(ReleaseFields.TIME,time)
                        .setItemType(ItemType.MY_BUY)
                        .build();
                ENTITIES.add(entity);
            }
        }
        return ENTITIES;
    }
}
