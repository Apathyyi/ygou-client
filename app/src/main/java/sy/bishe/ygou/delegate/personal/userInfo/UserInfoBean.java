package sy.bishe.ygou.delegate.personal.userInfo;

import android.widget.CompoundButton;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import sy.bishe.ygou.delegate.base.YGouDelegate;

public class UserInfoBean implements MultiItemEntity {

    private int sItemType;
    private String sImageUrl = null;
    private String sText = null;
    private String sValue = null;
    private int sId = 0;
    private YGouDelegate sDelegate = null;
    private CompoundButton.OnCheckedChangeListener sOnCheckedChangeListener = null;

    public UserInfoBean(int sItemType, String sImageUrl, String sText, String sValue, int sId, YGouDelegate sDelegate, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.sItemType = sItemType;
        this.sImageUrl = sImageUrl;
        this.sText = sText;
        this.sValue = sValue;
        this.sId = sId;
        this.sDelegate = sDelegate;
        this.sOnCheckedChangeListener = onCheckedChangeListener;
    }

    /**
     * 建造者
     */
    public static final class Builder {
        private int itemType = 0;
        private String imageUrl = null;
        private String text = null;
        private String value = null;
        private int id = 0;
        private YGouDelegate delegate = null;
        private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = null;
        public Builder setsItemType(int sItemType) {
            this.itemType = sItemType;
            return this;
        }

        public Builder setsImageUrl(String sImageUrl) {
            this.imageUrl = sImageUrl;
            return this;
        }

        public Builder setsText(String sText) {
            this.text = sText;
            return this;
        }

        public Builder setsValue(String sValue) {
            this.value = sValue;
            return this;
        }

        public Builder setsId(int sId) {
            this.id = sId;
            return this;
        }

        public Builder setsDelegate(YGouDelegate sDelegate) {
            this.delegate = sDelegate;
            return this;
        }

        public Builder setsOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener sOnCheckedChangeListener) {
            this.onCheckedChangeListener = sOnCheckedChangeListener;
            return this;
        }

        public UserInfoBean build() {
            return new UserInfoBean(itemType,  imageUrl,  text,  value,  id,  delegate, onCheckedChangeListener);
            }
        }

    public int getsItemType() {
        return sItemType;
    }

    public String getsImageUrl() {
        return sImageUrl;
    }

    public String getsText() {
        return sText;
    }

    public String getsValue() {
        return sValue;
    }

    public int getsId() {
        return sId;
    }

    public YGouDelegate getsDelegate() {
        return sDelegate;
    }

    public CompoundButton.OnCheckedChangeListener getOnCheckedChangeListener() {
        return sOnCheckedChangeListener;
    }


    @Override
    public int getItemType() {
        return 0;
    }
}
