package sy.bishe.ygou.web;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.URLUtil;
import android.webkit.WebView;

import androidx.core.content.ContextCompat;

import sy.bishe.ygou.delegate.base.YGouDelegate;

class Router {
    public Router() {
    }
    private static class Holder{
        private static final Router INSATNCE = new Router();
    }
    public static Router getInstance() {
        return Holder.INSATNCE;
    }
    public final boolean handleWebUrl(WebDelegate delegate,String url){
        //电话
        if (url.contains("tel:")) {
            callPhone(delegate.getContext(), url);
            return true;
        }
          final YGouDelegate topDelegate = delegate.getsTopDelegate();
          final WebDelegateImpl webDelegate = WebDelegateImpl.create(url);
          topDelegate.start(webDelegate);
//        final YGouDelegate parentDelegate = delegate.getParentDelegate();
//        final WebDelegateImpl webDelegate = WebDelegateImpl.create(url);
//        if (parentDelegate == null){
//            delegate.start(webDelegate);
//        }else {
//            parentDelegate.start(webDelegate);
//        }
        return true;
    }
    //加载Webview
    private void loadWebPage(WebView webView,String url){
        if (webView != null){
            webView.loadUrl(url);
        }else {
            throw new NullPointerException("WebView is null");
        }
    }
    //加载本地页面
    private void loadLocalWebPage(WebView webView,String url){
            loadWebPage(webView,"file:///android_asset/html/" +url);
             Log.i("WEBURL", "html/" +url);
    }
    private void loadPage(WebView webView,String url){
        if (URLUtil.isNetworkUrl(url) || URLUtil.isAssetUrl(url)){
            loadWebPage(webView,url);
        }
        else {
            loadLocalWebPage(webView, url);
        }
    }
    public final void loadPage(WebDelegate delegate,String url){
        loadPage(delegate.getsWebView(),url);
    }

    private void callPhone(Context context, String url) {
        final Intent intent = new Intent(Intent.ACTION_DIAL);
        final Uri data = Uri.parse(url);
        intent.setData(data);
        ContextCompat.startActivity(context,intent,null);
    }


}
