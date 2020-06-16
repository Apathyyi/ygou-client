package sy.bishe.ygou.app.config;

import android.content.Context;
import android.util.Log;

import java.util.WeakHashMap;
import java.util.logging.Handler;

/**
 * 获取配置信息
 */
public class YGou {
    //全局上下文
    public static Configurator init(Context context){
        Configurator.getConfig().getYiGouConfig().put(ConfigKeys.APPLICATION_CONTEXT, context.getApplicationContext());
        Log.i("APPLICATION_CONTEXT",context.getApplicationContext().toString());
        return Configurator.getConfig();
    }
    public static WeakHashMap<Object, Object> getConfigurations(){
        return Configurator.getConfig().getYiGouConfig();
    }
    public static <T> T getConfigruration(Object key){
        return Configurator.getConfig().getConfiguration((Enum<ConfigKeys>) key);
    }
    public static Context getApplication(){
        return Configurator.getConfig().getConfiguration(ConfigKeys.APPLICATION_CONTEXT);
    }

    public static Configurator getConfigurator() {
        return Configurator.getConfig();
    }

    public static Handler getHandler() {
        return null;
    }
}
