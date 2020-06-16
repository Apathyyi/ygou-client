package sy.bishe.ygou.utils.storage;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import sy.bishe.ygou.app.config.YGou;

/**
     * 提示:
     * 【用于Activity内部存储】Activity.getPreferences(int mode)生成 Activity名.xml
     * 【用于数据的保存】PreferenceManager.getDefaultSharedPreferences(Context)生成 “包名_preferences.xml”
     * 【自定义名称】Context.getSharedPreferences(String name,int mode)生成name.xml
     */
    public class YGouPreferences {
        private static final SharedPreferences PREFERENCES =
            PreferenceManager.getDefaultSharedPreferences(YGou.getApplication());

    private static final String APP_PREFERENCES_KEY = "profile";//定义一个key值；

    private static SharedPreferences getAppPreference() {
        return PREFERENCES;
    }

    public static void setAppProfile(String val) {
        getAppPreference()
                .edit()
                .putString(APP_PREFERENCES_KEY, val)
                .apply();
    }

    public static String getAppProfile() {
        return getAppPreference().getString(APP_PREFERENCES_KEY, null);
    }

    public static JSONObject getAppProfileJson() {
        final String profile = getAppProfile();
        return JSON.parseObject(profile);
    }

    public static void removeAppProfile() {
        getAppPreference()
                .edit()
                .remove(APP_PREFERENCES_KEY)
                .apply();
    }

    public static void clearAppPreferences() {
        getAppPreference()
                .edit()
                .clear()
                .apply();
    }

    /**
     * flag：true：第一次存储数据；fasle：非第一次存储数据
     * @param key
     * @param flag
     */
    public static void setAppFlag(String key, boolean flag) {
        getAppPreference()
                .edit()
                .putBoolean(key, flag)
                .apply();
    }

    public static boolean getAppFlag(String key) {
        return getAppPreference()
                .getBoolean(key, false);
    }

    public static void addCustomAppProfile(String key, String val) {
        getAppPreference()
                .edit()
                .putString(key, val)
                .apply();
    }

    public static String getCustomAppProfile(String key) {
        return getAppPreference().getString(key, "");
    }

}