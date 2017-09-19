package com.iyoho.social;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.iyoho.social.Entry.MessageEvent;
import com.iyoho.social.utils.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import social.iyoho.com.imgselectlibrary.view.ImageSelectorActivity;

public class EventBus2Activity extends IBaseActivity {
    private TextView textView;

    @Override
    public Bundle getBundle()throws Exception {
        return IBaseActivity.getBundle(EventBus2Activity.class);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        textView= (TextView) findViewById(R.id.btn);
    }

    @Override
    public void initEvent() {
        EventBusUtils.register(this);
        textView.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Bundle bundle= null;
        try {
            bundle = getBundle();
            System.out.println("----2"+bundle+"---"+bundle.getString("tag1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn:
                EventBusUtils.post(new MessageEvent(MainActivity.class,"main2"));
                EventBusUtils.post(new MessageEvent(EventBus1Activity.class,"E1"));
                setResult(1,null);
                finish();
                break;
        }
    }
    @Subscribe
    public void onEventMainThread(MessageEvent event){
        if(event!=null&&event.getmClass()==EventBus2Activity.class){
            System.out.println("--------EventBus2Activity"+event.getMessage1());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

}
