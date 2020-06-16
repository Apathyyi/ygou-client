package sy.bishe.ygou.delegate.personal.userInfo;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import sy.bishe.ygou.R;
import sy.bishe.ygou.delegate.buttons.MultipleFields;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.recycler.ItemType;
import sy.bishe.ygou.ui.recycler.MulitipleViewHolder;
import sy.bishe.ygou.ui.recycler.MultiRecyclerAdapter;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;

public class AdressAdapter extends MultiRecyclerAdapter {
    /**
     * 默认
     *
     * @param data
     */
    protected AdressAdapter(List<MultipleitemEntity> data) {
        super(data);
        addItemType(ItemType.ITEM_ADRESS, R.layout.item_adress);
    }

    @Override
    protected void convert(MulitipleViewHolder helper, MultipleitemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case ItemType.ITEM_ADRESS:
                final String name = item.getField(MultipleFields.NAME);
                final String phone = item.getField(AdressFields.PHONE);
                final String address = item.getField(AdressFields.ADRESS);
                final String isDefault = item.getField(MultipleFields.TAG);
                final int id = item.getField(MultipleFields.ID);
                final AppCompatTextView viewName = helper.getView(R.id.tv_address_name);
                final AppCompatTextView viewPhone = helper.getView(R.id.tv_address_phone);
                final AppCompatTextView viewAdress = helper.getView(R.id.tv_address_address);
                final AppCompatTextView viewDelete  =helper.getView(R.id.tv_address_delete);
                viewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RestClient.builder()
                                .setUrl("address/delete/"+id)
                                .onSuccess(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        JSONObject jsonObject = JSON.parseObject(response);
                                        String status = jsonObject.getString("status");
                                        if (status.equals("ok")){
                                            remove(helper.getLayoutPosition());
                                        }
                                    }
                                })
                                .build()
                                .post();
                    }
                });
                viewName.setText(name);
                viewPhone.setText(phone);
                viewAdress.setText(address);
                break;
                default:
                    break;

        }
    }
}
