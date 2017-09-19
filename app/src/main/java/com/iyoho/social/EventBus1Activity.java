package com.iyoho.social;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.iyoho.social.Entry.MessageEvent;
import com.iyoho.social.utils.EventBusUtils;
import com.yoho.glide.GlideImageLoader;
import com.yoho.glide.GlideImageView;
import com.yoho.glide.progress.CircleProgressView;
import com.yoho.glide.progress.OnGlideImageViewListener;
import com.yoho.glide.progress.OnGlideImageViewProgressListener;

import org.greenrobot.eventbus.Subscribe;

public class EventBus1Activity extends IBaseActivity {

    private GlideImageView glideImageView1;
    private GlideImageView glideImageView2;
    private GlideImageView glideImageView3;
    private GlideImageView glideImageView4;
    private GlideImageView glideImageView5;
    private GlideImageView glideImageView6;
    private GlideImageView glideImageView7;
    private GlideImageView glideImageView8;
    private GlideImageView glideImageView9;
    private GlideImageView glideImageView10;

    private TextView textContent;
    private CircleProgressView circleProgressView;
    private TextView textContent1;
    private CircleProgressView circleProgressView1;
    private TextView textContent2;
    private CircleProgressView circleProgressView2;
    @Override
    public Bundle getBundle() throws Exception{
        return IBaseActivity.getBundle(EventBus1Activity.class);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_test_glide;
    }

    @Override
    public void initView() {
        EventBusUtils.register(this);
        glideImageView1= (GlideImageView) findViewById(R.id.circleImgView1);
        glideImageView2= (GlideImageView) findViewById(R.id.circleImgView2);
        glideImageView3= (GlideImageView) findViewById(R.id.circleImgView3);
        glideImageView4= (GlideImageView) findViewById(R.id.circleImgView4);
        glideImageView5= (GlideImageView) findViewById(R.id.circleImgView5);
        glideImageView6= (GlideImageView) findViewById(R.id.circleImgView6);
        glideImageView7= (GlideImageView) findViewById(R.id.circleImgView7);
        glideImageView8= (GlideImageView) findViewById(R.id.circleImgView8);
        glideImageView9= (GlideImageView) findViewById(R.id.circleImgView9);
        glideImageView10= (GlideImageView) findViewById(R.id.circleImgView10);

        textContent= (TextView) findViewById(R.id.textContent);
        circleProgressView= (CircleProgressView) findViewById(R.id.circleProgressView);
        textContent1= (TextView) findViewById(R.id.textContent1);
        circleProgressView1= (CircleProgressView) findViewById(R.id.circleProgressView1);
        textContent2= (TextView) findViewById(R.id.textContent2);
        circleProgressView2= (CircleProgressView) findViewById(R.id.circleProgressView2);
    }

    @Override
    public void initEvent() {
        glideImageView1.setOnClickListener(this);
        glideImageView2.setOnClickListener(this);
        glideImageView3.setOnClickListener(this);
        glideImageView4.setOnClickListener(this);
        glideImageView5.setOnClickListener(this);
        glideImageView6.setOnClickListener(this);
        glideImageView7.setOnClickListener(this);
        glideImageView8.setOnClickListener(this);
        glideImageView9.setOnClickListener(this);
        glideImageView10.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Bundle bundle= null;
        try {
            bundle = getBundle();
            System.out.println("----1234"+bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url1 = "http://img.ivsky.com/img/tupian/slides/201708/14/bailu.jpg";
        String url2 = "http://img8.zol.com.cn/bbs/upload/16955/16954476.png";
        String cat = "http://image52.360doc.com/DownloadImg/2012/06/0316/24581213_2.jpg";
        String cat_thumbnail = "http://image52.360doc.com/DownloadImg/2012/06/0316/24581213_4.jpg";
        String girl = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/screenshot/girl.jpg";
        String girl_thumbnail = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/screenshot/girl_thumbnail.jpg";
        String gif1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496754078616&di=cc68338a66a36de619fa11d0c1b2e6f3&imgtype=0&src=http%3A%2F%2Fapp.576tv.com%2FUploads%2Foltz%2F201609%2F25%2F1474813626468299.gif";
        String gif2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497276275707&di=57c8c7917e91afc1bc86b1b57e743425&imgtype=0&src=http%3A%2F%2Fimg.haatoo.com%2Fpics%2F2016%2F05%2F14%2F9%2F4faf3f52b8e8315af7a469731dc7dce5.jpg";
        String gif3 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497276379533&di=71435f66d66221eb36dab266deb9d6d2&imgtype=0&src=http%3A%2F%2Fatt.bbs.duowan.com%2Fforum%2F201608%2F02%2F190418bmy9zqm94qxlmqf4.gif";
        GlideImageLoader.create(glideImageView1).loadImage(url1,R.drawable.icon_picture);
        GlideImageLoader.create(glideImageView3).loadImage(cat,R.drawable.icon_picture);
        GlideImageLoader.create(glideImageView4).loadImage(cat_thumbnail,R.drawable.icon_picture);
        GlideImageLoader.create(glideImageView5).loadImage(girl,R.drawable.icon_picture);
        GlideImageLoader.create(glideImageView6).loadImage(girl_thumbnail,R.drawable.icon_picture);
        GlideImageLoader.create(glideImageView7).loadImage(gif1,R.drawable.icon_picture);
        GlideImageLoader.create(glideImageView8).loadLocalImage(R.mipmap.jixiangwu,R.drawable.icon_picture);
        GlideImageLoader.create(glideImageView9).loadImage(gif3,R.drawable.icon_picture);
        GlideImageLoader.create(glideImageView10).loadImage(gif2, R.drawable.icon_picture, new OnGlideImageViewProgressListener() {
            @Override
            public void onProgress(String imageUrl, long bytesRead, long totalBytes, int percent, boolean isDone, GlideException exception) {
                textContent.setText("#"+percent+"#("+bytesRead+"/"+totalBytes+")"+isDone+"#");
                circleProgressView.setProgress(percent);
                circleProgressView.setVisibility(isDone?View.GONE:View.VISIBLE);
            }
        });
        GlideImageLoader.create(glideImageView1).loadImage(girl_thumbnail, R.drawable.icon_picture, new OnGlideImageViewProgressListener() {
            @Override
            public void onProgress(String imageUrl, long bytesRead, long totalBytes, int percent, boolean isDone, GlideException exception) {
                textContent1.setText("#"+percent+"#("+bytesRead+"/"+totalBytes+")"+isDone+"#");
                circleProgressView1.setProgress(percent);
                circleProgressView1.setVisibility(isDone?View.GONE:View.VISIBLE);
            }
        });
        GlideImageLoader.create(glideImageView2).loadImage(url2, R.drawable.icon_picture, new OnGlideImageViewProgressListener() {
            @Override
            public void onProgress(String imageUrl, long bytesRead, long totalBytes, int percent, boolean isDone, GlideException exception) {
                textContent2.setText("#"+percent+"#("+bytesRead+"/"+totalBytes+")"+isDone+"#");
                circleProgressView2.setProgress(percent);
                circleProgressView2.setVisibility(isDone?View.GONE:View.VISIBLE);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.circleImgView1:
                setResult(1,null);
                finish();
                break;
            case R.id.circleImgView2:
                IBaseActivity.startForResult(EventBus1Activity.this, EventBus2Activity.class, null, 0);
                break;
            case R.id.circleImgView3:
                IBaseActivity.start(EventBus1Activity.this,EventBus2Activity.class,null);
                break;
            case R.id.circleImgView4:
                break;
            case R.id.circleImgView5:
                break;
            case R.id.circleImgView6:
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0&&resultCode==1){
            Toast.makeText(EventBus1Activity.this,"vb1",Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe
    public void onEventMainThread(MessageEvent event){
        if(event!=null&&event.getmClass()==EventBus1Activity.class){
            System.out.println("--------EventBus1Activity"+event.getMessage1());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }
}
