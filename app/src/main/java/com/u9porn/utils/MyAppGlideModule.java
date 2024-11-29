package com.u9porn.utils;

import android.content.Context;
import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.AppGlideModule;


/**
 * @author flymegoc
 * @date 2018/1/14
 */
@GlideModule
public final class MyAppGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, GlideBuilder builder) {
        int diskCacheSizeBytes = 1024 * 1024 * 250;
        builder.setDiskCache(new DiskLruCacheFactory(AppCacheUtils.getGlideDiskCacheDir(context).getAbsolutePath(), diskCacheSizeBytes));
    }
}
