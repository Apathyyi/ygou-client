package sy.bishe.ygou.net.rest;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import sy.bishe.ygou.net.callback.IError;
import sy.bishe.ygou.net.callback.IFailure;
import sy.bishe.ygou.net.callback.IRequest;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.callback.RequestCallbacks;
import sy.bishe.ygou.ui.loader.LoaderStyle;
import sy.bishe.ygou.ui.loader.YGouLoader;

public class RestClient {
    private final String URL;
    private final WeakHashMap<String,Object> PARAMS = RestCreator.getParams();
    private final IRequest REQUEST;
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final RequestBody BODY;
    private final LoaderStyle LOADER_STYLE;
    private final List<File> FILELIST;
    private final File FILE;
    private final Context CONTEXT;

    public RestClient(String url, Map<String, Object> params, IRequest request, String download_dir, String extension, String name, ISuccess success, IFailure failure, IError error, RequestBody body, LoaderStyle loader_style, File file,List<File> list, Context context) {
        this.URL = url;
        this.DOWNLOAD_DIR = download_dir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.PARAMS.putAll(params);
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;
        this.CONTEXT = context;
        this.FILE = file;
        this.FILELIST =list;
        this.LOADER_STYLE = loader_style;
    }
    public static RestClientBuilder builder(){
        return new RestClientBuilder();
    }

    /**
     *request请求
     * @param method
     */
    private void request(HttpMethod method){
        final RestService service = RestCreator.getRestService();
        Call<String> call = null;
        if (REQUEST!=null){
            REQUEST.onRequestStart();
        }
        if (LOADER_STYLE!=null){
            YGouLoader.showLoading(CONTEXT,LOADER_STYLE);
        }
        switch (method){
            case GET:call = service.get(URL,PARAMS);
            break;
            case POST:call = service.post(URL,PARAMS);
            break;
            case POST_RAW:call = service.postRaw(URL,BODY);
            break;
            case PUT_RAW:call = service.putRaw(URL,BODY);
            break;
            case PUT:call=service.put(URL,PARAMS);
            break;
            case DELETE:call = service.delete(URL,PARAMS);
            case UPLOAD:
                int i = 0;
                Map<String, RequestBody> params = new HashMap<>();
                for (File file:FILELIST){
                    RequestBody requestBody = RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()),file);
                    params.put("AttachmentKey\"; filename=\"" +file.getName(),requestBody);
                    Log.i("", "request: "+file.getName());
                    i++;
                }
                call = RestCreator.getRestService().upLoad(URL,params);
                break;
            default:
                break;
        }
        if (call!=null){
            call.enqueue(getRequestCallback());
        }
    }

    /**
     * 回调
     * @return
     */
    private Callback getRequestCallback(){
        return new RequestCallbacks(
                REQUEST,
                SUCCESS,
                FAILURE,
                ERROR,
                LOADER_STYLE
        );
    }

    /**
     * get请求
     */
    public final void get(){
        request(HttpMethod.GET);
    }

    /**
     * post请求
     */
    public final void post(){
        if (BODY == null){
            request(HttpMethod.POST);
        }else {
            if (!PARAMS.isEmpty()){
                throw new RuntimeException("params must be null");
            }
            request(HttpMethod.POST_RAW);
        }
    }

    /**
     * put请求
     */
    public final void put(){
        if (BODY == null){
            request(HttpMethod.PUT);
        }else {
            if (!PARAMS.isEmpty()){
                throw new RuntimeException("params must be bull");
            }
            request(HttpMethod.PUT);
        }
    }
    /**
     *
     */
    public final void upload(){
        request(HttpMethod.UPLOAD);
    }

}
