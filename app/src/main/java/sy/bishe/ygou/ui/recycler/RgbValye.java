package sy.bishe.ygou.ui.recycler;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RgbValye {
    public abstract int red();
    public abstract int green();
    public abstract int blue();
    public static RgbValye create(int red,int green,int blue){
        return new AutoValue_RgbValye(red,green,blue);
    }
}
