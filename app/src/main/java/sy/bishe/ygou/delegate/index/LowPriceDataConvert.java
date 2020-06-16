package sy.bishe.ygou.delegate.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.delegate.sort.SortDetailFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class LowPriceDataConvert extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final ArrayList<MultipleitemEntity> list = new ArrayList<>();
        JSONArray users = JSON.parseObject(getsJsonData()).getJSONObject("jsonObject").getJSONArray("users");
        final JSONArray jsonArray = JSON.parseObject(getsJsonData()).getJSONArray("data");
        final int size = jsonArray.size();
        for (int i = 0;i<size;i++){
            JSONObject user = users.getJSONObject(i);
            final String userImg = user.getString("user_img");
            final String check  = user.getString("user_isidentificated");
            final JSONObject data = jsonArray.getJSONObject(i);
            final String imageUrl = data.getString("goodsinfo_thumb");
            final String name = data.getString("goodsinfo_name");
            final String id = data.getString("goodsinfo_id");
            final String price = data.getString("goodsinfo_price");
            final String hot = data.getString("goodsinfo_hot");
            final String userName = data.getString("goodsinfo_userName");
            //更具userName查找userimg
            final MultipleitemEntity entity = MultipleitemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, ItemType.LOWPRICE)
                    .setField(MultipleFields.ID,id)
                    .setField(MultipleFields.NAME,name)
                    .setField(IndexFileds.USERNAME,userName)
                    .setField(SortDetailFields.PRICE,price)
                    .setField(SortDetailFields.HOT,hot)
                    .setField(MultipleFields.IMAGE_URL,imageUrl)
                    .setField(IndexFileds.USERIMG,userImg)
                    .setField(IndexFileds.CHECK,check)
                    .build();
            list.add(entity);
        }
        return list;
    }
}
