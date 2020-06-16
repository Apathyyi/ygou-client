package sy.bishe.ygou.web;

import android.content.Context;
import android.webkit.WebView;

public abstract class Event implements IEvent {

    private Context sContext = null;
    private String sAction = null;
    private WebDelegate sDelegate = null;
    private String sUrl = null;
    private WebView sWebView = null;

    public Context getsContext() {
        return sContext;
    }

    public void setsContext(Context sContext) {
        this.sContext = sContext;
    }

    public WebView getsWebView() {
        return sWebView;
    }

    public String getsAction() {
        return sAction;
    }

    public void setsAction(String sAction) {
        this.sAction = sAction;
    }

    public WebDelegate getsDelegate() {
        return sDelegate;
    }

    public void setsDelegate(WebDelegate sDelegate) {
        this.sDelegate = sDelegate;
    }

    public String getsUrl() {
        return sUrl;
    }

    public void setsUrl(String sUrl) {
        this.sUrl = sUrl;
    }

    @Override
    public String execute(String params) {
        return null;
    }

}
