package sy.bishe.ygou.delegate.friends.chat;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.ui.recycler.DataConverter;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class ChatDetailDataConvert extends DataConverter {
    @Override
    public ArrayList<MultipleitemEntity> convert() {
        final ArrayList<MultipleitemEntity> list = new ArrayList<>();

        Log.i("getsJsonData", "convert: "+getsJsonData());
        JSONArray messageArray = JSONArray.parseArray(getsJsonData());
        if (!messageArray.isEmpty()){
            final int size = messageArray.size();
            for (int i = 0; i < size; i++) {
                final JSONObject meeageobject = messageArray.getJSONObject(i);

                final String text = meeageobject.getString("text");
                final String time = meeageobject.getString("time");

                final String fromName = meeageobject.getString("from_name").substring(5);

                final String targetName = meeageobject.getString("target_name").substring(5);

                final String type = meeageobject.getString("type");
                final MultipleitemEntity entity = MultipleitemEntity.builder()
                        .setField(MultipleFields.ITEM_TYPE, ItemType.CHATDETAIL)
                        .setField(ChatFields.TYPE,type)
                        .setField(ChatFields.TARGETNAME,targetName)
                        .setField(ChatFields.TYPE,text)
                        .setField(ChatFields.CONTENT,text)
                        .setField(ChatFields.TIME,time)
                        .setField(ChatFields.FROMNAME,fromName)
                        .build();
                list.add(entity);
            }
        }
        return list;
    }
}
