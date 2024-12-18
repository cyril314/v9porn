package com.u9porn.data.model.pxgav;

import java.io.Serializable;

/**
 * pigAv实体
 */
public class PxgavModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String title;
	private String contentUrl;
	private String imgUrl;
	private String pId;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
}
