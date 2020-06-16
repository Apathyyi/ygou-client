package sy.bishe.ygou.delegate.personal.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class OrderListDataConverter extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final JSONArray jsonArray = JSONObject.parseObject(getsJsonData()).getJSONArray("data");
        if (jsonArray!=null&&!jsonArray.isEmpty()){
            final int size = jsonArray.size();
            for (int i = 0; i <size ; i++) {
                final JSONObject object = jsonArray.getJSONObject(i);
                final String thumb = object.getString("order_thumb");
                final String title = object.getString("order_title");
                final String id = object.getString("order_id");
                final double price = object.getDouble("order_price");
                final String time = object.getString("order_time");
                final  String lable = object.getString("order_goods_lable");
                final String transcast = object.getString("order_trans_cast");
                final String transcount = object.getString("order_trans_count");
                final int totalcount = object.getInteger("order_total_count");
                final double totalprice = object.getDouble("order_total_price");
                final String name = object.getString("order_goods_releasename");
                final String img = object.getString("order_goods_releaseimg");
                final int count = object.getInteger("order_count");
                final String tag = object.getString("order_tag");

                final MultipleitemEntity entity = MultipleitemEntity.builder()
                        .setField(OrderItemFields.COUNT,count)
                        .setField(OrderItemFields.RELEASENAME,name)
                        .setField(OrderItemFields.IMG,img)
                        .setField(OrderItemFields.LABLE,lable)
                        .setField(OrderItemFields.TRANSCAST,transcast)
                        .setField(OrderItemFields.TRANSCOUNT,transcount)
                        .setField(OrderItemFields.TOTALCOIUNT,totalcount)
                        .setField(OrderItemFields.TOTALPRICE,totalprice)
                        .setItemType(ItemType.ORDER_LIST)
                        .setField(MultipleFields.TITLE,title)
                        .setField(MultipleFields.ID,id)
                        .setField(MultipleFields.IMAGE_URL,thumb)
                        .setField(OrderItemFields.PRICE,price)
                        .setField(OrderItemFields.TIME,time)
                        .setField(OrderItemFields.TAG,tag)
                        .build();
                ENTITIES.add(entity);
        }
    }
        return ENTITIES;
    }
}
