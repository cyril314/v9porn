package com.u9porn.ui.pxgav.playpxgav;

import androidx.lifecycle.Lifecycle;
import androidx.appcompat.app.AppCompatActivity;

import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class PlayPxgavActivityModule {
    @Provides
    AppCompatActivity provideAppCompatActivity(PlayPxgavActivity playPxgavActivity){
        return playPxgavActivity;
    }

    @Provides
    static LifecycleProvider<Lifecycle.Event> providerLifecycleProvider(AppCompatActivity mAppCompatActivity) {
        return AndroidLifecycle.createLifecycleProvider(mAppCompatActivity);
    }
}
