package com.iyoho.social.base;

import android.app.Application;
import android.os.Environment;

import com.iyoho.social.*;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.CacheLevel;
import com.okhttplib.annotation.CacheType;
import com.okhttplib.cookie.PersistentCookieJar;
import com.okhttplib.cookie.cache.SetCookieCache;
import com.okhttplib.cookie.persistence.SharedPrefsCookiePersistor;

/**
 * Created by ab053167 on 2017/9/12.
 */

public class IBaseApplication extends Application {
    private static IBaseApplication application;
    public static IBaseApplication getInstance(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        String downloadFileDir = Environment.getExternalStorageDirectory().getPath()+"/iyohosocial_download/";
        OkHttpUtil.init(this)
                .setConnectTimeout(6000)//连接超时时间
                .setWriteTimeout(6000)//写超时时间
                .setReadTimeout(6000)//读超时时间
                .setMaxCacheSize(10 * 1024 * 1024)//缓存空间大小
                .setCacheLevel(CacheLevel.FIRST_LEVEL)//缓存等级
                .setCacheType(CacheType.FORCE_NETWORK)//缓存类型
                .setShowHttpLog(true)//显示请求日志
                .setShowLifecycleLog(false)//显示Activity销毁日志
                .setRetryOnConnectionFailure(false)//失败后不自动重连
                .setDownloadFileDir(downloadFileDir)//文件下载保存目录
                .addResultInterceptor(HttpInterceptor.ResultInterceptor)//请求结果拦截器
                .addExceptionInterceptor(HttpInterceptor.ExceptionInterceptor)//请求链路异常拦截器
                .setCookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this)))//持久化cookie
                .build();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
