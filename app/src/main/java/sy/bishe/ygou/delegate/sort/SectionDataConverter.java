package sy.bishe.ygou.delegate.sort;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sy.bishe.ygou.bean.SectionBean;

public class SectionDataConverter  {

    final List<SectionBean> convert(String json) {
        final List<SectionBean> dataList = new ArrayList<>();
        final JSONArray dataArray = JSONObject.parseObject(json).getJSONArray("data");
        Log.i("SectionDataConverter",dataArray.toJSONString() );
        final int size = dataArray.size();
        for (int i = 0;i<size;i++){
            final JSONObject data = dataArray.getJSONObject(i);
            final int id = data.getInteger("content_id");
            final String title = data.getString("content_section");
            //添加title
            final SectionBean sectionBean = new SectionBean(true,title);
            sectionBean.setsId(id);
            sectionBean.setsIsmore(true);
            dataList.add(sectionBean);
            //解析
            final JSONArray goods = data.getJSONArray("brandBean");
            final int goodSize = goods.size();
            //商品内容获取
            for (int j =0;j<goodSize;j++){
                final JSONObject contentItem = goods.getJSONObject(j);
                final int goodsId = contentItem.getInteger("brand_sortId");
                final String goodsName = contentItem.getString("brand_name");
                final String goodsThumb = contentItem.getString("brand_img");
                Log.i("goodsThumb", goodsThumb);
                //获取内容
             final SectionContentItemEntity entity = new SectionContentItemEntity();
             entity.setsGoodsId(goodsId);
             entity.setsGoodsName(goodsName);
             entity.setsGoodsThumb(goodsThumb);
             //添加到list
             dataList.add(new SectionBean(entity));
            }
            //商品循环
        }
        //Section循环结束
        return dataList;
    }
}
