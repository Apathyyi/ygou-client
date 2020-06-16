package sy.bishe.ygou.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

import sy.bishe.ygou.delegate.sort.SectionContentItemEntity;

public class SectionBean extends SectionEntity<SectionContentItemEntity> {
    private boolean sIsmore = false;
    private int sId = -1;
    private SectionContentItemEntity entity = null;

    public SectionContentItemEntity getEntity() {
        return entity;
    }

    public SectionBean(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public SectionBean(SectionContentItemEntity sectionContentItemEntity) {
        super(sectionContentItemEntity);
        entity = sectionContentItemEntity;
    }
    public boolean isMore(){
        return sIsmore;
    }
    public void setsIsmore(boolean ismore){
        this.sIsmore = ismore;
    }
    public int getsId(){
        return sId;
    }
    public void setsId(int id){
        this.sId = id;
    }

}
