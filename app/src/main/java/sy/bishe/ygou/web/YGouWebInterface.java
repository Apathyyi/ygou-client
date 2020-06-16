package sy.bishe.ygou.web;


import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSONObject;

final class YGouWebInterface {
    private final WebDelegate DELEAGTE ;

    public YGouWebInterface(WebDelegate deleagte) {
        this.DELEAGTE = deleagte;
    }
    static YGouWebInterface create(WebDelegate delegate){
        return new YGouWebInterface(delegate);
    }
    //action
    @SuppressWarnings("unused")
    @JavascriptInterface
    public String event(String params){
        final String action = JSONObject.parseObject(params).getString("action");
        final Event event = EventManager.getInstance().createEvent(action);
        if (event!=null){
            event.setsAction(action);
            event.setsDelegate(DELEAGTE);
            event.setsContext(DELEAGTE.getContext());
            event.setsUrl(DELEAGTE.getsUrl());
            return event.execute(params);
        }
        return null;
    }
}
