package com.iyoho.social;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.iyoho.social.Entry.MessageEvent;
import com.iyoho.social.utils.EventBusUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import permissions.dispatcher.PermissionUtils;
import social.iyoho.com.imgselectlibrary.view.ImageSelectorActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBusUtils.register(this);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus1Activity.start(MainActivity.this,SelectAlbumActivity.class,null);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle=new Bundle();
                        bundle.putString("tag1","tagValue1");
                       // EventBus1Activity.start(MainActivity.this,EventBus2Activity.class,bundle);
                    }
                },10000);

            }
        });
    }

    @Subscribe
    public void onEventMainThread(MessageEvent event){
     if(event!=null&&event.getmClass()==MainActivity.class){
         System.out.println("--------MainActivity"+event.getMessage1());
     }
     };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }


}
/*class SelectAlbumPermissionsDispatcher {
    private static final int REQUEST_STARTALBUM = 0;

    private static final String[] PERMISSION_STARTALBUM = new String[] {"android.permission.READ_EXTERNAL_STORAGE"};

    private SelectAlbumPermissionsDispatcher() {
    }

    static void startAlbumWithCheck(SelectAlbumActivity target) {
        if (PermissionUtils.hasSelfPermissions(target, PERMISSION_STARTALBUM)) {
            target.startAlbum();
        } else {
            ActivityCompat.requestPermissions(target, PERMISSION_STARTALBUM, REQUEST_STARTALBUM);
        }
    }

    static void onRequestPermissionsResult(SelectAlbumActivity target, int requestCode, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STARTALBUM:
                if (PermissionUtils.getTargetSdkVersion(target) < 23 && !PermissionUtils.hasSelfPermissions(target, PERMISSION_STARTALBUM)) {
                    return;
                }
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    target.startAlbum();
                }
                break;
            default:
                break;
        }
    }
}*/
