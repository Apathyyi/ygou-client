package sy.bishe.ygou.app.config;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;

import java.util.ArrayList;
import java.util.WeakHashMap;

import okhttp3.Interceptor;
import sy.bishe.ygou.ui.main.MainActivity;
import sy.bishe.ygou.web.Event;
import sy.bishe.ygou.web.EventManager;

public class Configurator {
    /**
     * 配置信息
     */
    private static final WeakHashMap<Object,Object> YIGOU_CONFIG = new WeakHashMap<>();
    //图标配置
    private static final ArrayList<IconFontDescriptor> Icons = new ArrayList<>();
    //拦截器
    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();
    /**
     * 配置开始
     */
    public Configurator() {
        YIGOU_CONFIG.put(ConfigKeys.CONFIG_READY.name(),false);
    }
    /**
     * 初始化
     */
    public void initIcon(){
        if(Icons.size()>0){
            final Iconify.IconifyInitializer iconifyInitializer = Iconify.with(Icons.get(0));
            for (int i=0;i<Icons.size();i++){
                iconifyInitializer.with(Icons.get(i));
            }
        }
    }

    /**
     *
     * @return
     */
    public static Configurator getConfig(){
        return Holder.CFG_INSTANCE;
    }

    /**
     * 配置Activity
     * @param mainActivity
     */
    public final Configurator withActivity(Activity mainActivity) {
        YIGOU_CONFIG.put(ConfigKeys.ACTIVITY,mainActivity);
        return this;
    }

    /**
     * 懒汉式多线程
     *
     */
    private static class Holder{
        private static final Configurator CFG_INSTANCE = new Configurator();
    }

    /**
     * 配置完成
     */
    public final void configure(){
        initIcon();
        YIGOU_CONFIG.put(ConfigKeys.CONFIG_READY.name(),true);
    }

    /**
     * 返回配置
     * @return
     */
    public final WeakHashMap<Object, Object> getYiGouConfig(){
        return YIGOU_CONFIG;
    }

    /**
     * APIhost
     * @param host
     * @return
     */
    public final Configurator WithHost(String host){
        YIGOU_CONFIG.put(ConfigKeys.API_HOST,host);
        return this;
    }
    public final Configurator withIcon(IconFontDescriptor descriptor){
        Icons.add(descriptor);
        return this;
    }

    /**
     * 配置拦截器
     * @param interceptor
     * @return
     */
    public final Configurator withInterceptor(Interceptor interceptor){
        INTERCEPTORS.add(interceptor);
        YIGOU_CONFIG.put(ConfigKeys.INTERCEPTOR,INTERCEPTORS);
        return this;
    }
    public final Configurator withInterceptor(ArrayList<Interceptor> interceptors){
        INTERCEPTORS.addAll(interceptors);
        YIGOU_CONFIG.put(ConfigKeys.INTERCEPTOR,INTERCEPTORS);
        return this;
    }

    /**
     * web
     * @param name
     * @return
     */
    public Configurator withJavascriptInterface(@NonNull String name){
        YIGOU_CONFIG.put(ConfigKeys.JAVASCRIPT_INTERFACE,name);
        return this;
    }
    public Configurator withWebEvent(String name, Event event){
        final EventManager manager = EventManager.getInstance();
        manager.addEvrnt(name,event);
        return this;
    }
    /**
     * 检查是否配置完成
     */
    private void checkConfig(){
        final boolean isChecked = (boolean) YIGOU_CONFIG.get(ConfigKeys.CONFIG_READY.name());
        if(!isChecked){
            throw new RuntimeException("configuration is not ready");
        }
    }

    /**
     *
     * @param
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key){
        checkConfig();
        return (T) YIGOU_CONFIG.get(key);
    }
}
