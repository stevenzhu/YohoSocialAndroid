package com.iyoho.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ab053167 on 2017/9/12.
 */

public abstract class IBaseActivity extends AppCompatActivity implements View.OnClickListener {
    private static Context mContext;
    private static Class mClassValue;
    private static Bundle bundleValue;
//    private static OnActivityResult onActivityResult;
//    private static int requestCode;
    public interface OnActivityResult{
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
    public static void start(Activity activity,Class mClass,Bundle bundle){
        mContext=activity;
        bundleValue=bundle;
        mClassValue=mClass;
        Intent intent=new Intent(activity,mClass);
        intent.putExtra("bundle",bundle);
        activity.startActivity(intent);
    };
    public static void startForResult(Activity activity,Class mClass,Bundle bundle,int requestCode){
        mContext=activity;
        bundleValue=bundle;
        mClassValue=mClass;
        //onActivityResult=result;
        requestCode=requestCode;
        Intent intent=new Intent(activity,mClass);
        intent.putExtra("bundle",bundle);
        activity.startActivityForResult(intent,requestCode);
    };
    public static Bundle getBundle(Class mClass) throws Exception{
        if(mClassValue!=null&&mClassValue==mClass&&bundleValue!=null){
            return bundleValue;
        }
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        initView();
        initEvent();
        initData();
    }

    public abstract  Bundle getBundle() throws Exception;
    public abstract  int initLayout();
    public abstract  void initView();
    public abstract  void initEvent();
    public abstract  void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
