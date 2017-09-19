package com.iyoho.social;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iyoho.social.Entry.TestEntry;
import com.iyoho.social.server.ServerCallback;
import com.iyoho.social.server.ServerInterfaces;
import com.iyoho.social.utils.FastJsonUtils;
import com.iyoho.social.utils.PermissionDispatcherHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import social.iyoho.com.imgselectlibrary.view.ImageSelectorActivity;
public class SelectAlbumActivity extends IBaseActivity {
    private Button postAsync;
    private Button getAsync;
    private Button postSync;
    private Button getSync;
    private Button singleFileUpload;
    private Button batchFileAsyncUpload;
    private Button batchFileSyncUpload;
    private Button singleFileDownLoad;
    private Button intoGlide;
    private TextView content;
    private TAG tag=TAG.postAsync;
    private enum TAG{
        postAsync,getAsync,postSync,getSync,singleFileUpload,batchFileAsyncUpload,batchFileSyncUpload,singleFileDownLoad
    }
    @Override
    public Bundle getBundle()throws Exception {
        return IBaseActivity.getBundle(SelectAlbumActivity.class);
    }
    @Override
    public int initLayout() {
        return R.layout.test_select;
    }
    @Override
    public void initView() {
        postAsync= (Button) findViewById(R.id.postAsync);
        getAsync= (Button) findViewById(R.id.getAsync);
        postSync= (Button) findViewById(R.id.postSync);
        getSync= (Button) findViewById(R.id.getSync);
        singleFileUpload= (Button) findViewById(R.id.singleFileUpload);
        batchFileAsyncUpload= (Button) findViewById(R.id.batchFileAsyncUpload);
        batchFileSyncUpload= (Button) findViewById(R.id.batchFileSyncUpload);
        singleFileDownLoad= (Button) findViewById(R.id.singleFileDownLoad);
        intoGlide= (Button) findViewById(R.id.intoGlide);
        content= (TextView) findViewById(R.id.content);
    }

    @Override
    public void initEvent() {
        postAsync.setOnClickListener(this);
        getAsync.setOnClickListener(this);
        postSync.setOnClickListener(this);
        getSync.setOnClickListener(this);
        singleFileUpload.setOnClickListener(this);
        batchFileAsyncUpload.setOnClickListener(this);
        batchFileSyncUpload.setOnClickListener(this);
        singleFileDownLoad.setOnClickListener(this);
        intoGlide.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         PermissionDispatcherHelper.onRequestPermissionsResult(this, requestCode, grantResults);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE) {
            final ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            if(tag==TAG.singleFileUpload){
                  singleFileUpload(images.get(0));
            }else if(tag==TAG.batchFileAsyncUpload){
                  batchFileAsyncUpload(images);
            }else if(tag==TAG.batchFileSyncUpload){
                  setBatchFileSyncUpload(images);
            }
        }else if(requestCode==0&&resultCode==1){
            Toast.makeText(SelectAlbumActivity.this,"select",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.postAsync:
            tag=TAG.postAsync;
            postAsync();
            break;
        case R.id.getAsync:
            tag=TAG.getAsync;
            getAsync();
            break;
        case R.id.postSync:
            tag=TAG.postSync;
            postSync();
            break;
        case R.id.getSync:
            tag=TAG.getSync;
            getSync();
            break;
        case R.id.singleFileUpload:
            tag=TAG.singleFileUpload;
            ImageSelectorActivity.start(SelectAlbumActivity.this,1,ImageSelectorActivity.MODE_SINGLE,true,true,false);
            break;
        case R.id.batchFileAsyncUpload:
            tag=TAG.batchFileAsyncUpload;
            ImageSelectorActivity.start(SelectAlbumActivity.this,5,ImageSelectorActivity.MODE_MULTIPLE,true,true,false);
            break;
        case R.id.batchFileSyncUpload:
            tag=TAG.batchFileSyncUpload;
            ImageSelectorActivity.start(SelectAlbumActivity.this,5,ImageSelectorActivity.MODE_MULTIPLE,true,true,false);
            break;
        case R.id.singleFileDownLoad:
            tag=TAG.singleFileDownLoad;
            break;
        case R.id.intoGlide:
            IBaseActivity.startForResult(SelectAlbumActivity.this, EventBus1Activity.class, null,0);
            break;
    }
    }

    public void postAsync(){
        final String url = "http://api.k780.com:88";
        HashMap<String,String> params=new HashMap<String,String>();
        params.put("app","life.time");
        params.put("appkey","10003");
        params.put("sign","b59bc3ef6191eb9f747dd4e83c99f2a4");
        params.put("format","json");
        ServerInterfaces.getInstance().postAsync(this, url,params,new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    TestEntry te = FastJsonUtils.getSingleBean(result, TestEntry.class);
                    System.out.println("######获取完成#######\n"+result);
                    content.setText("######获取完成#######\n"+result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int errorCode, String strMsg) {
                System.out.println("######获取失败#######\n"+strMsg);
                content.setText("######获取失败#######\n"+strMsg);
            }
        });
    }
    public void getAsync(){
        final String url = "http://api.k780.com:88?app=life.time&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
        ServerInterfaces.getInstance().getAsync(this, url,new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    TestEntry te = FastJsonUtils.getSingleBean(result, TestEntry.class);
                    System.out.println("######获取完成#######\n"+result);
                    content.setText("######获取完成#######\n"+result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int errorCode, String strMsg) {
                System.out.println("######获取失败#######\n"+strMsg);
                content.setText("######获取失败#######\n"+strMsg);
            }
        });
    }
    public void postSync(){
        final String url = "http://api.k780.com:88";
        HashMap<String,String> params=new HashMap<String,String>();
        params.put("app","life.time");
        params.put("appkey","10003");
        params.put("sign","b59bc3ef6191eb9f747dd4e83c99f2a4");
        params.put("format","json");
        ServerInterfaces.getInstance().postSync(this, url,params,new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    TestEntry te = FastJsonUtils.getSingleBean(result, TestEntry.class);
                    System.out.println("######获取完成#######\n"+result);
                    content.setText("######获取完成#######\n"+result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int errorCode, String strMsg) {
                System.out.println("######获取失败#######\n"+strMsg);
                content.setText("######获取失败#######\n"+strMsg);
            }
        });
    }
    public void getSync(){
        final String url = "http://api.k780.com:88?app=life.time&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
        ServerInterfaces.getInstance().getSync(this, url,new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    TestEntry te = FastJsonUtils.getSingleBean(result, TestEntry.class);
                    System.out.println("######获取完成#######\n"+result);
                    content.setText("######获取完成#######\n"+result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int errorCode, String strMsg) {
                System.out.println("######获取失败#######\n"+strMsg);
                content.setText("######获取失败#######\n"+strMsg);
            }
        });
    }
    public void singleFileUpload(String filePath){
        ServerInterfaces.getInstance().uploadFileAsync(filePath, new ServerInterfaces.UploadFileCallback() {
            @Override
            public void uploadProgress(int uploadIndex, String uploadPath, int percent, long bytesWritten, long contentLength, boolean done) {
                System.out.println("######"+uploadIndex+"#上传进度#"+percent+"（"+uploadPath+")");
                content.setText("######"+uploadIndex+"#上传进度#"+percent+"（"+uploadPath+")");
            }

            @Override
            public void uploadCompleted(final int uploadIndex,final String uploadPath, String result) {
                final String result1=result;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("######"+uploadIndex+"#上传完成#"+"（"+uploadPath+")");
                        content.setText("######"+uploadIndex+"#上传完成#"+"（"+uploadPath+")");
                    }
                });
            }

            @Override
            public void uploadFailure(int uploadIndex, String uploadPath, int errorCode, String errorMsg) {
                System.out.println("######"+uploadIndex+"#上传失败#"+"（"+uploadPath+")"+errorMsg);
                content.setText("######"+uploadIndex+"#上传失败#"+"（"+uploadPath+")"+errorMsg);
            }
        });
    }
    public void setBatchFileSyncUpload(List<String> images){
        if(images!=null&&images.size()>0){
            ServerInterfaces.getInstance().uploadBatchFileSync(images, new ServerInterfaces.UploadFileCallback() {
                @Override
                public void uploadProgress(int uploadIndex, String uploadPath, int percent, long bytesWritten, long contentLength, boolean done) {
                    System.out.println("######"+uploadIndex+"#上传进度#"+percent+"（"+uploadPath+")");
                    content.setText("######"+uploadIndex+"#上传进度#"+percent+"（"+uploadPath+")");
                }

                @Override
                public void uploadCompleted(final int uploadIndex,final String uploadPath, String result) {
                    final String result1=result;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("######"+uploadIndex+"#上传完成#"+"（"+uploadPath+")");
                            content.setText("######"+uploadIndex+"#上传完成#"+"（"+uploadPath+")");
                        }
                    });
                }

                @Override
                public void uploadFailure(int uploadIndex, String uploadPath, int errorCode, String errorMsg) {
                    System.out.println("######"+uploadIndex+"#上传失败#"+"（"+uploadPath+")"+errorMsg);
                    content.setText("######"+uploadIndex+"#上传失败#"+"（"+uploadPath+")"+errorMsg);
                }


            });


        }
    }
    public void batchFileAsyncUpload(List<String> images){
        if(images!=null&&images.size()>0){
            ServerInterfaces.getInstance().uploadBatchFileAsync(images, new ServerInterfaces.UploadFileCallback() {
                @Override
                public void uploadProgress(int uploadIndex, String uploadPath, int percent, long bytesWritten, long contentLength, boolean done) {
                    System.out.println("######"+uploadIndex+"#上传进度#"+percent+"（"+uploadPath+")");
                    content.setText("######"+uploadIndex+"#上传进度#"+percent+"（"+uploadPath+")");
                }

                @Override
                public void uploadCompleted(final int uploadIndex, final  String uploadPath, String result) {
                    final String result1=result;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("######"+uploadIndex+"#上传完成#"+"（"+uploadPath+")");
                            content.setText("######"+uploadIndex+"#上传完成#"+"（"+uploadPath+")");
                        }
                    });
                }

                @Override
                public void uploadFailure(int uploadIndex, String uploadPath, int errorCode, String errorMsg) {
                    System.out.println("######"+uploadIndex+"#上传失败#"+"（"+uploadPath+")"+errorMsg);
                    content.setText("######"+uploadIndex+"#上传失败#"+"（"+uploadPath+")"+errorMsg);
                }


            });


        }
    }

}
