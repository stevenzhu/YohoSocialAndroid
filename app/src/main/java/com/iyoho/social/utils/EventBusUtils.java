package com.iyoho.social.utils;

import android.content.Context;

import com.iyoho.social.Entry.MessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ab053167 on 2017/9/12.
 */

public class EventBusUtils {

    public static void register(Context context){
        if(!EventBus.getDefault().isRegistered(context)){
            EventBus.getDefault().register(context);
        }
    }

    public static void unregister(Context context){
        if(EventBus.getDefault().isRegistered(context)){
            EventBus.getDefault().unregister(context);
        }
    };

    public static void post(MessageEvent subscriber){
        EventBus.getDefault().post(subscriber);
    }
}
