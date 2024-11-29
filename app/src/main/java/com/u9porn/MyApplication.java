package com.u9porn;

import android.content.Context;
import androidx.multidex.MultiDex;
import androidx.appcompat.app.AppCompatDelegate;
import android.webkit.WebView;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import com.helper.loadviewhelper.load.LoadViewHelper;
import com.liulishuo.filedownloader.FileDownloader;
import com.squareup.leakcanary.LeakCanary;
import com.u9porn.data.DataManager;
import com.u9porn.di.component.DaggerAppComponent;
import com.u9porn.eventbus.LowMemoryEvent;
import com.u9porn.utils.AddressHelper;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * 应用入口
 */
public class MyApplication extends DaggerApplication {

	private static final String TAG = MyApplication.class.getSimpleName();

	@Inject
	DataManager dataManager;
	@Inject
	WebView mWebView;
	@Inject
	AddressHelper addressHelper;

	private static MyApplication myApplication;

	@Override
	public void onCreate() {
		super.onCreate();
		myApplication = this;
		initNightMode();
		initLeakCanary();
		initLoadingHelper();
		initFileDownload();
		if (!BuildConfig.DEBUG) {
			//初始化bug收集
			//  Bugsnag.init(this);
		}
		BGASwipeBackHelper.init(this, null);
	}

	public static MyApplication getInstance() {
		return myApplication;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	private void initNightMode() {
		boolean isNightMode = dataManager.isOpenNightMode();
		AppCompatDelegate.setDefaultNightMode(isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
	}

	private void initFileDownload() {
		FileDownloader.setup(this);
	}

	/**
	 * 初始化加载界面，空界面等
	 */
	private void initLoadingHelper() {
		LoadViewHelper.getBuilder().setLoadEmpty(R.layout.empty_view).setLoadError(R.layout.error_view).setLoadIng(R.layout.loading_view);
	}

	/**
	 * 初始化内存分析工具
	 */
	private void initLeakCanary() {
		if (LeakCanary.isInAnalyzerProcess(this)) {
			// This process is dedicated to LeakCanary for heap analysis.
			// You should not init your app in this process.
			return;
		}
		LeakCanary.install(this);
		// Normal app init code...
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		boolean canReleaseMemory = dataManager.isForbiddenAutoReleaseMemory();
		if (!canReleaseMemory) {
			EventBus.getDefault().post(new LowMemoryEvent(TAG));
		}
	}

	public DataManager getDataManager() {
		return dataManager;
	}

	public WebView getWebView() {
		return mWebView;
	}

	public AddressHelper getAddressHelper() {
		return addressHelper;
	}

	@Override
	protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
		return DaggerAppComponent.builder().application(this).build();
	}
}
