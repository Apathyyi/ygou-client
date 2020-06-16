package sy.bishe.ygou.net.callback;

import android.os.Handler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sy.bishe.ygou.ui.loader.LoaderStyle;
import sy.bishe.ygou.ui.loader.YGouLoader;

/**
 * 回调
 */
public class RequestCallbacks implements Callback<String> {
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final LoaderStyle LOADER_STYLE;
    private static final Handler HANDLER = new Handler();

    public RequestCallbacks(IRequest REQUEST, ISuccess SUCCESS, IFailure FAILURE, IError ERROR, LoaderStyle loader_style) {
        this.REQUEST = REQUEST;
        this.SUCCESS = SUCCESS;
        this.FAILURE = FAILURE;
        this.ERROR = ERROR;
        this.LOADER_STYLE = loader_style;
    }
    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()){
            if (call.isExecuted()){
                if (SUCCESS!=null){
                    SUCCESS.onSuccess((response.body()));
                }
            }
        }else {
            if (ERROR!=null){
                ERROR.onError(response.code(),response.message());
            }
        }
        if (LOADER_STYLE!=null){
            HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    YGouLoader.stopLoading();
                }
            },1000);
        }
        stopLoading();
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        t.printStackTrace();
        if (FAILURE!=null){
            FAILURE.onFailure();
        }
        if (REQUEST!=null){
            REQUEST.onRequestEnd();
        }
        stopLoading();
    }

    private void stopLoading() {
        if (LOADER_STYLE!=null){
            HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    YGouLoader.stopLoading();
                }
            },1000);
        }
    }
}
