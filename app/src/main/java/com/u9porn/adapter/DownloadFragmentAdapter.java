package com.u9porn.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.u9porn.ui.BaseFragment;

import java.util.List;

import javax.inject.Inject;

/**
 * @author flymegoc
 * @date 2017/11/24
 * @describe
 */

public class DownloadFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    @Inject
    public DownloadFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<Fragment> fragmentList) {
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList == null ? "" : ((BaseFragment) fragmentList.get(position)).getTitle();
    }
}
