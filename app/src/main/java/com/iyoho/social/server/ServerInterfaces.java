package com.iyoho.social.server;

import android.content.Context;
import android.os.StrictMode;
import android.text.TextUtils;

import com.iyoho.social.utils.LogUtil;
import com.iyoho.social.utils.NetWorkUtil;
import com.iyoho.social.utils.SystemUtil;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.OkHttpUtilInterface;
import com.okhttplib.annotation.CacheType;
import com.okhttplib.callback.CallbackOk;
import com.okhttplib.callback.ProgressCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.path;
import static android.R.attr.x;
import static org.greenrobot.eventbus.EventBus.TAG;

/**
 * Created by ab053167 on 2017/9/12.
 */

public class ServerInterfaces {
    public ServerInterfaces() {
        init();
    }

    public enum ENV {
        DEVELOP, UAT, PRD
    }

    public static ENV ENVIRONMENT = ENV.DEVELOP;
    private String serverInterfaceURL = "";
    public static final String DEVELOP_HOST = "";
    public static final String UAT_HOST = "";
    public static final String PRD_HOST = "";
    private String fileUploadUrl="http://open.1000erp.com/os/file/uploadFile.do";
    private String version;
    private String deviceID;
    private String platformId;
    private String uuid;
    private String deviceType="Android";
    private static volatile ServerInterfaces instance = null;

    public static ServerInterfaces getInstance() {

        if (instance == null) {
            synchronized (ServerInterfaces.class) {
                if (instance == null) {
                    instance = new ServerInterfaces();
                }
            }
        }
        return instance;
    }

    public void init() {
        initENV();
        initParams();
    }

    private void initENV() {
        switch (ENVIRONMENT) {
            case DEVELOP: {
                serverInterfaceURL = DEVELOP_HOST;
            }
            break;
            case UAT: {
                serverInterfaceURL = UAT_HOST;
            }
            break;
            case PRD: {
                serverInterfaceURL = PRD_HOST;
            }
            break;
            default: {
                serverInterfaceURL = PRD_HOST;

            }
        }
    }

    public void initParams() {
        version = SystemUtil.getVersionName(SystemUtil.getInstance().getApplication().getApplicationContext());
        if (TextUtils.isEmpty(version)) {
            version = "1.0.0";
        }
        platformId = SystemUtil.getInstance().getApplication().getPackageName();
        deviceID = SystemUtil.getInstance().getDeviceID();
        uuid = SystemUtil.getInstance().getUuid();
    }

    /**
     * 异步POST请求：回调方法可以直接操作UI
     */
    public void postAsync(Context context, String interfaceUrl, HashMap<String, String> params, final ServerCallback callback) {
        // addPublicParams(params);
        OkHttpUtilInterface build=null;
        if(!NetWorkUtil.isNetworkAvailable(context)){
            build=OkHttpUtil.Builder().setCacheType(CacheType.FORCE_CACHE).build(context);
        }else{
            build=OkHttpUtil.getDefault(context);
        }
        HttpInfo info =null;
        if(getHeads()!=null&&getHeads().size()>0){
            info= HttpInfo.Builder().addHeads(getHeads()).setUrl(interfaceUrl).addParams(params).build();
        }else{
            info= HttpInfo.Builder().setUrl(interfaceUrl).addParams(params).build();
        }
        build.doPostAsync(info, new CallbackOk() {
            @Override
            public void onResponse(HttpInfo info) throws IOException {
                if (info != null && info.isSuccessful() && info.getNetCode() == 200) {
                    callback.onSuccess(info.getRetDetail());
                } else {
                    callback.onFailure(info.getRetCode(), info.getRetDetail());
                }
            }
        });
    }

    /**
     * 同步POST请求：由于不能在UI线程中进行网络请求操作，所以采用子线程方式
     */
    public void postSync(final Context context, final String interfaceUrl,final HashMap<String, String> params, final ServerCallback callback) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
               // addPublicParams(params);
                OkHttpUtilInterface build=null;
                if(!NetWorkUtil.isNetworkAvailable(context)){
                    build=OkHttpUtil.Builder().setCacheType(CacheType.FORCE_CACHE).build(context);
                }else{
                    build=OkHttpUtil.getDefault(context);
                }
                HttpInfo info =null;
               if(getHeads()!=null&&getHeads().size()>0){
                info= HttpInfo.Builder().addHeads(getHeads()).setUrl(interfaceUrl).addParams(params).build();
                }else{
                info= HttpInfo.Builder().setUrl(interfaceUrl).addParams(params).build();
                 }
                build.doPostSync(info);
                if (info != null && info.isSuccessful() && info.getNetCode() == 200) {
                    callback.onSuccess(info.getRetDetail());
                } else {
                    callback.onFailure(info.getRetCode(), info.getRetDetail());
                }
            }
    /**
     * 异步GET请求：回调方法可以直接操作UI
     */
    public void getAsync(final Context context, String interfaceUrl, final ServerCallback callback) {
        // addPublicParams(params);
        OkHttpUtilInterface build=null;
        if(!NetWorkUtil.isNetworkAvailable(context)){
            build=OkHttpUtil.Builder().setCacheType(CacheType.FORCE_CACHE).build(context);
        }else{
            build=OkHttpUtil.getDefault(context);
        }
        HttpInfo info=null;
        if(getHeads()!=null&&getHeads().size()>0){
            info= HttpInfo.Builder().addHeads(getHeads()).setUrl(interfaceUrl).build();
        }else{
            info= HttpInfo.Builder().setUrl(interfaceUrl).build();
        }
        build.doGetAsync(info, new CallbackOk() {
            @Override
            public void onResponse(HttpInfo info) throws IOException {
                if (info != null && info.isSuccessful() && info.getNetCode() == 200) {
                    callback.onSuccess(info.getRetDetail());
                } else {
                    callback.onFailure(info.getRetCode(), info.getRetDetail());
                }
            }
        });
    }

    /**
     * 同步GET请求：由于不能在UI线程中进行网络请求操作，所以采用子线程方式
     */
    public void getSync(final Context context, final String interfaceUrl, final ServerCallback callback) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
                // addPublicParams(params);
                OkHttpUtilInterface build=null;
                if(!NetWorkUtil.isNetworkAvailable(context)){
                    build=OkHttpUtil.Builder().setCacheType(CacheType.FORCE_CACHE).build(context);
                }else{
                    build=OkHttpUtil.getDefault(context);
                }
                HttpInfo info=null;
                if(getHeads()!=null&&getHeads().size()>0){
                    info= HttpInfo.Builder().addHeads(getHeads()).setUrl(interfaceUrl).build();
                }else{
                    info= HttpInfo.Builder().setUrl(interfaceUrl).build();
                }
                build.doGetSync(info);
                if (info != null && info.isSuccessful() && info.getNetCode() == 200) {
                    callback.onSuccess(info.getRetDetail());
                } else {
                    callback.onFailure(info.getRetCode(), info.getRetDetail());
                }
            }

            //------上传文件-----
            public interface UploadFileCallback {
                void uploadProgress(int uploadIndex,String uploadPath,int percent, long bytesWritten, long contentLength, boolean done);
                void uploadCompleted(int uploadIndex,String uploadPath, String result);
                void uploadFailure(int uploadIndex,String uploadPath, int errorCode,String errorMsg);
            }
    /**
     * 异步上传单张图片
     */
    public void uploadFileAsync(final String filePath,final UploadFileCallback callback) {
        HttpInfo info = HttpInfo.Builder()
                .setUrl(fileUploadUrl)
                .addParam("fileType", "avatar")
                .addUploadFile("uploadFile", filePath, new ProgressCallback() {
                    //onProgressMain为UI线程回调，可以直接操作UI
                    @Override
                    public void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done) {
                        callback.uploadProgress(0,filePath,percent,bytesWritten,contentLength,done);
                    }
                    @Override
                    public void onResponseSync(String filePath, HttpInfo info) {
                        super.onResponseSync(filePath,info);
                        if (info != null && info.isSuccessful() && info.getNetCode() == 200) {
                            callback.uploadCompleted(0,filePath,info.getRetDetail());
                        } else {
                            callback.uploadFailure(0,filePath,info.getRetCode(),info.getRetDetail());
                        }

                    }
                })

                .build();
        doUploadFileAsync(info);

    }
    public void uploadFileAsync(final int index,final String filePath,final UploadFileCallback callback) {
        HttpInfo info = HttpInfo.Builder()
                .setUrl(fileUploadUrl)
                .addParam("fileType", "avatar")
                .addUploadFile("uploadFile", filePath, new ProgressCallback() {
                    //onProgressMain为UI线程回调，可以直接操作UI
                    @Override
                    public void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done) {
                        callback.uploadProgress(index,filePath,percent,bytesWritten,contentLength,done);
                    }

                    @Override
                    public void onResponseSync(String filePath, HttpInfo info) {
                        super.onResponseSync(filePath,info);
                        if (info != null && info.isSuccessful() && info.getNetCode() == 200) {
                            callback.uploadCompleted(index,filePath,info.getRetDetail());
                        } else {
                            callback.uploadFailure(index,filePath,info.getNetCode(),info.getRetDetail());
                        }
                    }
                })

                .build();
        doUploadFileAsync(info);

    }
    /**
     * 异步批量上传
     */
    public void uploadBatchFileAsync(List<String> imgList,final UploadFileCallback callback) {
        //循环添加上传文件
        if (imgList != null && imgList.size() > 0) {
            for (int x = 0; x < imgList.size(); x++) {
                uploadFileAsync(x, imgList.get(x), new UploadFileCallback() {
                    @Override
                    public void uploadProgress(int uploadIndex, String uploadPath, int percent, long bytesWritten, long contentLength, boolean done) {
                        callback.uploadProgress(uploadIndex, uploadPath, percent, bytesWritten, contentLength, done);
                    }

                    @Override
                    public void uploadCompleted(int uploadIndex, String uploadPath, String result) {
                        callback.uploadCompleted(uploadIndex, uploadPath, result);
                    }

                    @Override
                    public void uploadFailure(int uploadIndex, String uploadPath, int errorCode, String errorMsg) {
                        callback.uploadFailure(uploadIndex, uploadPath, errorCode, errorMsg);
                    }
                });
            }
        }
    }
        /**
         * 同步批量上传
         */
    public void uploadBatchFileSync(final List<String> imgList, final UploadFileCallback callback){
        //循环添加上传文件
        if(imgList!=null&&imgList.size()>0){
                uploadFileAsync(0,imgList.get(0), new UploadFileCallback() {
                    @Override
                    public void uploadProgress(int uploadIndex, String uploadPath, int percent, long bytesWritten, long contentLength, boolean done) {
                        callback.uploadProgress(uploadIndex,uploadPath,percent,bytesWritten,contentLength,done);
                    }
                    @Override
                    public void uploadCompleted(int uploadIndex, String uploadPath, String result) {
                        callback.uploadCompleted(uploadIndex,uploadPath,result);
                        if(uploadIndex++!=imgList.size()){
                            uploadFileAsync(uploadIndex,imgList.get(uploadIndex),this);
                        }

                    }
                    @Override
                    public void uploadFailure(int uploadIndex, String uploadPath, int errorCode, String errorMsg) {
                        callback.uploadFailure(uploadIndex,uploadPath,errorCode,errorMsg);
                        if(uploadIndex++!=imgList.size()){
                            uploadFileAsync(uploadIndex,imgList.get(uploadIndex),this);
                        }
                    }
                });

        }

    }
    /**
     * 异步上传图片
     */
    private void doUploadFileAsync(final HttpInfo info){
        OkHttpUtil.getDefault(this).doUploadFileAsync(info );
    }

    public void addPublicParams(HashMap<String, String> params){
        params.put("mode", "json");
        params.put("platformId", platformId);
        params.put("deviceAppId", deviceID);
        params.put("deviceType", deviceType);
        params.put("appVersion", version);
        params.put("sn",uuid);
    }
    public HashMap<String, String> getHeads(){
        HashMap<String, String> heads=new HashMap<String,String>();
        //heads.put("Accept-Encoding","gzip");
        return heads;
    }
}