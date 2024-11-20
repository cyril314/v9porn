package com.u9porn.utils;

import com.u9porn.data.prefs.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author flymegoc
 * @date 2018/1/26
 */

public class AddressHelper {

	private static Random mRandom;
	private PreferencesHelper preferencesHelper;

	/**
	 * 无需手动初始化,已在di中全局单例
	 */
	public AddressHelper(PreferencesHelper preferencesHelper) {
		mRandom = new Random();
		this.preferencesHelper = preferencesHelper;
	}

	/**
	 * 获取随机ip地址
	 */
	public String getRandomIPAddress() {
		StringBuilder sb = new StringBuilder();
		sb.append(mRandom.nextInt(100)).append(".");
		sb.append(mRandom.nextInt(255)).append(".");
		sb.append(mRandom.nextInt(255)).append(".");
		sb.append(mRandom.nextInt(255));
		return sb.toString();
	}

	public String getVideo9PornAddress() {
		return preferencesHelper.getPorn9VideoAddress();
	}

	public String getForum9PornAddress() {
		return preferencesHelper.getPorn9ForumAddress();
	}

	public String getPavAddress() {
		return preferencesHelper.getPavAddress();
	}

	public String getAxgleAddress() {
		return preferencesHelper.getAxgleAddress();
	}

	public String getKeDouWoAddress() {
		return preferencesHelper.getKeDouWoAddress();
	}
}
