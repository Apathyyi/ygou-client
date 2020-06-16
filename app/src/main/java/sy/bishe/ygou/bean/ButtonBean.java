package sy.bishe.ygou.bean;

public class ButtonBean {
    private final CharSequence ICON;
    private final CharSequence TITLE;

    public ButtonBean(CharSequence icon, CharSequence title) {
        this.ICON = icon;
        this.TITLE = title;
    }

    public CharSequence getICON() {
        return ICON;
    }

    public CharSequence getTITLE() {
        return TITLE;
    }
}
