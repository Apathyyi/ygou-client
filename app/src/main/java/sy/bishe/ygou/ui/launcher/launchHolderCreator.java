package sy.bishe.ygou.ui.launcher;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

public class launchHolderCreator implements CBViewHolderCreator<LauncherHolder> {

    @Override
    public LauncherHolder createHolder() {
        return new LauncherHolder();
    }
}
