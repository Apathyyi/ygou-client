package sy.bishe.ygou.delegate.find;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import sy.bishe.ygou.R;
import sy.bishe.ygou.R2;
import sy.bishe.ygou.app.appication.YGouApp;
import sy.bishe.ygou.bean.GoodsInfoBean;
import sy.bishe.ygou.bean.ImgFileBean;
import sy.bishe.ygou.delegate.buttons.ButtonItemDelegate;
import sy.bishe.ygou.net.callback.IFailure;
import sy.bishe.ygou.net.callback.ISuccess;
import sy.bishe.ygou.net.rest.RestClient;
import sy.bishe.ygou.utils.storage.YGouPreferences;

public class ReleaseDelegate extends ButtonItemDelegate {

    private GoodsInfoBean goodsInfoBean = new GoodsInfoBean(); //发布的商品bean

    List<ImgFileBean> fileList = new ArrayList<>();  //上传的图片列表

    LoadPicAdapter adapter = null;  //图片适配器

    @BindView(R2.id.release_title)
    AppCompatEditText edt_title = null;

    @BindView(R2.id.release_desc)
    AppCompatEditText edt_desc = null;

    @BindView(R2.id.release_price)
    AppCompatEditText edt_price = null;

    @BindView(R2.id.release_count)
    AppCompatEditText edt_count = null;

    @BindView(R2.id.release_type)
    AppCompatSpinner spi_type = null;

    @BindView(R2.id.release_brand)
    AppCompatSpinner spi_brand = null;

    @BindView(R2.id.release_lable1)
    AppCompatEditText label1 = null;

    @BindView(R2.id.release_lable2)
    AppCompatEditText label2 = null;

    @BindView(R2.id.release_lable3)
    AppCompatEditText label3 = null;

    @BindView(R2.id.release_lable4)
    AppCompatEditText label4 = null;

    ProgressDialog progressDialog;
    private boolean Success = false;


    @BindView(R2.id.release_address)
    AppCompatEditText edit_address = null;

    @BindView(R2.id.release__wait)
    LinearLayout ll_release_wait = null;


    private int num = 0;
    /**
     * 返回
     */
    @OnClick(R2.id.release_back)
    void back(){
       getFragmentManager().popBackStack();
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_relese;
    }
    @Override
    /**
     * 绑定 初始化商品类型
     */
    public void OnBindView(@Nullable Bundle bundle, View rootView) {
        initAdapter();
        spi_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] vertical = getResources().getStringArray(R.array.type);
                String cuurentstr = vertical[position];
//        <item>服装</item>   1   Cloth
//        <item>书籍</item>   2   book
//        <item>工具</item>   3
//        <item>数码</item>   4
//        <item>手机</item>   5
//        <item>美妆</item>   6
//        <item>电脑</item>   7
//        <item>宠物</item>   8
//        <item>乐器</item>   9
//        <item>其它</item>   10
                Log.i("vertical", "onItemSelected: "+cuurentstr+"position"+position);
                List<String> list = new ArrayList<String>();
                ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item);
               switch (position){
                   case 1:
                       list = Arrays.asList(getResources().getStringArray(R.array.Clothing));
                       num = 36;
                       break;
                   case 2:
                       list = Arrays.asList(getResources().getStringArray(R.array.Book));
                       num = 10;
                       break;
                   case 3:
                       list = Arrays.asList(getResources().getStringArray(R.array.Tools));
                       num = 51;
                       break;
                   case 4:
                       list = Arrays.asList(getResources().getStringArray(R.array.Digital));
                       num = 68;
                       break;
                   case 5:
                       list = Arrays.asList(getResources().getStringArray(R.array.Phone));
                       num = 79;
                       break;
                   case 6:
                       list = Arrays.asList(getResources().getStringArray(R.array.Beauty));
                       num = 87;
                       break;
                   case 7:
                       list = Arrays.asList(getResources().getStringArray(R.array.Computer));
                       num = 101;
                       break;
                   case 8:
                       list = Arrays.asList(getResources().getStringArray(R.array.pet));
                       num = 110;
                       break;
                   case 9:
                       list = Arrays.asList(getResources().getStringArray(R.array.Instrument));
                       num = 123;
                       break;
                   case 10:
                       list = Arrays.asList(getResources().getStringArray(R.array.other));
                       num = 124;
                       break;
                       default:
                           break;
               }
               adapter.addAll(list);
               spi_brand.setAdapter(adapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 信息核实
     * @return
     */
    private boolean checkinfomation(){
        boolean isOk = true;
        if (spi_brand.getSelectedItem()==null||spi_type.getSelectedItem()==null){
            isOk = false;
            Toast.makeText(getContext(),"请选择类型",Toast.LENGTH_SHORT).show();
        }
        else {

            String title = edt_title.getText().toString();
            String desc = edt_desc.getText().toString();
            String type = spi_type.getSelectedItem().toString();
            String price = edt_price.getText().toString();
            String count = edt_count.getText().toString();
            String brand = spi_brand.getSelectedItem().toString();
            String address = edit_address.getText().toString();
            if (title.isEmpty() || title.length() > 10) {
                isOk = false;
                edt_title.setError("标题不能为空并且小于10个字符");
            } else {
                edt_title.setError(null);
                goodsInfoBean.setGoodsinfo_name(title);
            }
            if (desc.isEmpty() || desc.length() > 30) {
                isOk = false;
                edt_desc.setError("描述不能为空并且小于30个字符");
            } else {
                edt_desc.setError(null);
                goodsInfoBean.setGoodsinfo_desc(desc);
            }
            if (type.equals("请选择")) {
                isOk = false;
                Toast.makeText(getContext(), "选择一个类型", Toast.LENGTH_SHORT).show();
            }else {
                if (!brand.isEmpty()){
                  goodsInfoBean.setGoodsinfo_type(num);
                }

            }
            if (edt_price.getText().toString().isEmpty() || Double.parseDouble(price) == 0) {
                isOk = false;
                edt_price.setError("价格不能为空并且不小于0");
            } else {
                edt_price.setError(null);
                goodsInfoBean.setGoodsinfo_price(Double.parseDouble(price));
            }
            if (edt_count.getText().toString().isEmpty() || count.equals("0")) {
                isOk = false;
                edt_count.setError("数量不能为空并且不小于1");
            } else {
                edt_count.setError(null);
                goodsInfoBean.setGoodsinfo_count(count);
            }
            if (label1.getText().toString().equals("") || label1.getText().toString().length() > 5) {
                label1.setError("标签不为空，字数少于五");
                isOk = false;
            } else {
                label1.setError(null);
            }
            if (label2.getText().toString().equals("") || label2.getText().toString().length() > 5) {
                label2.setError("标签不为空，字数少于五");
                isOk = false;
            } else {
                label2.setError(null);
            }
            if (label3.getText().toString().equals("") || label3.getText().toString().length() > 5) {
                label3.setError("标签不为空，字数少于五");
                isOk = false;
            } else {
                label3.setError(null);
            }
            if (address.isEmpty()||!address.contains("-")){
                edit_address.setError("地址格式不正确");
                isOk=false;
            }else {
                edit_address.setError(null);
                goodsInfoBean.setGoodsinfo_area(address);
            }
//            if (label4.getText().toString().equals("") || label4.getText().toString().length() > 5) {
//                label4.setError("标签不为空，字数少于五");
//                isOk = false;
//            } else {
//                label4.setError(null);
//            }
        }
        goodsInfoBean.setGoodsinfo_hot("0");
        return isOk;
    }



    @BindView(R.id.rvPic)
    RecyclerView rvPic = null;

    @BindView(R.id.tvNum)
    AppCompatTextView tvNum = null;

    /**
     * 初始化图片的适配
     */
    private void initAdapter() {

        fileList.add(new ImgFileBean());
        adapter = new LoadPicAdapter(getContext(), fileList,1);
        rvPic.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        rvPic.setLayoutManager(gridLayoutManager);
        adapter.setListener(new LoadPicAdapter.OnItemClickListener() {
            @Override
            public void click(View view, int positon) {
                if (fileList.size()>8){
                    Log.i("click", "click:1张照片");
                }else {
                    selectPic();  //选择添加图片方法
                }
            }

            @Override
            public void del(View view) {
                tvNum.setText((fileList.size()-1)+"/8");
            }
        });

    }

    /**
     * 图片参数
     */
    String mPhtotPath;
    Uri uriImage;
    File mPhotoFile = null;

    /**
     *   选择图片
     *   核对和请求权限
     *   选择方式
     *   传递意图 获取数据
     */
    private void selectPic() {
//动态请求权限，除此之外还需进行Androidmanifest.xml中进行请求 6.0
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(),
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        final CharSequence[] items = {"相册", "拍照"};
        AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
        dlg.setTitle("添加图片");
        dlg.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // 这里item是根据选择的方式，
                if (item == 0) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 0);
                } else {
                    try {
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        mPhtotPath = getSDPath() + "/" + getPhotoFileName();
                        Log.i("mPhtotPath", "onClick: "+mPhtotPath);
                        //创建一个file
                        mPhotoFile = new File(mPhtotPath);
                        if (!mPhotoFile.exists()) {
                            mPhotoFile.createNewFile();
                        }
//                        uriImage = FileProvider.getUriForFile(EndLoadMstActivity.this, getPackageName() + ".provider", createImageFile());
                        //获得uri地址
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
     * 获得存储路径
     * @return
     */
    public String getSDPath() {
        File sdDir = null;
        //只有在SD卡状态为MEDIA_MOUNTED时/mnt/sdcard目录才是可读可写，并且可以创建目录及文件。
        boolean sdCardExsit = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExsit) {
            //内部存储就是/data下的，外部存储就是/sdcard下的
            sdDir = Environment.getExternalStorageDirectory();
        }
        return sdDir.toString();
    }

    /**
     * 获得一个唯一文件名 以当前系统时间
     * @return
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    /**
     *  重写onActivityResult方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; //图片宽高都为原来的二分之一，即图片为原来的四分之一
            //Bitmap的存储可以说包括两个部分，像素以及长,宽，颜色等描述信息
            Bitmap bitmap = BitmapFactory.decodeFile(mPhtotPath, options);
            if (bitmap != null) {
                if (uriImage != null) {
                    //相机
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
                //相册
                Uri uri = data.getData();
                saveUritoFile(uri,0);
            }
        }

    }

    /**
     * 将Uri图片类型转换成File，BitMap类型
     * 在界面上显示BitMap图片，以防止内存溢出
     * 上传可选择File文件上传
     * @param uriImage
     * @param type
     */

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
                    //相册传递过来的
                    file = getFileByUri(uriImage);

                }else {
                    //相机
                    if (mPhotoFile!=null){
                        file = mPhotoFile;
                    }

                }
                fileList.add(new ImgFileBean(file, false, 0, photoBmp));
                tvNum.setText((fileList.size()-1)+"/8");
                if (fileList.size()>8){    //判断时候达到最大数量，如果达到最大数量，则去掉前面的加号
                    fileList.remove(0);
                }
                Log.i("fileList", "saveUritoFile: "+fileList);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("TAG", "saveUritoFile: ---------压缩图片异常！");
            }
        }
    }
    /**
     * 通过Uri返回File文件
     * 通过相机的是类似content://media/external/images/media/97596
     * 通过相册选择的：file:///storage/sdcard0/DCIM/Camera/IMG_20150423_161955.jpg
     * 通过查询获取实际的地址
     * @param uri
     * @return
     * 通过 mContext.getContentResolver();获取ContentResolver 实例
     *
     * query(...) 搜索指定Uri下的媒体文件，
     */

    public File getFileByUri(Uri uri) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                //ContentResolver负责获取ContentProvider提供的数据。
                ContentResolver cr = getContext().getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                //通过Uri进行查询，返回一个Cursor。
                ///String[]中包含了所有要返回的字段  图片的字段信息
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

    /**
     * 点击提交
     * 验证填写信息
     * 先保存照片
     * 将信息保存到商品bean
     * 插入数据库
     *
     */
    @OnClick(R2.id.release_submit)
    void onClickcheck(){
        //获得品牌名
        //显示正在上传
        if (spi_brand.getSelectedItem()==null){
            Toast.makeText(getContext(),"请选择类型",Toast.LENGTH_SHORT).show();
            return;
        }
        ll_release_wait.setVisibility(View.VISIBLE);
        String barnd = spi_brand.getSelectedItem().toString();
        List<File> files = new ArrayList<>();
        int size  = fileList.size();
        for (int i = 1; i < size; i++) {
            Log.i("i", "onClickcheck: "+size);
            ImgFileBean imgFileBean = fileList.get(i);
            Log.i("NAME", "onClickcheck: "+ fileList.get(i).getFile());
            files.add(fileList.get(i).getFile());
        }
        if (files.size()==0){
            Toast.makeText(getContext(),"请上传8张图片",Toast.LENGTH_SHORT).show();
        }else {
            if (checkinfomation()){
                Toast.makeText(getContext(),"信息核实",Toast.LENGTH_SHORT).show();
                //1 保存数据
                String label ="";
                goodsInfoBean.setGoodsinfo_name(edt_title.getText().toString());
                if (!label1.getText().toString().isEmpty()){
                    label = label1.getText().toString();
                    if (!label2.getText().toString().isEmpty()){
                        label=label+"-"+label2.getText().toString();
                        if (!label3.getText().toString().isEmpty()){
                            label=label+"-"+label3.getText().toString();
                            if (!label4.getText().toString().isEmpty()){
                                label=label+"-"+label4.getText().toString();
                            }
                        }
                    }
                }
                goodsInfoBean.setGoodsinfo_thumb("http://192.168.43.191:8080/upload/"+files.get(0).getName());
                goodsInfoBean.setGoodsinfo_lable(label);
                goodsInfoBean.setGoodsinfo_userName(YGouPreferences.getCustomAppProfile("userName"));
                ll_release_wait.setVisibility(View.VISIBLE);
                RestClient.builder()
                        .setUrl("goodsinfo/upload")
                        // .file(fileList.get(1).getFile())
                        .file(files)
                        .onSuccess(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                Log.i("上传成功后返回地址", "onSuccess: "+response);
                                JSONArray data = JSONObject.parseObject(response).getJSONArray("data");

                                //默认取第一个为照片
                                String thunmb = data.getString(0);
                                String host = "http://192.168.43.191:8080/upload/";
                                String totalname = host + thunmb;
                                goodsInfoBean.setGoodsinfo_thumb(totalname);
                                String banners ="";
                                if (!data.isEmpty()){
                                    int size = data.size();
                                    for (int i = 0; i <size ; i++) {
                                        if (i!=size-1){
                                            banners += host+data.getString(i)+";";
                                        }else {
                                            banners+=host+data.getString(i);
                                        }
                                    }
                                    Log.i("banners的值", "onSuccess: "+banners);
                                    goodsInfoBean.setGoodsinfo_banners(banners);
                                }

                                //等待上传成功
                                try {
                                    Thread.sleep(10000);
                                    //查询品牌名
                                    RestClient.builder()
                                            .setUrl("sortcontent/queryByName/"+barnd)
                                            .onSuccess(new ISuccess() {
                                                @Override
                                                public void onSuccess(String response) {
                                                    JSONObject jsonObject1 = (JSONObject) JSONObject.toJSON(goodsInfoBean);
                                                    RequestBody requestBody = RequestBody.create(YGouApp.JSON,jsonObject1.toJSONString());
                                                    RestClient.builder()
                                                            .setUrl("goodsinfo/add")
                                                            .setBody(requestBody)
                                                            .onSuccess(new ISuccess() {
                                                                @Override
                                                                public void onSuccess(String response) {
                                                                    Toast.makeText(getContext(),"成功",Toast.LENGTH_SHORT).show();
                                                                    ll_release_wait.setVisibility(View.GONE);
                                                                    getFragmentManager().popBackStack();
                                                                }
                                                            })
                                                            .failure(new IFailure() {
                                                                @Override
                                                                public void onFailure() {
                                                                    Toast.makeText(getContext(),"失败",Toast.LENGTH_SHORT).show();
                                                                }
                                                            })
                                                            .build()
                                                            .post();
                                                    JSONObject jsonObject = JSON.parseObject(response);
                                                    if (jsonObject.getString("status").equals("ok")){
                                                        Integer type = jsonObject.getInteger("data");
                                                        Log.i("type", "onSuccess: "+type);
                                                        goodsInfoBean.setGoodsinfo_type(type);
                                                    }
                                                }
                                            })
                                            .failure(new IFailure() {
                                                @Override
                                                public void onFailure() {
                                                    Toast.makeText(getContext(),"失败",Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .build()
                                            .get();

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                JSONObject jsonObject = JSONObject.parseObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")){
                                    Toast.makeText(getContext(),"上传成功",Toast.LENGTH_SHORT).show();
                                    ll_release_wait.setVisibility(View.GONE);
                                    List<String> list = jsonObject.getJSONArray("data").toJavaList(String.class);
                                    int size = list.size();
                                    for (int i = 0; i < size; i++) {
                                        String url = list.get(i);
                                        Log.i("返回图片地址", "onSuccess: "+url);
                                    }
                                }
                            }

                        })
                        .failure(new IFailure() {
                            @Override
                            public void onFailure() {
                                Log.i("", "onFailure: ");
                                Toast.makeText(getContext(),"上传失败请重试",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        })
                        .build()
                        .upload();

            }else {
                Toast.makeText(getContext(),"请填写信息",Toast.LENGTH_SHORT).show();
                return ;
            }
        }

    }

    /**
     * 显示弹窗
     * @param title
     * @param message
     */
    public void showProgressDialog(String title, String message) {
        if (progressDialog == null) {

            progressDialog = ProgressDialog.show(getContext(), title,
                    message, true, false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
        }

        progressDialog.show();

    }

    /**
     * 隐藏
     */
    public void hideProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }
}
