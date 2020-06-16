package sy.bishe.ygou.net.download;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import java.io.File;
import java.io.InputStream;
import okhttp3.ResponseBody;
import sy.bishe.ygou.app.config.YGou;
import sy.bishe.ygou.net.callback.IRequest;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.utils.file.FileUtil;

final class SaveFileTask extends AsyncTask<Object,Void, File> {

    private final IRequest REQUEST;
    private final ISuccess SUCCESS;

    SaveFileTask(IRequest request, ISuccess success) {
        this.REQUEST = request;
        this.SUCCESS = success;
    }

    @Override
    protected File doInBackground(Object... params) {
        String downloadDir = (String) params[0];
        String extension = (String) params[1];
        final ResponseBody body = (ResponseBody) params[2];
        final String name = (String) params[3];
        final InputStream inputStream = body.byteStream();
        if (downloadDir == null || downloadDir.equals("")) {
            downloadDir = "down_loads";
        }
        if (extension == null || extension.equals("")) {
            extension = "";
        }
            if (name == null) {
                return FileUtil.writeToDisk(inputStream, downloadDir, extension.toUpperCase(), extension);
            } else {
                return FileUtil.writeToDisk(inputStream, downloadDir, name);
            }
        }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (SUCCESS != null){
            SUCCESS.onSuccess(file.getPath());
        }
        if (REQUEST != null){
            REQUEST.onRequestEnd();
        }
        autoInstallApk(file);
    }

    private void autoInstallApk(File file) {
        if (FileUtil.getExtension(file.getPath()).equals("apk")){
            final Intent install = new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(file),"application/vnd/android.package.archive");
            YGou.getApplication().startActivity(install);
        }
    }
}
