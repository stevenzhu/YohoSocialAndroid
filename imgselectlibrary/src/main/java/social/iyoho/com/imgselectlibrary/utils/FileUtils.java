package social.iyoho.com.imgselectlibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import social.iyoho.com.imgselectlibrary.utils.luban.Luban;
import social.iyoho.com.imgselectlibrary.utils.luban.OnCompressListener;
import social.iyoho.com.imgselectlibrary.utils.luban.OnPhotoCompressListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dee on 15/11/20.
 */
public class FileUtils {
    public static final String POSTFIX = ".JPEG";
    public static final String APP_NAME = "ImageSelector";
    public static final String CAMERA_PATH = "/" + APP_NAME + "/CameraImage/";
    public static final String CROP_PATH = "/" + APP_NAME + "/CropImage/";
    public static  OnPhotoCompressListener mylistener=null;
    public static File createCameraFile(Context context) {
        return createMediaFile(context,CAMERA_PATH);
    }
    public static File createCropFile(Context context) {
        return createMediaFile(context,CROP_PATH);
    }

    private static File createMediaFile(Context context,String parentPath){
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED)?Environment.getExternalStorageDirectory():context.getCacheDir();

        File folderDir = new File(rootDir.getAbsolutePath() + parentPath);
        if (!folderDir.exists() && folderDir.mkdirs()){

        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String fileName = APP_NAME + "_" + timeStamp + "";
        File tmpFile = new File(folderDir, fileName + POSTFIX);
        return tmpFile;
    }
    /**
     *
     * @param imgPath
     * @param bitmap
     * @return
     */
    public static String imgToBase64(Context context,String imgPath, Bitmap bitmap) {
        if (imgPath !=null && imgPath.length() > 0) {
            bitmap = readBitmap(imgPath);
        }
        if(bitmap == null){
            Toast.makeText(context,"bitmap not found!!",Toast.LENGTH_SHORT).show();
            return null;
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();

            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static Bitmap readBitmap(String imgPath) {
        try {
            return BitmapFactory.decodeFile(imgPath);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }

    }

    /**
     *
     * @param base64Data
     * @param imgName
     */
    public static void base64ToBitmap(String base64Data,String imgName) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        File myCaptureFile = new File("/sdcard/", imgName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myCaptureFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        boolean isTu = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        if (isTu) {
            // fos.notifyAll();
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
           }
        }
    }


    //圖片上传前的壓縮處理

    public static void  photoCompress(final Context context, final ArrayList<String> photos, OnPhotoCompressListener listener){
        mylistener=listener;
        final ArrayList<String> images=new ArrayList<String>();
        for(int x=0;x<photos.size();x++){
            File file=new File(photos.get(x));
        if(file.length()/1024<=60){
            Log.d("--", "@@FirstLength="+file.length()/ 1024 + "k, @@"+"path= " + photos.get(x));
            images.add(photos.get(x));
            if(images.size()==photos.size()){
                mylistener.complete(images);
            }
        }else{
            //使用鲁班二次压缩
            Luban.get(context).load(file).putGear(Luban.FIRST_GEAR).setCompressListener(new OnCompressListener() {
                @Override
                public void onStart() {
                    // TODO 压缩开始前调用，可以在方法内启动 loading UI
                }

                @Override
                public void onSuccess(File file) {
                    // TODO 压缩成功后调用，返回压缩后的图片文件
                    Log.d("--", "@@SecondLength="+file.length()/ 1024 + "k, @@"+"path= " + file.getAbsolutePath());
                    images.add(file.getAbsolutePath());
                    if(images.size()==photos.size()){
                        mylistener.complete(images);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    // TODO 当压缩过去出现问题时调用

                }
            }).launch();

        }
        }

      // return images;


    }

    //圖片上传前的壓縮處理

    public static void  photoCompressToBase64(final Context context,final ArrayList<String> photos, OnPhotoCompressListener listener){
        final ArrayList<String> images = new ArrayList<String>();
        mylistener=listener;
        for(int x=0;x<photos.size();x++){

            File file=new File(photos.get(x));
            if(file.length()/1024<=60){
                Log.d("--", "@@FirstLength="+file.length()/ 1024 + "k, @@"+"path= " + photos.get(x));
                images.add(FileUtils.imgToBase64(context,images.get(x),null));
                if(images.size()==photos.size()){
                    mylistener.complete(images);
                }
            }else{
                //使用鲁班二次压缩
                Luban.get(context).load(file).putGear(Luban.FIRST_GEAR).setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI

                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        Log.d("--", "@@SecondLength="+file.length()/ 1024 + "k, @@"+"path= " + file.getAbsolutePath());
                        images.add(FileUtils.imgToBase64(context,file.getAbsolutePath(),null));
                        if(images.size()==photos.size()){
                            mylistener.complete(images);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过去出现问题时调用

                    }
                }).launch();
            }
        }
    }
}
