package sy.bishe.ygou.web;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

public class WebDelegateImpl extends WebDelegate {
    private IPageLoadListener loadListener = null;

    public void setLoadListener(IPageLoadListener loadListener) {
        this.loadListener = loadListener;
    }

    public static WebDelegateImpl create(String url){
        final Bundle args = new Bundle();
        args.putString(RouteKeys.URL.name(),url);
        final WebDelegateImpl delegate = new WebDelegateImpl();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public IWebViewInitializer setInitializer() {
        return this;
    }

    @Override
    public Object setLayout() {
        return getsWebView();
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        if (getsUrl()!=null){
            Router.getInstance().loadPage(this,getsUrl());
        }
    }

    @Override
    public WebView initWebView(WebView webView) {
        return new WebViewInitializer().createWebView(webView);
    }

    @Override
    public WebViewClient initWebViewClient() {
        final WebViewClientImpl client = new WebViewClientImpl(this);
        client.setLoadListener(loadListener);
        return client;
    }

    @Override
    public WebChromeClient initWebChromeClient() {
        return new WebChromClientImpl();
    }
}
