package com.yoho.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.yoho.glide.progress.OnGlideImageViewListener;
import com.yoho.glide.progress.OnGlideImageViewProgressListener;
import com.yoho.glide.progress.OnProgressListener;
import com.yoho.glide.progress.ProgressManager;
import com.yoho.glide.transformation.GlideCircleTransformation;

import java.lang.ref.WeakReference;

import static android.R.attr.resource;

/**
 * Created by zhugongzhao on 2017/6/6.
 */
public class GlideImageLoader {

    private static final String ANDROID_RESOURCE = "android.resource://";
    private static final String FILE = "file://";
    private static final String SEPARATOR = "/";
    private static final String HTTP = "http";

    private WeakReference<ImageView> mImageView;
    private Object mImageUrlObj;
    private long mTotalBytes = 0;
    private long mLastBytesRead = 0;
    private boolean mLastStatus = false;
    private Handler mMainThreadHandler;

    private OnProgressListener internalProgressListener;
    private OnGlideImageViewListener onGlideImageViewListener;
    private OnProgressListener onProgressListener;
    private OnGlideImageViewProgressListener onGlideImageViewProgressListener;
    public static GlideImageLoader create(ImageView imageView) {
        return new GlideImageLoader(imageView);
    }

    private GlideImageLoader(ImageView imageView) {
        mImageView = new WeakReference<>(imageView);
        mMainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public ImageView getImageView() {
        if (mImageView != null) {
            return mImageView.get();
        }
        return null;
    }

    public Context getContext() {
        if (getImageView() != null) {
            return getImageView().getContext();
        }
        return null;
    }

    public String getImageUrl() {
        if (mImageUrlObj == null) return null;
        if (!(mImageUrlObj instanceof String)) return null;
        return (String) mImageUrlObj;
    }

    public Uri resId2Uri(int resourceId) {
        if (getContext() == null) return null;
        return Uri.parse(ANDROID_RESOURCE + getContext().getPackageName() + SEPARATOR + resourceId);
    }

    public void load(int resId, RequestOptions options) {
        load(resId2Uri(resId), options);
    }
//    public void loadLocCircle(int resId, RequestOptions options) {
//        load(resId2Uri(resId), options.transform(new GlideCircleTransformation()));
//    }
//    public void loadLocCircle(int resId,int placeholderResId) {
//        load(resId2Uri(resId), circleRequestOptions(placeholderResId));
//    }
//    public void loadLocCircle(String localPath,int placeholderResId) {
//        if (localPath == null || getContext() == null) return;
//        requestBuilder(localPath, circleRequestOptions(placeholderResId)).into(getImageView());
//    }
//    public void loadLocCircle(String localPath, RequestOptions options) {
//        if (localPath == null || getContext() == null) return;
//        requestBuilder(localPath, options.transform(new GlideCircleTransformation())).into(getImageView());
//    }
//
//
//    public void loadCircle(String localPath, RequestOptions options) {
//        if (localPath == null || getContext() == null) return;
//        requestBuilder(localPath, options.transform(new GlideCircleTransformation())).into(getImageView());
//    }

    public void load(Uri uri, RequestOptions options) {
        if (uri == null || getContext() == null) return;
        requestBuilder(uri, options).into(getImageView());
    }
    public void load(String url) {
        if (url == null || getContext() == null) return;
        requestBuilder(url, null).into(getImageView());
    }
    public void load(String url, RequestOptions options) {
        if (url == null || getContext() == null) return;
        requestBuilder(url, options).into(getImageView());
    }


    public void load(String url, RequestOptions options,OnGlideImageViewListener onGlideImageViewListener) {
        if (url == null || getContext() == null) return;
        if(onGlideImageViewListener!=null){
            setOnGlideImageViewListener(url,onGlideImageViewListener);
        }
        requestBuilder(url, options).into(getImageView());
    }
    public void load(String url, RequestOptions options,OnProgressListener onProgressListener) {
        if (url == null || getContext() == null) return;
        if(onGlideImageViewListener!=null){
            setOnProgressListener(url,onProgressListener);
        }
        requestBuilder(url, options).into(getImageView());
    }
    public void load(String url, RequestOptions options,OnGlideImageViewProgressListener onGlideImageViewProgressListener) {
        if (url == null || getContext() == null) return;
        if(onGlideImageViewProgressListener!=null){
            setOnGlideImageViewProgressListener(url,onGlideImageViewProgressListener);
        }
        requestBuilder(url, options).into(getImageView());
    }

    public RequestBuilder<Drawable> requestBuilder(Object obj, RequestOptions options) {
        this.mImageUrlObj = obj;
        RequestListener requestListener=new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                mainThreadCallback(mLastBytesRead, mTotalBytes, true, e);
                ProgressManager.removeProgressListener(internalProgressListener);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                mainThreadCallback(mLastBytesRead, mTotalBytes, true, null);
                ProgressManager.removeProgressListener(internalProgressListener);
                return false;
            }
        };

        if(options==null){
            return Glide.with(getContext()).load(obj).listener(requestListener);
        }else{
            return Glide.with(getContext()).load(obj).apply(options).listener(requestListener);
        }

    }

    public RequestOptions requestOptions(int placeholderResId) {
        return requestOptions(placeholderResId, placeholderResId);
    }

    public RequestOptions requestOptions(int placeholderResId, int errorResId) {
        return new RequestOptions()
                .placeholder(placeholderResId)
                .error(errorResId);
    }

    public RequestOptions circleRequestOptions(int placeholderResId) {
        return circleRequestOptions(placeholderResId, placeholderResId);
    }

    public RequestOptions circleRequestOptions(int placeholderResId, int errorResId) {
        return requestOptions(placeholderResId, errorResId)
                .transform(new GlideCircleTransformation());
    }
    public void loadImage(String url) {
        load(url, null);
    }
    public void loadImage(String url, int placeholderResId) {
        load(url, requestOptions(placeholderResId));
    }
    public void loadImage(String url, RequestOptions requestOptions) {
        load(url, requestOptions);
    }

    public void loadImage(String url,OnGlideImageViewListener onGlideImageViewListener) {
        load(url, null,onGlideImageViewListener);
    }
    public void loadImage(String url, int placeholderResId,OnGlideImageViewListener onGlideImageViewListener) {
        load(url, requestOptions(placeholderResId),onGlideImageViewListener);
    }
    public void loadImage(String url, RequestOptions requestOptions,OnGlideImageViewListener onGlideImageViewListener) {
        load(url, requestOptions,onGlideImageViewListener);
    }

    public void loadImage(String url,OnProgressListener onProgressListener) {
        load(url, null,onProgressListener);
    }
    public void loadImage(String url, int placeholderResId,OnProgressListener onProgressListener) {
        load(url, requestOptions(placeholderResId),onProgressListener);
    }
    public void loadImage(String url, RequestOptions requestOptions,OnProgressListener onProgressListener) {
        load(url, requestOptions,onProgressListener);
    }

    public void loadImage(String url,OnGlideImageViewProgressListener onGlideImageViewProgressListener) {
        load(url, null,onGlideImageViewProgressListener);
    }
    public void loadImage(String url, int placeholderResId,OnGlideImageViewProgressListener onGlideImageViewProgressListener) {
        load(url, requestOptions(placeholderResId),onGlideImageViewProgressListener);
    }
    public void loadImage(String url, RequestOptions requestOptions,OnGlideImageViewProgressListener onGlideImageViewProgressListener) {
        load(url, requestOptions,onGlideImageViewProgressListener);
    }

//    public void loadCircleImage(String url) {
//        loadCircle(url, null);
//    }
//    public void loadCircleImage(String url, int placeholderResId) {
//        load(url, circleRequestOptions(placeholderResId));
//    }
//    public void loadCircleImage(String url, RequestOptions requestOptions) {
//        loadCircle(url, requestOptions);
//    }


    public void loadLocalImage(@DrawableRes int resId, int placeholderResId) {
        load(resId, requestOptions(placeholderResId));
    }
    public void loadLocalImage(@DrawableRes int resId, RequestOptions requestOptions) {
        load(resId, requestOptions);
    }
    public void loadLocalImage(String localPath, int placeholderResId) {
        load(FILE + localPath, requestOptions(placeholderResId));
    }
    public void loadLocalImage(String localPath, RequestOptions requestOptions) {
        load(FILE + localPath,requestOptions);
    }

//    public void loadLocalCircleImage(int resId, int placeholderResId) {load(resId, circleRequestOptions(placeholderResId));}
//    public void loadLocalCircleImage(int resId, RequestOptions requestOptions) {loadLocCircle(resId,requestOptions);}
//    public void loadLocalCircleImage(String localPath,int placeholderResId) {load(FILE + localPath,  circleRequestOptions(placeholderResId));}
//    public void loadLocalCircleImage(String localPath,RequestOptions requestOptions) {
//        load(FILE + localPath, requestOptions);
//    }

    private void addProgressListener() {
        if (getImageUrl() == null) return;
        final String url = getImageUrl();
        if (!url.startsWith(HTTP)) return;

        internalProgressListener = new OnProgressListener() {
            @Override
            public void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone, GlideException exception) {
                if (totalBytes == 0) return;
                if (!url.equals(imageUrl)) return;
                if (mLastBytesRead == bytesRead && mLastStatus == isDone) return;

                mLastBytesRead = bytesRead;
                mTotalBytes = totalBytes;
                mLastStatus = isDone;
                mainThreadCallback(bytesRead, totalBytes, isDone, exception);

                if (isDone) {
                    ProgressManager.removeProgressListener(this);
                }
            }
        };
        ProgressManager.addProgressListener(internalProgressListener);
    }

    private void mainThreadCallback(final long bytesRead, final long totalBytes, final boolean isDone, final GlideException exception) {
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                final int percent = (int) ((bytesRead * 1.0f / totalBytes) * 100.0f);
                if (onProgressListener != null) {
                    onProgressListener.onProgress((String) mImageUrlObj, bytesRead, totalBytes, isDone, exception);
                }

                if (onGlideImageViewListener != null) {
                    onGlideImageViewListener.onProgress(percent, isDone, exception);
                }
                if(onGlideImageViewProgressListener!=null){
                    onGlideImageViewProgressListener.onProgress((String) mImageUrlObj, bytesRead, totalBytes,percent, isDone, exception);
                }
            }
        });
    }

    public void setOnGlideImageViewListener(String imageUrl, OnGlideImageViewListener onGlideImageViewListener) {
        this.mImageUrlObj = imageUrl;
        this.onGlideImageViewListener = onGlideImageViewListener;
        addProgressListener();
    }

    public void setOnProgressListener(String imageUrl, OnProgressListener onProgressListener) {
        this.mImageUrlObj = imageUrl;
        this.onProgressListener = onProgressListener;
        addProgressListener();
    }
    public void setOnGlideImageViewProgressListener(String imageUrl, OnGlideImageViewProgressListener onGlideImageViewProgressListener) {
        this.mImageUrlObj = imageUrl;
        this.onGlideImageViewProgressListener = onGlideImageViewProgressListener;
        addProgressListener();
    }
}
