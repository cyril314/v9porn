package com.u9porn.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.u9porn.data.network.Api;
import com.u9porn.utils.AgentUtil;
import com.u9porn.R;
import com.u9porn.data.model.MeiZiTu;
import com.u9porn.utils.GlideApp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 妹子图适配器
 */
public class MeiZiTuAdapter extends BaseQuickAdapter<MeiZiTu, BaseViewHolder> {
	private Map<String, Integer> heightMap = new HashMap<>();
	private int width;

	public MeiZiTuAdapter(int layoutResId, @Nullable List<MeiZiTu> data) {
		super(layoutResId, data);
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	protected void convert(BaseViewHolder helper, MeiZiTu item) {
		ImageView imageView = helper.getView(R.id.iv_item_mei_zi_tu);
		GlideApp.with(helper.itemView.getContext()).load(buildGlideUrl(item.getThumbUrl())).transition(new DrawableTransitionOptions().crossFade(300)).into(imageView);
		int height;
		if (!heightMap.containsKey(item.getThumbUrl())) {
			height = item.getHeight() * width / item.getWidth();
			heightMap.put(item.getThumbUrl(), height);
		} else {
			height = heightMap.get(item.getThumbUrl());
		}
		StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) helper.itemView.getLayoutParams();
		layoutParams.height = height;
		helper.itemView.setLayoutParams(layoutParams);
	}

	private GlideUrl buildGlideUrl(String url) {
		if (TextUtils.isEmpty(url)) {
			return null;
		} else {
			return new GlideUrl(url, new LazyHeaders.Builder()
					//.addHeader("Accept-Language", "zh-CN,zh;q=0.9,zh-TW;q=0.8")
					//.addHeader("Host", "i.meizitu.net")
					.addHeader("Referer", Api.APP_MEI_ZI_TU_DOMAIN)
					.addHeader("User-Agent", AgentUtil.getAgent())
					.build());
		}
	}
}
