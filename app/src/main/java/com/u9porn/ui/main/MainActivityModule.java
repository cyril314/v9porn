package com.u9porn.ui.main;

import android.arch.lifecycle.Lifecycle;
import android.support.v7.app.AppCompatActivity;

import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.u9porn.di.PerFragment;
import com.u9porn.ui.axgle.AxgleFragment;
import com.u9porn.ui.axgle.MainAxgleFragment;
import com.u9porn.ui.images.MainDBMeiZiFragment;
import com.u9porn.ui.images.MainMeiZiTuFragment;
import com.u9porn.ui.images.douban.DouBanFragment;
import com.u9porn.ui.images.meizitu.MeiZiTuFragment;
import com.u9porn.ui.kedouwo.KeDouFragment;
import com.u9porn.ui.kedouwo.MainKeDouFragment;
import com.u9porn.ui.mine.MineFragment;
import com.u9porn.ui.music.MusicFragment;
import com.u9porn.ui.pxgav.MainPxgavFragment;
import com.u9porn.ui.pxgav.PxgavFragment;
import com.u9porn.ui.porn9forum.Forum9IndexFragment;
import com.u9porn.ui.porn9forum.ForumFragment;
import com.u9porn.ui.porn9forum.Main9ForumFragment;
import com.u9porn.ui.porn9video.Main9PronVideoFragment;
import com.u9porn.ui.porn9video.comment.CommentFragment;
import com.u9porn.ui.porn9video.index.IndexFragment;
import com.u9porn.ui.porn9video.videolist.VideoListFragment;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {
    @PerFragment
    @ContributesAndroidInjector
    abstract VideoListFragment videoListFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract PxgavFragment pavFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract MainPxgavFragment mainPavFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract IndexFragment indexFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract MeiZiTuFragment meiZiTuFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract MainMeiZiTuFragment mainMeiZiTuFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract MainDBMeiZiFragment mainDBMeiZiFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract DouBanFragment douBanFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract MineFragment mineFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract Main9ForumFragment main9ForumFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract ForumFragment forumFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract Forum9IndexFragment forum9IndexFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract CommentFragment commentFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract Main9PronVideoFragment main9PronVideoFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract MainAxgleFragment mainAxgleFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract AxgleFragment axgleFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract MainKeDouFragment mainKeDouFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract KeDouFragment KeDouFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract MusicFragment musicFragment();

    @Provides
    static AppCompatActivity provideAppCompatActivity(MainActivity mainActivity){
        return mainActivity;
    }

    @Provides
    static LifecycleProvider<Lifecycle.Event> providerLifecycleProvider(AppCompatActivity mAppCompatActivity) {
        return AndroidLifecycle.createLifecycleProvider(mAppCompatActivity);
    }
}
