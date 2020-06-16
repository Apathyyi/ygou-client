package sy.bishe.ygou.delegate.sort;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import sy.bishe.ygou.bean.SectionBean;
import sy.bishe.ygou.delegate.base.YGouDelegate;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.delegate.detail.GoodsDetailDelegate;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class SortItemClickListener extends SimpleClickListener {

    private  final  YGouDelegate DELEGATE;

    public SortItemClickListener(YGouDelegate delegate) {
        this.DELEGATE = delegate;
    }
    public static SimpleClickListener create(YGouDelegate delegate){
        return new SortItemClickListener(delegate);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        //entity
        final SectionBean sectionBean = (SectionBean) baseQuickAdapter.getData().get(position);
        //获取分类的id
        final int sortId = sectionBean.getEntity().getsGoodsId();
        Log.i("sortId****", sortId+"");

        if (sortId==37){
            DELEGATE.getParentDelegate().getSupportDelegate().start(new ForBuyDelegate());
        }else {
            String brandName = sectionBean.getEntity().getsGoodsName();
            Log.i("getData****", sectionBean.getEntity().getsGoodsName());
            //根据id和品牌名称获取相应的数据
            final SortDetailDelegate sortDetailDelegate = SortDetailDelegate.create(sortId,brandName);
            DELEGATE.getParentDelegate().getSupportDelegate().start(sortDetailDelegate);
        }
        //获取品牌名称

//        DELEGATE.start(sortDetailDelegate);
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        //entity
        final MultipleitemEntity entity = (MultipleitemEntity) baseQuickAdapter.getData().get(position);
        final int goodsId = Integer.parseInt(entity.getField(MultipleFields.ID));
        Log.i("goodsId:", ""+goodsId+"****position:"+position);
        final GoodsDetailDelegate detailDelegate = GoodsDetailDelegate.create(goodsId);
        detailDelegate.getSupportDelegate().start(detailDelegate);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
