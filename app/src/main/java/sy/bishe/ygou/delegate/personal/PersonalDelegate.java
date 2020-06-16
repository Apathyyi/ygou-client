package sy.bishe.ygou.delegate.personal;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.delegate.buttons.ButtonItemDelegate;
import sy.bishe.ygou.delegate.personal.bottom.PersonalBottomAdapter;
import sy.bishe.ygou.delegate.personal.bottom.PersonalBottomConverter;
import sy.bishe.ygou.delegate.personal.myrelease.MyReleaseDelegate;
import sy.bishe.ygou.delegate.personal.order.OrderListDelegate;
import sy.bishe.ygou.delegate.personal.showorder.ShowOrderDelegate;
import sy.bishe.ygou.delegate.personal.userInfo.UserInfoDelegate;
import sy.bishe.ygou.net.callback.IFailure;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.ui.recycler.MultipleitemEntity;
import sy.bishe.ygou.utils.storage.YGouPreferences;


//个人中心
public class PersonalDelegate extends ButtonItemDelegate {

    //订单类型·
    public static final String ORDER_TYPE = "ORDER_TYPE";

    private Bundle bundle = null;

    private PersonalBottomAdapter sAdapter = null;

    String mPhtotPath;
    Uri uriImage;
    File mPhotoFile = null;

    File mFile = null;

    @BindView(R2.id.rv_personal_bottom)
    RecyclerView sRvSetting = null;

    //微信分享
    private  SendMessageToWX.Req req = new SendMessageToWX.Req();
    //消息
    @OnClick(R2.id.my_message)
    void goMymeaage(){

        getParentDelegate().getSupportDelegate().start(new FriendDelegate());
    }

    /**
     * 我的发布
     */
    @OnClick(R2.id.go_my_release)
    void goMyRelease(){
        getParentDelegate().getSupportDelegate().start(new MyReleaseDelegate());
    }

    @OnClick(R2.id.tv_all_order)
    /**
     *所以普订单
     */
    void onClickAllOrder(){
        bundle.putString(ORDER_TYPE,"all");
        startOrderListByType();
    }

    /**
     */
    @BindView(R2.id.img_user_avatar)
    CircleImageView img_avater = null;


    @OnClick(R2.id.img_user_avatar)
    /**
     * 修改头像
     */
    void changeAvater(){
        selectPic();
    }
    /**
     * 设置
     */
    @OnClick(R2.id.icon_setting)
    void onClickSetting(){
        getParentDelegate().getSupportDelegate().start(new UserInfoDelegate());
    }

    /**
     * 已评价
     */
    @OnClick(R2.id.ll_hasevl)
    void onClickPay(){
        bundle.putString(ORDER_TYPE,"hasevl");
        startOrderListByType();
    }

    /**
     * 待发货
     */
    @OnClick(R2.id.ll_order_send)
    void onClicksend(){
        bundle.putString(ORDER_TYPE,"wait");
        startOrderListByType();
    }

    /**
     * 待收货
     */
    @OnClick(R2.id.ll_receive)
    void onClickrecive(){
        bundle.putString(ORDER_TYPE,"receive");
        startOrderListByType();
    }

    /**
     * 待评价
     */
    @OnClick(R2.id.ll_evaluate)
    void onClickevalute(){
        bundle.putString(ORDER_TYPE,"evaluate");
        startOrderListByType();
    }

    /**
     * 售后
     */
    @OnClick(R2.id.ll_after_market)
    void onClickmarket(){

    }


    //钱包
    @OnClick(R2.id.ll_myBalance)
    void toMyBanlance(){

        getParentDelegate().getSupportDelegate().start(new MyBanlanceDelegate());
    }

    /**
     * 分享
     */
    @OnClick(R2.id.ll_share)
    void share(){

        // req.scene = SendMessageToWX.Req.WXSceneTimeline ;  朋友圈

        final CharSequence[] items = {"好友", "朋友圈"};
        AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
        dlg.setTitle("分享App");
        dlg.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // 这里item是根据选择的方式，相册
                if (item == 0) {
                    req.scene = SendMessageToWX.Req.WXSceneSession;//对话
                } else {
                    try {
                        req.scene = SendMessageToWX.Req.WXSceneTimeline ;  //朋友圈

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

//文字
                WXWebpageObject wxWebpageObject = new WXWebpageObject();
//              wxWebpageObject.webpageUrl ="http://wpa.qq.com/msgrd?v=3&uin=522648467&site=qq&menu=yes";
                wxWebpageObject.webpageUrl ="http://ygou.com/openApp";
//用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
                WXMediaMessage wmsg = new WXMediaMessage(wxWebpageObject);
                wmsg.title =" YGou让闲置不闲置";
                wmsg.description ="快使用这个App吧！我在这个上面选到了实惠的东西";
                Bitmap wbmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_app);
                wmsg.thumbData =bmpToByteArray(wbmp, true);
                req.message = wmsg;
                req.transaction = buildTransaction("webpage");
                YGouApp.iwxapi.sendReq(req);

//构造一个Req
//                SendMessageToWX.Req req = new SendMessageToWX.Req();
//                req.transaction = buildTransaction("webpage");
//                req.message =msg;
//                req.scene =mTargetScene;
//                req.userOpenId = getOpenId()


//                WXTextObject textObj = new WXTextObject();
//                textObj.text = "分享这个App给你";
//                WXMediaMessage tmsg = new WXMediaMessage();
//                tmsg.mediaObject = textObj;
//                tmsg.description = "快使用这个App吧！我在这个上面选到了实惠的东西";
//                SendMessageToWX.Req req = new SendMessageToWX.Req();
//                req.transaction = buildTransaction("text");
//                req.message = tmsg;
//                YGouApp.iwxapi.sendReq(req);
//
////图片
//                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_app);
//                //初始化 WXImageObject 和 WXMediaMessage 对象
//                WXImageObject imgObj = new WXImageObject(bmp);
//                WXMediaMessage msg = new WXMediaMessage();
//
//                msg.title = "YGou让闲置不闲置";                     //标题
//                msg.description = "快使用这个App吧！我在这个上面选到了实惠的东西";               //描述
//                msg.mediaObject = imgObj;
//                //设置缩略图
//                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 120, 120, true);
//                bmp.recycle();
//                msg.thumbData =bmpToByteArray(thumbBmp, true);
//                req.transaction = buildTransaction("img");
//                req.message = msg;
//                //   req.userOpenId = getOpenId();
//                //调用api接口，发送数据到微信
//
//
//                YGouApp.iwxapi.sendReq(req);


            }
        }).create();
        dlg.show();
    }

    /**
     * wxsharez转换
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 微信源码
     * @param type
     * @return
     */
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    //晒单
    @OnClick(R2.id.go_show_order)
    void goShowOrder(){
        getParentDelegate().getSupportDelegate().start(new ShowOrderDelegate());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = new Bundle();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_personal;
    }


    /**
     * 进入我的订单
     */
    private void startOrderListByType(){
        final OrderListDelegate delegate = new OrderListDelegate();
        delegate.setArguments(bundle);
        getParentDelegate().getSupportDelegate().start(delegate);
    }

    @Override
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        super.OnBindView(bundle,rootView);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        sRvSetting.setLayoutManager(manager);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RestClient.builder()
                .setUrl("adv/queryRandom/"+YGouPreferences.getCustomAppProfile("userId"))
                .loader(getContext())
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject jsonObject =JSONObject.parseObject(response);
                        JSONObject user = jsonObject.getJSONObject("jsonObject");
                        if (user!=null){
                            String user_img = user.getString("user_img");
                            Log.i("用户的照片地址，加载照片", response);
                            YGouPreferences.addCustomAppProfile("userImg",user_img);
                            Glide.with(getContext())
                                    .load(user_img)
                                    .into(img_avater);
                        }
                        final ArrayList<MultipleitemEntity> list = new PersonalBottomConverter().setsJsonData(response).convert();
                        sAdapter = new PersonalBottomAdapter(list);
                        sRvSetting.setAdapter(sAdapter);
                    }
                })
                .build()
                .get();

    }

    /**
     *
     * 选择图片
     */
    private void selectPic() {

//动态请求权限，除此之外还需进行Androidmanifest.xml中进行请求
        if (ContextCompat.checkSelfPermission(_mActivity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this._mActivity,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        final CharSequence[] items = {"相册", "拍照"};
        AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
        dlg.setTitle("修改头像");
        dlg.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // 这里item是根据选择的方式，相册
                if (item == 0) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 0);
                } else {
                    try {
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        mPhtotPath = getSDPath() + "/" + getPhotoFileName();
                        Log.i("mPhtotPath", "onClick: "+mPhtotPath);
                        mPhotoFile = new File(mPhtotPath);
                        if (!mPhotoFile.exists()) {
                            mPhotoFile.createNewFile();
                        }
//                        uriImage = FileProvider.getUriForFile(EndLoadMstActivity.this, getPackageName() + ".provider", createImageFile());
                        uriImage = FileProvider.getUriForFile(getContext(), "com.ygou.app.tms.provider", mPhotoFile);
                        Log.i("TAG", "onClick: "+mPhtotPath+"---------" + ".provider");
                        // uriImage = Uri.fromFile(mPhotoFile);以上一句代替解决相机兼容问题
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
                        startActivityForResult(intent, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).create();
        dlg.show();
    }

    /**
     * 获得path
     * @return
     */
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExsit = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExsit) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        return sdDir.toString();
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取对应的请求码
        if (requestCode == 1) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; //图片宽高都为原来的二分之一，即图片为原来的四分之一
            Bitmap bitmap = BitmapFactory.decodeFile(mPhtotPath, options);
            if (bitmap != null) {
                if (uriImage != null) {
                    img_avater.setImageURI(uriImage);
                    saveUritoFile(uriImage,1);
                }
                if (!bitmap.isRecycled()) {
                    bitmap.recycle(); //回收图片所占的内存
                    System.gc(); //提醒系统及时回收
                }
            }
        }
        if (requestCode == 0) {
            if (data != null) {
                Uri uri = data.getData();
                img_avater.setImageURI(uri);
                saveUritoFile(uri,0);
            }
        }

        if (requestCode==2){
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_app);
            //初始化 WXImageObject 和 WXMediaMessage 对象
            WXImageObject imgObj = new WXImageObject(bmp);
            WXMediaMessage msg = new WXMediaMessage();

            msg.title = "YGou让闲置不闲置";                     //标题
            msg.description = "快使用这个App吧！我在这个上面选到了实惠的东西";               //描述
            msg.mediaObject = imgObj;

            //设置缩略图
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 120, 120, true);
            bmp.recycle();
            msg.thumbData =bmpToByteArray(thumbBmp, true);

            req.transaction = buildTransaction("img");
            req.message = msg;
            //   req.userOpenId = getOpenId();
        //调用api接口，发送数据到微信
            YGouApp.iwxapi.sendReq(req);

        }

    }

    private void saveUritoFile(Uri uriImage,int type) {
        Bitmap photoBmp = null;

        if (uriImage != null) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2; //图片宽高都为原来的二分之一，即图片为原来的四分之一
                try {
                    photoBmp = BitmapFactory.decodeStream(getContext().getContentResolver()
                            .openInputStream(uriImage), null, options);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                File file = new File("");
                if (type==0){
                    file = getFileByUri(uriImage);

                }else {
                    if (mPhotoFile!=null){
                        file = mPhotoFile;
                    }
                }
               //得到覅file
                mFile = file;
                Log.i("获得文件", "onClickPay: "+mFile.getName());
                //上传文件
                upload(file);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("TAG", "saveUritoFile: ---------压缩图片异常！");
            }
        }
    }

    /**
     * 上传头像
     * @param file
     */
    private void upload(File file) {
        List<File> fileList = new ArrayList<>();
        fileList.add(file);
        RestClient.builder()
                .setUrl("user/upload/"+ YGouPreferences.getCustomAppProfile("userId"))
                .file(fileList)
                .onSuccess(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("response", "onSuccess: ");
                        Log.i("", "onSuccess: "+response);

                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {

                    }
                })
                .build()
                .upload();
    }

    /**
     * 通过Uri返回File文件
     * 注意：通过相机的是类似content://media/external/images/media/97596
     * 通过相册选择的：file:///storage/sdcard0/DCIM/Camera/IMG_20150423_161955.jpg
     * 通过查询获取实际的地址
     * @param uri
     * @return
     */
    public File getFileByUri(Uri uri) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = getContext().getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContext().getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();
            return new File(path);
        } else {
            Log.i("", "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }

}
