package sy.bishe.ygou.ui.recycler;

import java.util.ArrayList;

public abstract class DataConverter {
    protected final ArrayList<MultipleitemEntity> ENTITIES = new ArrayList<>();
    private String sJsonData = null;
    //转换为bean
    public abstract ArrayList<MultipleitemEntity> convert();
    //加载数据
    public DataConverter setsJsonData(String sJsonData) {
        this.sJsonData = sJsonData;
        return this;
    }
    //获取数据

    public String getsJsonData() {
        if (sJsonData == null || sJsonData.isEmpty()){
            throw new NullPointerException("Data is null");
        }
        return sJsonData;
    }
    public void cleaeData(){
        ENTITIES.clear();
    }
}
