package com.u9porn.ui.proxy;

import androidx.lifecycle.Lifecycle;
import androidx.appcompat.app.AppCompatActivity;

import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class ProxySettingActivityModule {
    @Provides
    static AppCompatActivity provideAppCompatActivity(ProxySettingActivity proxySettingActivity){
        return proxySettingActivity;
    }

    @Provides
    static LifecycleProvider<Lifecycle.Event> providerLifecycleProvider(AppCompatActivity mAppCompatActivity) {
        return AndroidLifecycle.createLifecycleProvider(mAppCompatActivity);
    }
}
