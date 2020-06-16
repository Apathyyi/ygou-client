package sy.bishe.ygou.delegate.base;

public abstract class YGouDelegate extends PermissionCheckDekegate {
    @SuppressWarnings("unchecked")
    public <T extends YGouDelegate> T getParentDelegate(){
        return (T) getParentFragment();
    }
}
