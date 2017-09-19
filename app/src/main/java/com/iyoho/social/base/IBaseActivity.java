package com.iyoho.social.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by ab053167 on 2017/9/12.
 */

public abstract class IBaseActivity extends AppCompatActivity implements View.OnClickListener {
    private static Context mContext;
    private static Class mClassValue;
    private static Bundle bundleValue;
    public static void start(Context context,Class mClass,Bundle bundle){
        mContext=context;
        bundleValue=bundle;
        mClassValue=mClass;
        Intent intent=new Intent(context,mClass);
        intent.putExtra("bundle",bundle);
        context.startActivity(intent);
    };
    public static Bundle getBundle(Class mClass){
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



    public abstract  Bundle getBundle();
    public abstract  int initLayout();
    public abstract  void initView();
    public abstract  void initEvent();
    public abstract  void initData();

}
