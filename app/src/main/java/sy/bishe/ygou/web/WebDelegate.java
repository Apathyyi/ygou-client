package sy.bishe.ygou.web;

import android.annotation.SuppressLint;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import sy.bishe.ygou.app.config.ConfigKeys;
import sy.bishe.ygou.app.config.YGou;
import sy.bishe.ygou.delegate.base.YGouDelegate;

public abstract class WebDelegate extends YGouDelegate implements IWebViewInitializer{
    private WebView sWebView = null;
    private final ReferenceQueue<WebView> WEB_VIEW_QUENE = new ReferenceQueue<>();
    private String sUrl = null;
    private boolean sIsinited = false;
    private YGouDelegate sTopDelegate = null;

    public WebDelegate() {
    }
    //子类实现
    public abstract IWebViewInitializer setInitializer();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        sUrl = args.getString(RouteKeys.URL.name());
        initWebView();
    }

    @Override
    public Object setLayout() {
        return null;
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (sWebView != null){
            sWebView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sWebView != null){
            sWebView.onResume();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sIsinited = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sWebView != null){
            sWebView.removeAllViews();
            sWebView.destroy();
            sWebView = null;
        }
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView(){
        if (sWebView != null){
            sWebView.removeAllViews();
            sWebView.destroy();
        }else {
            final IWebViewInitializer initializer = setInitializer();
            if (initializer != null){
                final WeakReference<WebView> webViewWeakReference = new WeakReference<>(new WebView(getContext()),WEB_VIEW_QUENE);
                sWebView = webViewWeakReference.get();
                sWebView = initializer.initWebView(sWebView);
                sWebView.setWebViewClient(initializer.initWebViewClient());
                sWebView.setWebChromeClient(initializer.initWebChromeClient());
                final  String name = YGou.getConfigruration(ConfigKeys.JAVASCRIPT_INTERFACE);
                sWebView.addJavascriptInterface(YGouWebInterface.create(this),name);
                sIsinited = true;
            }else {
                throw new NullPointerException("Initializer is null");
            }
        }
    }
    public void setTopDelegate(YGouDelegate delegate){
        sTopDelegate = delegate;
    }

    public YGouDelegate getsTopDelegate(){
        if (sTopDelegate == null){
            sTopDelegate = this;
        }
        return sTopDelegate;
    }
    public WebView getsWebView(){
        if (sWebView == null){
            throw new NullPointerException("WebView is null");
        }
        return sIsinited ? sWebView:null;
    }

    public String getsUrl() {
        if (sUrl == null){
            throw new NullPointerException("WebView is null");
        }
        return sUrl;
    }
}
