package sy.bishe.ygou.web;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public interface IWebViewInitializer {
    //初始化webview
    WebView initWebView(WebView webView);
    //初始化浏览器
    WebViewClient initWebViewClient();
    //页面控制
    WebChromeClient initWebChromeClient();
}
