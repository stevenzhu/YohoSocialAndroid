package com.iyoho.social.Entry;

/**
 * Created by ab053167 on 2017/9/12.
 */

public class MessageEvent {
    private String message1;
    private String message2;
    private String message3;
    private String tag;
    private Object obj;
    private Class mClass;
    public MessageEvent(Class mClass){
        this.mClass=mClass;
    }
    public MessageEvent(Class mClass,Object obj){
        this.mClass=mClass;
        this.obj=obj;
    }
    public MessageEvent(Class mClass,String message1){
        this.mClass=mClass;
        this.message1=message1;
    }
    public MessageEvent(Class mClass,String message1,String message2){
        this.mClass=mClass;
        this.message1=message1;
        this.message2=message2;
    }
    public MessageEvent(Class mClass,String message1,String message2,String message3){
        this.mClass=mClass;
        this.message1=message1;
        this.message2=message2;
        this.message3=message3;
    }
    public void setTag(String tag){
        this.tag=tag;
    }
    public String getTag(){
        return this.tag;
    }
    public String getMessage1() {
        return message1;
    }

    public String getMessage2() {
        return message2;
    }

    public String getMessage3() {
        return message3;
    }

    public Object getObj() {
        return obj;
    }

    public Class getmClass() {
        return mClass;
    }
}
