package com.u9porn.ui.download;

import androidx.lifecycle.Lifecycle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.u9porn.di.PerFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class DownloadActivityModule {
    @PerFragment
    @ContributesAndroidInjector
    abstract DownloadingFragment downloadingFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract FinishedFragment finishedFragment();

    @Provides
    static List<Fragment> providesFragmentList() {
        return new ArrayList<>();
    }


    @Provides
    static AppCompatActivity providesAppCompatActivity(DownloadActivity downloadActivity){
        return downloadActivity;
    }

    @Provides
    static FragmentManager providesSupportFragmentManager(AppCompatActivity mAppCompatActivity) {
        return mAppCompatActivity.getSupportFragmentManager();
    }

    @Provides
    static LifecycleProvider<Lifecycle.Event> providerLifecycleProvider(AppCompatActivity mAppCompatActivity) {
        return AndroidLifecycle.createLifecycleProvider(mAppCompatActivity);
    }
}
