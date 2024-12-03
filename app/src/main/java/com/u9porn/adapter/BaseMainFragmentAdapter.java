package com.u9porn.adapter;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import android.view.ViewGroup;

import com.u9porn.data.db.entity.Category;
import com.u9porn.ui.axgle.AxgleFragment;
import com.u9porn.ui.images.douban.DouBanFragment;
import com.u9porn.ui.images.meizitu.MeiZiTuFragment;
import com.u9porn.ui.kedouwo.KeDouFragment;
import com.u9porn.ui.pxgav.PxgavFragment;
import com.u9porn.ui.porn9forum.Forum9IndexFragment;
import com.u9porn.ui.porn9forum.ForumFragment;
import com.u9porn.ui.porn9video.index.IndexFragment;
import com.u9porn.ui.porn9video.videolist.VideoListFragment;

import java.util.List;

/**
 * @author
 * @date 2017/11/24
 * @describe
 */

public class BaseMainFragmentAdapter extends FragmentPagerAdapter {

	private static final String TAG = BaseMainFragmentAdapter.class.getSimpleName();
	private List<Category> categoryList;
	private int categoryType;
	private FragmentTransaction mCurTransaction;
	private FragmentManager mFragmentManager;

	private boolean isDestroy = false;

	public BaseMainFragmentAdapter(FragmentManager fm, List<Category> categoryList, int categoryType) {
		super(fm);
		mFragmentManager = fm;
		this.categoryList = categoryList;
		this.categoryType = categoryType;
	}

	public boolean isDestroy() {
		return isDestroy;
	}

	public void setDestroy(boolean destroy) {
		isDestroy = destroy;
	}

	@Override
	public Fragment getItem(int position) {
		return buildFragmentItem(categoryType, position);
	}

	@Override
	public int getCount() {
		return categoryList.size();
	}

	@Override
	public int getItemPosition(@NonNull Object object) {
		return POSITION_NONE;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return categoryList.get(position).getCategoryName();
	}

	@Override
	public long getItemId(int position) {
		return categoryList.get(position).getId();
	}

	private Fragment buildFragmentItem(int type, int position) {
		Category category = categoryList.get(position);
		switch (type) {
			case Category.TYPE_91PORN:
				if ("index".equalsIgnoreCase(category.getCategoryValue())) {
					IndexFragment indexFragment = IndexFragment.getInstance();
					indexFragment.setCategory(category);
					return indexFragment;
				} else {
					VideoListFragment videoListFragment = VideoListFragment.getInstance();
					videoListFragment.setCategory(category);
					return videoListFragment;
				}

			case Category.TYPE_91PORN_FORUM:
				if (("index").equals(category.getCategoryValue())) {
					Forum9IndexFragment forum9IndexFragment = Forum9IndexFragment.getInstance();
					forum9IndexFragment.setCategory(category);
					return forum9IndexFragment;
				} else {
					ForumFragment forumFragment = ForumFragment.getInstance();
					forumFragment.setCategory(category);
					return forumFragment;
				}
			case Category.TYPE_MEI_ZI_TU:
				MeiZiTuFragment meiZiTuFragment = MeiZiTuFragment.getInstance();
				meiZiTuFragment.setCategory(category);
				return meiZiTuFragment;
			case Category.TYPE_PXG_AV:
				PxgavFragment pigAvFragment = PxgavFragment.getInstance();
				pigAvFragment.setCategory(category);
				return pigAvFragment;
			case Category.TYPE_AXGLE:
				AxgleFragment axgleFragment = AxgleFragment.getInstance();
				axgleFragment.setCategory(category);
				return axgleFragment;
			case Category.TYPE_DOU_BAN:
				DouBanFragment douBanFragment = DouBanFragment.getInstance();
				douBanFragment.setCategory(category);
				return douBanFragment;
			case Category.TYPE_KE_DOU_WO:
				KeDouFragment keDouFragment = KeDouFragment.getInstance();
				keDouFragment.setCategory(category);
				return keDouFragment;
			default:
		}
		return new Fragment();
	}

	@SuppressLint("CommitTransaction")
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (isDestroy) {
			if (mCurTransaction == null) {
				mCurTransaction = mFragmentManager.beginTransaction();
			}
			mCurTransaction.remove((Fragment) object);
		} else {
			super.destroyItem(container, position, object);
		}

	}

	@Override
	public void finishUpdate(ViewGroup container) {
		super.finishUpdate(container);
		if (isDestroy) {
			if (mCurTransaction != null) {
				mCurTransaction.commitNowAllowingStateLoss();
				mCurTransaction = null;
			}
		}
	}
}
