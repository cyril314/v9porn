package com.u9porn.ui.download;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import com.u9porn.utils.Logger;
import com.u9porn.R;
import com.u9porn.adapter.DownloadFragmentAdapter;
import com.u9porn.ui.BaseAppCompatActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author
 */
public class DownloadActivity extends BaseAppCompatActivity {

    private static final String TAG = DownloadActivity.class.getSimpleName();

    @BindView(R.id.download_viewpager)
    ViewPager downloadViewpager;
    @BindView(R.id.download_tab)
    TabLayout downloadTab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    protected DownloadingFragment downloadingFragment;

    @Inject
    protected FinishedFragment finishedFragment;

    @Inject
    List<Fragment> fragmentList;

    @Inject
    DownloadFragmentAdapter downloadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
        initToolBar(toolbar);

        fragmentList.add(downloadingFragment);
        fragmentList.add(finishedFragment);

        downloadAdapter.setData(fragmentList);

        downloadViewpager.setAdapter(downloadAdapter);
        downloadTab.setupWithViewPager(downloadViewpager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.t(TAG).d("onDestroy()");
    }
}
