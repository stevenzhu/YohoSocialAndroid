package com.iyoho.social.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SystemUtil {
    static float upX = 0;
    static float downX = 0;
    static float upY = 0;
    static float downY = 0;
    /////////Singleton//////////////////////
    public static long lastClick = 0;
    public static String headStyle = "B03";
    private static volatile SystemUtil instance = null;

    private Application application;

    public static SystemUtil getInstance() {

        if (instance == null) {
            synchronized (SystemUtil.class) {
                if (instance == null) {
                    instance = new SystemUtil();
                }
            }
        }

        return instance;
    }

    private SystemUtil() {

    }

    //////////////////////////////////////////

    public Application getApplication() {
        if (application == null) {
            try {
                Class activityThread = Class.forName("android.app.ActivityThread");
                Method m = activityThread.getMethod("currentApplication", new Class[0]);
                m.setAccessible(true);
                application = (Application) m.invoke(null, new Object[0]);
                return application;
            } catch (Throwable ignore) {
                throw new RuntimeException("Failed to get current application!");
            }
        } else {
            return application;
        }
    }

    public String getDeviceID() {
        try {
            Context context = getApplication();
            String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (TextUtils.isEmpty(id)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
                id = tm.getDeviceId();

                if (TextUtils.isEmpty(id)) {
                    id = Build.SERIAL;
                }
            }
            return id;
        } catch (Exception ex) {
            return Build.SERIAL;
        }
    }

    public static String getVersionName(Context context) {

        try {
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String versionName = packInfo.versionName;
            return versionName;
        } catch (Exception ex) {
            return "";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int versionCode = packInfo.versionCode;
            return versionCode;
        } catch (Exception ex) {
            return -1;
        }
    }
    @SuppressLint("SimpleDateFormat")
    public static String GetNow() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        return sDateFormat.format(new Date());
    }

    @SuppressLint("SimpleDateFormat")
    public static Date GetDateTime(String date) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date ret = null;

        try {
            ret = sDateFormat.parse(date);
        } catch (ParseException e) {
        }

        return ret;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pInfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<>();
        if (pInfo != null) {
            for (int i = 0; i < pInfo.size(); i++) {
                String pn = pInfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    public static boolean isAppInstalled2(Context context, String packagename) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getApplicationInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isDoubleClick() {
        if (System.currentTimeMillis() - lastClick <= 500) {
            return true;
        }
        lastClick = System.currentTimeMillis();
        return false;
    }

    public String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplication().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplication().getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if ((appProcess.processName.equals(packageName) || appProcess.processName.contains("ABDoor.webview"))
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }


    public static void setSelectBg(final Context context, final View layout, final String bgImageUrl, final String bgRgb) {
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (bgImageUrl != null && !"".equals(bgImageUrl)) {
                    //---
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();

                        Glide.with(context.getApplicationContext()).asBitmap().load(bgImageUrl).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                Drawable drawable = new BitmapDrawable(resource);
                                drawable.setColorFilter(Color.parseColor("#cccccc"), PorterDuff.Mode.MULTIPLY);
                                layout.setBackgroundDrawable(drawable);
                            }
                        });
                    }

                    if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        Glide.with(context.getApplicationContext()).asBitmap().load(bgImageUrl).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                Drawable drawable = new BitmapDrawable(resource);
                                drawable.clearColorFilter();
                                layout.setBackgroundDrawable(drawable);
                            }

                        });
                        upX = motionEvent.getX();
                        upY = motionEvent.getY();

                        if (upY - downY >= 60 || downY - upY >= 60 || downX - upX >= 60 || upX - downX >= 60) {
                            return true;
                        }
                    }

                    //---
                } else if (bgRgb != null && !"".equals(bgRgb)) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        try {
                            layout.setBackgroundColor(Color.parseColor(bgRgb.substring(0, 1) + "dd" + bgRgb.substring(1)));
                        } catch (Exception ex) {
                            layout.setBackgroundColor(Color.parseColor("#55b3b3b3"));
                            ex.printStackTrace();
                        }
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        upX = motionEvent.getX();
                        upY = motionEvent.getY();
                        try {
                            layout.setBackgroundColor(Color.parseColor(bgRgb));
                        } catch (Exception ex) {
                        }
                        if (upY - downY >= 60 || downY - upY >= 60 || downX - upX >= 60 || upX - downX >= 60) {
                            return true;
                        }
                    }
                } else {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        layout.setBackgroundColor(Color.parseColor("#55b3b3b3"));
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        layout.setBackgroundColor(Color.parseColor("#ffffff"));
                        upX = motionEvent.getX();
                        upY = motionEvent.getY();
                        if (upY - downY >= 60 || downY - upY >= 60 || downX - upX >= 60 || upX - downX >= 60) {
                            return true;
                        }
                    }
                }

                return false;
            }
        });
    }

    public static void setRightSelectBg(final Context context, final View layout, final String bgImageUrl, final String bgRgb, final String defaultColor) {
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (bgImageUrl != null && !"".equals(bgImageUrl)) {
                    //---
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                        Glide.with(context.getApplicationContext()).asBitmap().load(bgImageUrl).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                Drawable drawable = new BitmapDrawable(resource);
                                drawable.setColorFilter(Color.parseColor("#cccccc"), PorterDuff.Mode.MULTIPLY);
                                layout.setBackgroundDrawable(drawable);
                            }
                        });

                    }
                    //Log.e("666666", String.format("motion: %d", motionEvent.getAction()));
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        upX = motionEvent.getX();
                        upY = motionEvent.getY();
                        Glide.with(context.getApplicationContext()).asBitmap().load(bgImageUrl).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                Drawable drawable = new BitmapDrawable(resource);
                                drawable.clearColorFilter();
                                layout.setBackgroundDrawable(drawable);
                            }
                        });
                        if (upY - downY >= 60 || downY - upY >= 60 || downX - upX >= 60 || upX - downX >= 60) {
                            return true;
                        }
                    }
                    //---
                } else if (bgRgb != null && !"".equals(bgRgb)) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                        try {
                            layout.setBackgroundColor(Color.parseColor(bgRgb.substring(0, 1) + "dd" + bgRgb.substring(1)));
                        } catch (Exception ex) {
                            layout.setBackgroundColor(Color.parseColor("#55b3b3b3"));
                            ex.printStackTrace();
                        }
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        upX = motionEvent.getX();
                        upY = motionEvent.getY();
                        try {
                            layout.setBackgroundColor(Color.parseColor(bgRgb));
                        } catch (Exception ex) {
                        }
                        if (upY - downY >= 60 || downY - upY >= 60 || downX - upX >= 60 || upX - downX >= 60) {
                            return true;
                        }
                    }
                } else if (defaultColor != null && !"".equals(defaultColor)) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                        try {
                            layout.setBackgroundColor(Color.parseColor(defaultColor.substring(0, 1) + "dd" + defaultColor.substring(1)));
                        } catch (Exception ex) {
                            layout.setBackgroundColor(Color.parseColor("#55b3b3b3"));
                            ex.printStackTrace();
                        }
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        upX = motionEvent.getX();
                        upY = motionEvent.getY();
                        try {
                            layout.setBackgroundColor(Color.parseColor(defaultColor));
                        } catch (Exception ex) {
                        }
                        if (upY - downY >= 60 || downY - upY >= 60 || downX - upX >= 60 || upX - downX >= 60) {
                            return true;
                        }
                    }
                }

                return false;
            }
        });
    }

    public static void setSelectBg(final Context context, final View layout, final String bgRgb) {
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (bgRgb != null && !"".equals(bgRgb)) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        try {
                            layout.setBackgroundColor(Color.parseColor("#dddddd"));
                        } catch (Exception ex) {
                            layout.setBackgroundColor(Color.parseColor("#ffb3b3b3"));
                            ex.printStackTrace();
                        }
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        upX = motionEvent.getX();
                        upY = motionEvent.getY();
                        try {
                            layout.setBackgroundColor(Color.parseColor(bgRgb));
                        } catch (Exception ex) {
                        }
                        if (upY - downY >= 60 || downY - upY >= 60 || downX - upX >= 60 || upX - downX >= 60) {
                            return true;
                        }
                    }
                } else {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        layout.setBackgroundColor(Color.parseColor("#ffb3b3b3"));
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        layout.setBackgroundColor(Color.parseColor("#ffffff"));
                        upX = motionEvent.getX();
                        upY = motionEvent.getY();
                        if (upY - downY >= 60 || downY - upY >= 60 || downX - upX >= 60 || upX - downX >= 60) {
                            return true;
                        }
                    }
                }

                return false;
            }
        });
    }

    /**
     * 判断该设备是否支持计歩
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isSupportStepCountSensor(Context context) {
        SensorManager sensorManager = (SensorManager) context
                .getSystemService(context.SENSOR_SERVICE);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        return countSensor != null || detectorSensor != null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public boolean isSupportStepCountSensor2(Context context) {
        return context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER);
    }

}
