package sy.bishe.ygou.icon;

import com.joanzapata.iconify.Icon;

public enum Ecicons implements Icon {
    icon_scan('\ue602'),
    icon_pay('\ue606'),
    icon_alipay('\ue612'),
    icon_news('\ue8bd'),
    icon_add('\ue8c0')
    ;
    private char character;
    Ecicons(char character) {
        this.character=character;
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public char character() {
        return 0;
    }
}
