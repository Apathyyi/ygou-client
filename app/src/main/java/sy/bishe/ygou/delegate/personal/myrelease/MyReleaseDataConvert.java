package sy.bishe.ygou.delegate.personal.myrelease;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.delegate.personal.order.OrderItemFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class MyReleaseDataConvert extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final JSONArray jsonArray = JSONObject.parseObject(getsJsonData()).getJSONArray("data");
        if (jsonArray!=null&&!jsonArray.isEmpty()){
            final int size = jsonArray.size();
            for (int i = 0; i <size ; i++) {
                final JSONObject object = jsonArray.getJSONObject(i);

                final int id = object.getInteger("id");
                final String thumb = object.getString("img");
                final String title = object.getString("title");
                final double price = object.getDouble("price");
                final String time = object.getString("time");
                final int count = object.getInteger("count");
                final String tag = object.getString("tag");
                //订单持有者名字
                final String name = object.getString("user_name");

                final MultipleitemEntity entity = MultipleitemEntity.builder()
                        .setField(OrderItemFields.COUNT,count)
                        .setField(MultipleFields.ID,id)
                        .setItemType(ItemType.MY_RELEASE)
                        .setField(MultipleFields.TITLE,title)
                        .setField(MultipleFields.IMAGE_URL,thumb)
                        .setField(OrderItemFields.PRICE,price)
                        .setField(OrderItemFields.TIME,time)
                        .setField(OrderItemFields.TAG,tag)
                        .setField(OrderItemFields.NAME,name)
                        .build();
                ENTITIES.add(entity);
            }
        }
        return ENTITIES;
    }
}
