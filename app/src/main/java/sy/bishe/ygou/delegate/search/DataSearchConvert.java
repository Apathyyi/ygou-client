package sy.bishe.ygou.delegate.search;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class DataSearchConvert extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final JSONArray jsonArray = JSON.parseObject(getsJsonData()).getJSONArray("data");
        if (!jsonArray.isEmpty()){
            final int size = jsonArray.size();
            for (int i = 0;i<size;i++) {
                final JSONObject data = jsonArray.getJSONObject(i);
                final String name = data.getString("goodsinfo_name");
                final String label = data.getString("goodsinfo_lable");
                final String id = data.getString("goodsinfo_id");
                final MultipleitemEntity entity = MultipleitemEntity.builder()
                        .setItemType(ItemType.DATA_SEARCH)
                        .setField(MultipleFields.NAME,name)
                        .setField(MultipleFields.ID, id)
                        .build();
                if (!label.isEmpty()) {
                    String[] split = label.split("-");
                    int labelsize = split.length;
                    Log.d("split", "convert: "+split.toString()+"************length"+labelsize);
                    if (labelsize == 1) {
                        entity.setFields(SearchFields.LABEL0, split[0]);
                        entity.setFields(SearchFields.LABEL1,"");
                        entity.setFields(SearchFields.LABEL2, "");
                    }
                    if (labelsize == 2) {
                        entity.setFields(SearchFields.LABEL0, split[0]);
                        entity.setFields(SearchFields.LABEL1, split[1]);
                        entity.setFields(SearchFields.LABEL2, "");
                    }
                    if (labelsize == 3) {
                        entity.setFields(SearchFields.LABEL0, split[0]);
                        entity.setFields(SearchFields.LABEL1, split[1]);
                        entity.setFields(SearchFields.LABEL2, split[2]);
                    }
                }
                ENTITIES.add(entity);
            }
        }
        return ENTITIES;
    }
}
