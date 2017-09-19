package com.iyoho.social.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;

import com.iyoho.social.SelectAlbumActivity;
import com.iyoho.social.server.ServerInterfaces;

import permissions.dispatcher.PermissionUtils;

/**
 * Created by ab053167 on 2017/9/13.
 */

public class PermissionDispatcherHelper {
    private static final int REQUEST_STARTALBUM = 0;

    private static final String[] PERMISSION_STARTALBUM = new String[] {"android.permission.READ_EXTERNAL_STORAGE"};

    private static final int REQUEST_STARTCAMERA = 1;

    private static final String[] PERMISSION_STARTCAMERA = new String[] {"android.permission.CAMERA"};

    /////////Singleton//////////////////////
    private static volatile PermissionDispatcherHelper instance = null;

    public static PermissionDispatcherHelper getInstance() {

        if (instance == null) {
            synchronized (PermissionDispatcherHelper.class) {
                if (instance == null) {
                    instance = new PermissionDispatcherHelper();
                }
            }
        }

        return instance;
    }

    private PermissionDispatcherHelper() {
    }

    /*OnReadExternalStorageListener
       * */
    public interface OnReadExternalStorageListener {
        void onPermissionCompleted();
    }
    private static OnReadExternalStorageListener onReadExternalStorageListener;
    public void setOnReadExternalStorageListener(OnReadExternalStorageListener listener) {
        this.onReadExternalStorageListener = listener;
    }

    /*OnCameraListener
      * */
    public interface OnCameraListener {
        void onPermissionCompleted();
    }
    private static OnCameraListener onCameraListener;
    public void setOnCameraListener(OnCameraListener listener) {
        this.onCameraListener = listener;
    }



    public void startAlbumWithCheck(Activity context, OnReadExternalStorageListener listener) {
        setOnReadExternalStorageListener(listener);
        if (PermissionUtils.hasSelfPermissions(context, PERMISSION_STARTALBUM)) {
            if(onReadExternalStorageListener!=null){
                onReadExternalStorageListener.onPermissionCompleted();
            }
        } else {
            ActivityCompat.requestPermissions(context, PERMISSION_STARTALBUM, REQUEST_STARTALBUM);
        }
    }

    public void startCameraWithCheck(Activity context, OnCameraListener listener) {
        setOnCameraListener(listener);
        if (PermissionUtils.hasSelfPermissions(context, PERMISSION_STARTCAMERA)) {
            if(onCameraListener!=null){
                onCameraListener.onPermissionCompleted();
            }
        } else {
            ActivityCompat.requestPermissions(context, PERMISSION_STARTCAMERA, REQUEST_STARTCAMERA);
        }
    }

    public static void onRequestPermissionsResult(Activity context, int requestCode, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STARTALBUM:
                if (PermissionUtils.getTargetSdkVersion(context) < 23 && !PermissionUtils.hasSelfPermissions(context, PERMISSION_STARTALBUM)) {
                    return;
                }
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    if(onReadExternalStorageListener!=null){
                        onReadExternalStorageListener.onPermissionCompleted();
                    }
                }
                break;
            case REQUEST_STARTCAMERA:
                if (PermissionUtils.getTargetSdkVersion(context) < 23 && !PermissionUtils.hasSelfPermissions(context, PERMISSION_STARTCAMERA)) {
                    return;
                }
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    if(onCameraListener!=null){
                        onCameraListener.onPermissionCompleted();
                    }
                }
                break;
            default:
                break;
        }
    }
}

