package sy.bishe.ygou.delegate.sort;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public final class VerticalDataConverter extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final ArrayList<MultipleitemEntity> list = new ArrayList<>();
        final JSONArray jsonArray = JSONObject.parseObject(getsJsonData())
                .getJSONArray("data");
        Log.i("VerticalDataConverter", jsonArray.toJSONString());
        final int size = jsonArray.size();
        for (int i = 0;i<size;i++){
            final JSONObject data = jsonArray.getJSONObject(i);
            int id = data.getInteger("vertical_id");
            String name = data.getString("vertical_name");
            final MultipleitemEntity entity = MultipleitemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, ItemType.VERTICAL_LIST)
                    .setField(MultipleFields.ID,id)
                    .setField(MultipleFields.TEXT,name)
                    .setField(MultipleFields.TAG,false)
                    .build();
            list.add(entity);
            //默认设置第一个被选中
            list.get(0).setField(MultipleFields.TAG,true);
        }
        return list;
    }
}
