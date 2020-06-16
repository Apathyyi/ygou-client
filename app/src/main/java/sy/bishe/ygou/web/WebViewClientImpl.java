package sy.bishe.ygou.web;

import android.graphics.Bitmap;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.logging.Handler;

import sy.bishe.ygou.app.config.YGou;
import sy.bishe.ygou.ui.loader.YGouLoader;
import sy.bishe.ygou.utils.logger.YGouLogger;

public class WebViewClientImpl extends WebViewClient {
    private final WebDelegate DELEAGTE;
    private IPageLoadListener sLoadListener = null;
    private static final Handler HANDLER = YGou.getHandler();

    public WebViewClientImpl(WebDelegate deleagte) {
        DELEAGTE = deleagte;
    }

    public void setLoadListener(IPageLoadListener loadListener){
        this.sLoadListener = loadListener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
    }

    //兼容
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        YGouLogger.d("shouldOverrideUrlLoading",url);
        return Router.getInstance().handleWebUrl(DELEAGTE,url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (sLoadListener != null){
            sLoadListener.onLoadStart();;
        }
        YGouLoader.showLoading(view.getContext());
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (sLoadListener != null){
            sLoadListener.onLoadEnd();
        }
    }
}
