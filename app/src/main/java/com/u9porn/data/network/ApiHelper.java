package com.u9porn.data.network;

import android.graphics.Bitmap;

import com.u9porn.data.db.entity.V9PornItem;
import com.u9porn.data.db.entity.VideoResult;
import com.u9porn.data.model.BaseResult;
import com.u9porn.data.model.DouBanMeizi;
import com.u9porn.data.model.F9PornContent;
import com.u9porn.data.model.F9PronItem;
import com.u9porn.data.model.MeiZiTu;
import com.u9porn.data.model.Notice;
import com.u9porn.data.model.PinnedHeaderEntity;
import com.u9porn.data.model.ProxyModel;
import com.u9porn.data.model.UpdateVersion;
import com.u9porn.data.model.User;
import com.u9porn.data.model.VideoComment;
import com.u9porn.data.model.axgle.AxgleResponse;
import com.u9porn.data.model.kedouwo.KeDouModel;
import com.u9porn.data.model.kedouwo.KeDouRelated;
import com.u9porn.data.model.pxgav.PxgavResultWithBlockId;
import com.u9porn.data.model.pxgav.PxgavVideoParserJsonResult;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author flymegoc
 * @date 2018/3/4
 */

public interface ApiHelper {
    Observable<List<V9PornItem>> loadPorn9VideoIndex(boolean cleanCache);

    Observable<BaseResult<List<V9PornItem>>> loadPorn9VideoByCategory(String category, String viewType, int page, String m, boolean cleanCache, boolean isLoadMoreCleanCache);

    Observable<BaseResult<List<V9PornItem>>> loadPorn9authorVideos(String uid, String type, int page, boolean cleanCache);

    Observable<BaseResult<List<V9PornItem>>> loadPorn9VideoRecentUpdates(String next, int page, boolean cleanCache, boolean isLoadMoreCleanCache);

    Observable<VideoResult> loadPorn9VideoUrl(String viewKey);

    Observable<List<VideoComment>> loadPorn9VideoComments(String videoId, int page, String viewKey);

    Observable<String> commentPorn9Video(String cpaintFunction, String comment, String uid, String vid, String viewKey, String responseType);

    Observable<String> replyPorn9VideoComment(String comment, String username, String vid, String commentId, String viewKey);

    Observable<BaseResult<List<V9PornItem>>> searchPorn9Videos(String viewType, int page, String searchType, String searchId, String sort);

    Observable<String> favoritePorn9Video(String uId, String videoId, String ownnerId);

    Observable<BaseResult<List<V9PornItem>>> loadPorn9MyFavoriteVideos(String userName, int page, boolean cleanCache);

    Observable<List<V9PornItem>> deletePorn9MyFavoriteVideo(String rvid);

    Observable<Bitmap> porn9VideoLoginCaptcha();

    Observable<User> userLoginPorn9Video(String username, String password, String captcha);

    Observable<User> userRegisterPorn9Video(String username, String password1, String password2, String email, String captchaInput);

    Observable<List<PinnedHeaderEntity<F9PronItem>>> loadPorn9ForumIndex();

    Observable<BaseResult<List<F9PronItem>>> loadPorn9ForumListData(String fid, int page);

    Observable<F9PornContent> loadPorn9ForumContent(Long tid, final boolean isNightModel);

    Observable<UpdateVersion> checkUpdate();

    Observable<Notice> checkNewNotice();

    Observable<String> commonQuestions();

    Observable<BaseResult<List<MeiZiTu>>> listMeiZiTu(String tag, int page, boolean pullToRefresh);

    Observable<List<DouBanMeizi>> listDouBanMeiZhi(int cid, int page, boolean pullToRefresh);

    Observable<List<String>> meiZiTuImageList(int id, boolean pullToRefresh);

    Observable<PxgavResultWithBlockId> loadPxgavListByCategory(String category, boolean pullToRefresh);

    Observable<PxgavResultWithBlockId> loadMorePxgavListByCategory(String category, int page, String lastBlockId, boolean pullToRefresh);

    Observable<PxgavVideoParserJsonResult> loadPxgavVideoUrl(String url, String pId, boolean pullToRefresh);

    Observable<BaseResult<List<ProxyModel>>> loadXiCiDaiLiProxyData(int page);

    Observable<Boolean> testProxy(String proxyIpAddress, int proxyPort);

    void existProxyTest();

    Observable<Boolean> testPorn9VideoAddress();

    Observable<Boolean> testPorn9ForumAddress();

    Observable<Boolean> testPavAddress(String url);

    Observable<Boolean> testAxgle();

    /**
     * 各分类视频
     *
     * @param page  页码
     * @param o     排序 default mr
     *              bw (Last viewed) 上次查看
     *              mr (Latest) 最新
     *              mv (Most viewed) 观看最多
     *              tr (Top rated) 最受好评
     *              tf (Most favoured) 最受青睐
     *              lg (Longest) 最长时间
     * @param t     时间 default a
     *              t (1 day)
     *              w (1 week)
     *              m (1 month)
     *              a (Forever)
     * @param type  类别 public(公开)  private(私有)
     * @param c     CHID of a valid video category (integer) 有效视频类别的 CHID
     * @param limit 每页数 [1, 250]
     */
    Observable<AxgleResponse> axgleVideos(int page, String o, String t, String type, String c, int limit);

    Observable<AxgleResponse> searchAxgleVideo(String keyWord, int page);

    Observable<AxgleResponse> searchAxgleJavVideo(String keyWord, int page);

    Call<ResponseBody> getPlayVideoUrl(String url);

    Observable<List<KeDouModel>> videoList(String category,int page,boolean pullToRefresh);

    Observable<List<KeDouModel>> videoListLatest(int page);

    Observable<List<KeDouModel>> videoListTop(int page);

    Observable<List<KeDouModel>> videoListPopular(int page);

    Observable<KeDouRelated> videoRelated(String url);

    Observable<String> getRealVideoUrl(String url);

    Observable<Response<ResponseBody>> testV9Porn(String url);

    Observable<Response<ResponseBody>> verifyGoogleRecaptcha(String action, String r, String id, String recaptcha);
}
