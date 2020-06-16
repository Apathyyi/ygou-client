package sy.bishe.ygou.delegate.personal.showorder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.delegate.personal.order.OrderItemFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class ShowOrderDataConvert extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final JSONArray jsonArray = JSONObject.parseObject(getsJsonData()).getJSONArray("data");
        JSONArray users = JSONObject.parseObject(getsJsonData()).getJSONObject("jsonObject").getJSONArray("users" );
        if (jsonArray!=null&&!jsonArray.isEmpty()){
            final int size = jsonArray.size();
            for (int i = 0; i <size ; i++) {
                final JSONObject object = jsonArray.getJSONObject(i);
                final String evl_goods_img = object.getString("evl_goods_img");
                final String evl_content = object.getString("evl_content");
                final String evl_time = object.getString("evl_time");
                final String user_img = users.getJSONObject(i).getString("user_img");
                final int order_id = object.getInteger("evl_order_id");
                final String user_name = users.getJSONObject(i).getString("user_name");
                final  String evl_location = object.getString("evl_location");

                final MultipleitemEntity entity = MultipleitemEntity.builder()
                        .setItemType(ItemType.SHOWORDER)
                        .setField(MultipleFields.ID,order_id)
                        .setField(MultipleFields.IMAGE_URL,user_img)
                        .setField(OrderItemFields.IMG,evl_goods_img)
                        .setField(MultipleFields.TEXT,evl_content)
                        .setField(MultipleFields.TIME,evl_time)
                        .setField(MultipleFields.NAME,user_name)
                        .setField(OrderItemFields.ADDRESS,evl_location)
                        .build();
                ENTITIES.add(entity);
            }
        }
        return ENTITIES;
    }
}
