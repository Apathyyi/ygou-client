package sy.bishe.ygou.delegate.cart;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class ShopCartDataConverter extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final ArrayList<MultipleitemEntity> list = new ArrayList<>();
        final JSONArray jsonArray = JSONObject.parseObject(getsJsonData()).getJSONArray("data");
        if (jsonArray!=null&&!jsonArray.isEmpty()) {
            final int size = jsonArray.size();
            for (int i = 0; i < size; i++) {
                final JSONObject object = jsonArray.getJSONObject(i);
                final String title = object.getString("cart_name");
                final String desc = object.getString("cart_desc");
                final int id = object.getInteger("cart_goods_id");
                final Double price = object.getDouble("cart_price");
                final int count = object.getInteger("cart_goods_count");
                final String thumb = object.getString("cart_thumb");

                final MultipleitemEntity entity = MultipleitemEntity.builder()
                        .setField(MultipleFields.ITEM_TYPE, ItemType.SHOP_CART)
                        .setField(MultipleFields.ID, id)
                        .setField(MultipleFields.IMAGE_URL, thumb)
                        .setField(ShopCartItemFields.TITLE, title)
                        .setField(ShopCartItemFields.DESC, desc)
                        .setField(ShopCartItemFields.COUNT, count)
                        .setField(ShopCartItemFields.PRICE, price)
                        .setField(ShopCartItemFields.IS_SELECTED, false)
                        .setField(ShopCartItemFields.POSITION, i)
                        .build();
                list.add(entity);
            }
        }
        return list;
    }
}
