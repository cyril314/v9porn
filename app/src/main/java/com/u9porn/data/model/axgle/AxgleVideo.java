package com.u9porn.data.model.axgle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author megoc
 */
public class AxgleVideo implements Serializable{
    /**
     *
     */
    private String title;
    private String keyword; //演员名称
    private String channel;
    private double duration;
    private double framerate;
    private boolean hd; // 是否高清
    private int addtime; // 创建时间
    private int viewnumber;
    private int likes;
    private int dislikes;
    private String video_url;   // 视频地址
    private String embedded_url; // 嵌入式视频地址
    private String preview_url; // 预览图片地址
    private String preview_video_url; // 预览视频地址
    @SerializedName("private")
    private boolean privateX;
    private String vid; // 视频ID
    private String uid; // 用户ID

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getFramerate() {
        return framerate;
    }

    public void setFramerate(double framerate) {
        this.framerate = framerate;
    }

    public boolean isHd() {
        return hd;
    }

    public void setHd(boolean hd) {
        this.hd = hd;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }

    public int getViewnumber() {
        return viewnumber;
    }

    public void setViewnumber(int viewnumber) {
        this.viewnumber = viewnumber;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getEmbedded_url() {
        return embedded_url;
    }

    public void setEmbedded_url(String embedded_url) {
        this.embedded_url = embedded_url;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    public String getPreview_video_url() {
        return preview_video_url;
    }

    public void setPreview_video_url(String preview_video_url) {
        this.preview_video_url = preview_video_url;
    }

    public boolean isPrivateX() {
        return privateX;
    }

    public void setPrivateX(boolean privateX) {
        this.privateX = privateX;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
