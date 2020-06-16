package sy.bishe.ygou.net.rest;


import java.util.ArrayList;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import sy.bishe.ygou.app.config.ConfigKeys;
import sy.bishe.ygou.app.config.YGou;

public class RestCreator {

    /**
     *
     */
    private static final class RetrofitHolder{
        private static final String BASE_URL = YGou.getConfigruration(ConfigKeys.API_HOST);

        private static final Retrofit RETROFIT_CLIENT= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }


    /**
     * 网络请求
     */
    private static final class OKHttpHolder{
        private static final int TIME_OUT = 60;
        private static final OkHttpClient.Builder  BUILDER = new OkHttpClient.Builder();
        //拦截器
        private static final ArrayList<Interceptor> INTERCEPTORS =  YGou.getConfigruration(ConfigKeys.INTERCEPTOR);

        private static OkHttpClient.Builder addInterceptor(){
            if (INTERCEPTORS != null && !INTERCEPTORS.isEmpty()){
                for (Interceptor interceptor:INTERCEPTORS){
                    BUILDER.addInterceptor(interceptor);
                }
            }
            return BUILDER;
        }
        private static final OkHttpClient OK_HTTP_CLIENT = addInterceptor()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    /**
     *参数
     */
    private static final class ParamsHolder{
        public static final WeakHashMap<String,Object> PARAMS = new WeakHashMap<>();
    }
    public static WeakHashMap<String,Object> getParams(){
        return ParamsHolder.PARAMS;
    }
    public static RestService getRestService(){
        return RestServiceHolder.REST_SERVICE;
    }

    /**
     * 返回服务
     */
    private static final class RestServiceHolder{
        private static final RestService REST_SERVICE = RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }
}
