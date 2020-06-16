package sy.bishe.ygou.net.rest;

import android.content.Context;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import sy.bishe.ygou.net.callback.IError;
import sy.bishe.ygou.net.callback.IFailure;
import sy.bishe.ygou.net.callback.IRequest;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.ui.loader.LoaderStyle;

public class RestClientBuilder {
    private  String mUrl =null;
    private  static final Map<String,Object> PARAMS = RestCreator.getParams();
    private IRequest mRequest=null;
    private ISuccess mSuccess=null;
    private IFailure mFailure=null;
    private IError mError=null;
    private RequestBody mBody=null;
    private Context mContext = null;
    private LoaderStyle mLoaderstyle =null;
    private List<File> mlist ;
    private File mfile = null;
    private  String mDownloadDir = null;
    private  String mExtension = null;
    private  String mName = null;
    public RestClientBuilder() {
    }
    public final RestClientBuilder setUrl(String url){
        this.mUrl = url;
        return this;
    }
    public final RestClientBuilder setParams(WeakHashMap<String,Object> params){
        PARAMS.putAll(params);
        return this;
    }
    public final RestClientBuilder setParams(String key,Object val){
        PARAMS.put(key,val);
        return this;
    }
    public final RestClientBuilder setBody(RequestBody requestBody){
       this.mBody = requestBody;
        return this;
    }
    public final RestClientBuilder file(List<File> file){
//        for (File f:file
//             ) {
//            this.mlist.add(f);
//        }
        this.mlist=file;
        return this;
    }
    public final RestClientBuilder file(File file){
       this.mfile=file;
        return this;
    }
    public final RestClientBuilder setRaw(String raw){
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),raw);
        return this;
    }
    public final RestClientBuilder onRequest(IRequest iRequest){
        this.mRequest = iRequest;
        return this;
    }
    public final RestClientBuilder onSuccess(ISuccess iSuccess){
        this.mSuccess = iSuccess;
        return this;
    }
    public final RestClientBuilder failure(IFailure iFailure){
        this.mFailure = iFailure;
        return this;
    }

    public final RestClientBuilder setmDownloadDir(String mDownloadDir) {
        this.mDownloadDir = mDownloadDir;
        return this;
    }

    public final RestClientBuilder setmExtension(String mExtension) {
        this.mExtension = mExtension;
        return this;
    }

    public final RestClientBuilder setmName(String mName) {
        this.mName = mName;
        return this;
    }

    public final RestClientBuilder error(IError iError){
        this.mError = iError;
        return this;
    }
    public final RestClientBuilder loader(Context context, LoaderStyle style){
        this.mContext = context;
        this.mLoaderstyle = style;
        return this;
    }
    public final RestClientBuilder loader(Context context){
        this.mContext = context;
        this.mLoaderstyle = LoaderStyle.BallClipRotatePulseIndicator;
        return this;
    }
    public final RestClient build(){
        return new RestClient(mUrl,PARAMS,mRequest, mDownloadDir, mExtension, mName, mSuccess,mFailure,mError,mBody, mLoaderstyle, mfile,mlist, mContext);
    }
}
