package sy.bishe.ygou.net.download;

import android.os.AsyncTask;

import java.util.WeakHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sy.bishe.ygou.net.rest.RestCreator;
import sy.bishe.ygou.net.callback.IError;
import sy.bishe.ygou.net.callback.IFailure;
import sy.bishe.ygou.net.callback.IRequest;
import sy.bishe.ygou.net.callback.ISuccess;

public class DownloadHandler {
        private final String URL;
        private static final WeakHashMap<String,Object> PARAMS = RestCreator.getParams();
        private final IRequest REQUEST;
        private final String DOWNLOAD_DIR;
        private final String EXTENSION;
        private final String NAME;
        private final ISuccess SUCCESS;
        private final IFailure FAILURE;
        private final IError ERROR;

    public DownloadHandler(String url, IRequest request, String download_dir, String extension, String name, ISuccess success, IFailure failure, IError error) {
        this.URL = url;
        this.REQUEST = request;
        this.DOWNLOAD_DIR = download_dir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
    }

    public final void handleDownload() {
        if (REQUEST!=null){
            REQUEST.onRequestStart();
        }
        RestCreator.getRestService().downLoad(URL,PARAMS)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            final ResponseBody responseBody = response.body();
                            final SaveFileTask task = new SaveFileTask(REQUEST,SUCCESS);
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,DOWNLOAD_DIR,EXTENSION,responseBody,NAME);
                            if (task.isCancelled()){
                                if (REQUEST != null){
                                    REQUEST.onRequestEnd();
                                }
                            }else {
                                if (ERROR != null){
                                    ERROR.onError(response.code(),response.message());
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (FAILURE != null){
                            FAILURE.onFailure();
                        }
                    }
                });
    }
}
