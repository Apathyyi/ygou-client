package sy.bishe.ygou.web;

import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

public class TestEvent extends Event {
    @Override
    public String execute(String params) {
        Toast.makeText(getsContext(),params,Toast.LENGTH_SHORT).show();
        Log.i("ACTION", params);
        if (getsAction().equals("item_dynamic")){
            final WebView webView = getsWebView();
            if (webView!=null) {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.evaluateJavascript("nativeCall():", null);
                    }
                });
            }
        }
        return null;
    }
}
