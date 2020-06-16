package sy.bishe.ygou.app.appication;

import android.app.Application;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.mob.MobSDK;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import okhttp3.MediaType;
import sy.bishe.ygou.app.config.YGou;
import sy.bishe.ygou.icon.FontEcModule;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.web.TestEvent;

/**
 * 初始化开始的application
 */
public class YGouApp extends Application {
    //城市
    public static final List<String> CITYS = new ArrayList<>();
    public static final List<List<String>> provinces = new ArrayList<>();
    public static final String APP_NAME = "YGou_";
    public static YGouApp app;
    //    public static DaoSession daoSession;
    public static String WX_APPID = "wxa215e644f9bea11e";
    public static String WX_APPSercret = "9cbf80f070f713ae10189f436eed0f55";
    public static IWXAPI iwxapi;
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        //初始化极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JMessageClient.setDebugMode(true);
        JMessageClient.init(this, true);
        //初始化微信
        iwxapi = WXAPIFactory.createWXAPI(this, WX_APPID, true);
        iwxapi.registerApp(WX_APPID);
        YGou.init(this)
                // .WithHost("http://192.168.43.191:8080/")
                .WithHost("http://192.168.43.191:8089/")
                .withIcon(new FontAwesomeModule())
                .withIcon(new FontEcModule())
                .withJavascriptInterface("YGou")
                .withWebEvent("item_dynamic", new TestEvent())
                //.withInterceptor(new DebugInterceptor("index", R.raw.item_dynamic))
                .configure();
        //初始化短信验证
        MobSDK.init(this);
        CITYS.add("全国");
        RestClient.builder()
                .setUrl("city/query")
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject = JSONObject.parseObject(response).getJSONObject("jsonObject");
                        Set<String> strings = jsonObject.keySet();
                        Iterator<String> iterator = strings.iterator();
                        while (iterator.hasNext()){
                            String next = iterator.next();
                            Log.d("next", next);
                            CITYS.add(next);
                        }
                    }
                })
                .build()
                .get();
        //初始化greenDao
//        DaoMaster.DevOpenHelper helper;
//        helper = new DaoMaster.DevOpenHelper(this,"YGou_DataBase");
//        Database database = helper.getWritableDb();
//        daoSession = new DaoMaster(database).newSession();
    }
//    public static DaoSession getDaoSession(){
//        return daoSession;
//    }
}
