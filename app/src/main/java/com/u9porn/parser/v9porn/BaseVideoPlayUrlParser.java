package com.u9porn.parser.v9porn;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.u9porn.data.DataManager;
import com.u9porn.data.db.entity.VideoResult;
import com.u9porn.data.model.UpdateVersion;
import com.u9porn.data.model.User;

import org.jsoup.nodes.Document;

public abstract class BaseVideoPlayUrlParser {

    protected static final String TAG = BaseVideoPlayUrlParser.class.getSimpleName();

    protected void parserOtherInfo(Document doc, VideoResult videoResult, User user) {

        if (doc == null || videoResult == null || user == null) {
            return;
        }
        //这里解析的作者id已经变了，非纯数字了
        String ownerUrl = doc.select("a[href*=UID]").first().attr("href");
        String ownerId = ownerUrl.substring(ownerUrl.indexOf("=") + 1, ownerUrl.length());
        videoResult.setOwnerId(ownerId);

        //暂时不处理收藏
//        String addToFavLink = doc.getElementById("addToFavLink").selectFirst("a").attr("onClick");
//        String args[] = addToFavLink.split(",");
//        String userId = args[1].trim();
//        Logger.t(TAG).d("userId:::" + userId);
//        user.setUserId(Integer.parseInt(userId));

        //原始纯数字作者id，用于收藏接口
        //目前不再是纯数字id？
        String authorId=doc.select("a[href*=UID]").attr("href").substring(doc.select("a[href*=UID]").attr("href").indexOf("UID")+4);
        videoResult.setAuthorId(authorId);
        // 作者
        String ownerName = doc.select("a[href*=UID]").first().text();
        videoResult.setOwnerName(ownerName);
        //添加时间
        String addDate=doc.select("div[id=videodetails-content]").get(1).select("div").get(2).select("span[class=title-yakov]").text();
        videoResult.setAddDate(addDate);

        Logger.t(TAG).d("作者Id：%s, authorId: %s ,%n作者：%s ,添加时间：%s", ownerId, authorId, ownerName, addDate);

        try {
            String otherInfo = doc.select("div[id=videodetails-content]").get(1).select("div").get(3).select("span[class=more title]").text();
            videoResult.setUserOtherInfo(otherInfo);
            Logger.t(TAG).d(otherInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String thumImg = doc.getElementById("player_one").attr("poster");
            videoResult.setThumbImgUrl(thumImg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String videoName = doc.select("head").select("title").text().replace("Chinese homemade video","");
        videoResult.setVideoName(videoName);
        Logger.t(TAG).d("%n视频标题：%s,%n缩略图：%s", videoResult.getVideoName(), videoResult.getThumbImgUrl());

        String uvId=doc.getElementById("VUID").html();
        videoResult.setUvId(uvId);
        //如果uid不为空那么在此播放画面中获取uid数据
        String uId=doc.getElementById("UID").html();
        if(!TextUtils.isEmpty(uId)){
            videoResult.setUid(Integer.parseInt(uId));
            user.setUserId(Integer.parseInt(uId));
        }
    }
}
