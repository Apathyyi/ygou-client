package sy.bishe.ygou.delegate.sort;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class SortDetailDataConvert extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final ArrayList<MultipleitemEntity> list = new ArrayList<>();
        final JSONArray jsonArray = JSON.parseObject(getsJsonData()).getJSONArray("data");
        if (jsonArray!=null){
        final int size = jsonArray.size();
        for (int i = 0;i<size;i++) {
            final JSONObject data = jsonArray.getJSONObject(i);
            final String imageUrl = data.getString("goodsinfo_thumb");
            final String name = data.getString("goodsinfo_name");
            final String id = data.getString("goodsinfo_id");
            final String desc = data.getString("goodsinfo_desc");
            final String price = data.getString("goodsinfo_price");
            final String hot = data.getString("goodsinfo_hot");
            final String lable = data.getString("goodsinfo_lable");
            final String address = data.getString("goodsinfo_area");
            final String time = data.getString("goodsinfo_time");
            final MultipleitemEntity entity = MultipleitemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, ItemType.SORTDETAIL)
                    .setField(SortDetailFields.LABLE, lable)
                    .setField(SortDetailFields.ADDRESS, address)
                    .setField(SortDetailFields.TIME, time)
                    .setField(MultipleFields.ID, id)
                    .setField(MultipleFields.NAME, name)
                    .setField(MultipleFields.TEXT, desc)
                    .setField(SortDetailFields.PRICE, price)
                    .setField(SortDetailFields.HOT, hot)
                    .setField(MultipleFields.IMAGE_URL, imageUrl)
                    .build();
            list.add(entity);
        }
        }
        return list;
    }
}
