package com.yoho.glide.progress;

import com.bumptech.glide.load.engine.GlideException;

/**
 * Created by zhugongzhao on 2017/6/14.
 */
public interface OnGlideImageViewProgressListener {

    void onProgress(String imageUrl, long bytesRead, long totalBytes,int percent, boolean isDone, GlideException exception);
}
