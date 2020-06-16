package sy.bishe.ygou.delegate.friends.chat;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class ChatDataConvert extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final ArrayList<MultipleitemEntity> list = new ArrayList<>();
        Log.i("getsJsonData", "convert: "+getsJsonData());
        JSONArray times = JSONObject.parseObject(getsJsonData()).getJSONArray("times");
        JSONArray texts = JSONObject.parseObject(getsJsonData()).getJSONArray("texts");
        JSONArray jsonArray = JSONObject.parseObject(getsJsonData()).getJSONArray("datas");
        JSONArray counts = JSONObject.parseObject(getsJsonData()).getJSONArray("counts");
        final int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject object = jsonArray.getJSONObject(i);
            final String id = object.getString("user_id");
            final String imgUrl  =object.getString("user_img");
            final String name = object.getString("user_name");
            String time = times.getString(i);
            String text = texts.getString(i);
            int count = counts.getInteger(i);
            final MultipleitemEntity entity = MultipleitemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, ItemType.CHAT)
                    .setField(MultipleFields.ID,id)
                    .setField(MultipleFields.IMAGE_URL,imgUrl)
                    .setField(MultipleFields.NAME,name)
                    .setField(ChatFields.TIME,time)
                    .setField(ChatFields.CONTENT,text)
                    .setField(ChatFields.COUNT,count)
                   // .setField(ChatFields.TIME,time)
                   // .setField(MultipleFields.TEXT,content)
                    .setField(ChatFields.POSITION,i)
                    .build();
            list.add(entity);
        }
        return list;
    }
}
