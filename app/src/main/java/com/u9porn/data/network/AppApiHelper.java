package com.u9porn.data.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.u9porn.utils.Logger;
import com.u9porn.constants.Constants;
import com.u9porn.data.cache.CacheProviders;
import com.u9porn.data.db.entity.V9PornItem;
import com.u9porn.data.db.entity.VideoResult;
import com.u9porn.data.model.*;
import com.u9porn.data.model.axgle.Axgle;
import com.u9porn.data.model.axgle.AxgleResponse;
import com.u9porn.data.model.kedouwo.KeDouModel;
import com.u9porn.data.model.kedouwo.KeDouRelated;
import com.u9porn.data.model.pxgav.PxgavLoadMoreResponse;
import com.u9porn.data.model.pxgav.PxgavResultWithBlockId;
import com.u9porn.data.model.pxgav.PxgavVideoParserJsonResult;
import com.u9porn.data.network.apiservice.*;
import com.u9porn.data.network.okhttp.HeaderUtils;
import com.u9porn.data.network.okhttp.MyProxySelector;
import com.u9porn.exception.FavoriteException;
import com.u9porn.exception.MessageException;
import com.u9porn.parser.*;
import com.u9porn.parser.v9porn.VideoPlayUrlParser;
import com.u9porn.rxjava.RetryWhenProcess;
import com.u9porn.ui.porn9video.play.PlayVideoPresenter;
import com.u9porn.utils.AddressHelper;
import com.u9porn.utils.AgentUtil;
import com.u9porn.utils.UserHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.DynamicKeyGroup;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.EvictDynamicKeyGroup;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.Reply;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author
 * @date 2018/3/4
 */

@Singleton
public class AppApiHelper implements ApiHelper {

	private static final String TAG = AppApiHelper.class.getSimpleName();

	private final static String CHECK_UPDATE_URL = "https://raw.githubusercontent.com/techGay/v9porn/master/version.txt";
	private final static String CHECK_NEW_NOTICE_URL = "https://raw.githubusercontent.com/techGay/v9porn/master/notice.txt";
	private final static String COMMON_QUESTIONS_URL = "https://raw.githubusercontent.com/techGay/v9porn/master/COMMON_QUESTION.md";
	private CacheProviders cacheProviders;

	private V9PornServiceApi v9PornServiceApi;
	private Forum9PronServiceApi forum9PronServiceApi;
	private GitHubServiceApi gitHubServiceApi;
	private MeiZiTuServiceApi meiZiTuServiceApi;
	private DouBanServiceApi douBanServiceApi;
	private PavServiceApi pavServiceApi;
	private ProxyServiceApi proxyServiceApi;
	private AxgleServiceApi axgleServiceApi;
	private KeDouServiceApi keDouServiceApi;
	private AddressHelper addressHelper;
	private MyProxySelector myProxySelector;
	private Gson gson;
	private User user;
	private final VideoPlayUrlParser videoPlayUrlParser;

	class InJavaScriptLocalObj {
		@JavascriptInterface
		public void showSource(String html) {
			Logger.d("HTML", html);
		}
	}

	@Inject
	public AppApiHelper(CacheProviders cacheProviders, V9PornServiceApi v9PornServiceApi, Forum9PronServiceApi forum9PronServiceApi,
	                    AddressHelper addressHelper, Gson gson, GitHubServiceApi gitHubServiceApi, MeiZiTuServiceApi meiZiTuServiceApi,
	                    PavServiceApi pavServiceApi, ProxyServiceApi proxyServiceApi, AxgleServiceApi axgleServiceApi, DouBanServiceApi douBanServiceApi,
	                    KeDouServiceApi keDouServiceApi, MyProxySelector myProxySelector, User user, VideoPlayUrlParser videoPlayUrlParser) {
		this.cacheProviders = cacheProviders;
		this.v9PornServiceApi = v9PornServiceApi;
		this.forum9PronServiceApi = forum9PronServiceApi;
		this.gitHubServiceApi = gitHubServiceApi;
		this.meiZiTuServiceApi = meiZiTuServiceApi;
		this.pavServiceApi = pavServiceApi;
		this.proxyServiceApi = proxyServiceApi;
		this.axgleServiceApi = axgleServiceApi;
		this.douBanServiceApi = douBanServiceApi;
		this.keDouServiceApi = keDouServiceApi;
		this.addressHelper = addressHelper;
		this.gson = gson;
		this.myProxySelector = myProxySelector;
		this.user = user;
		this.videoPlayUrlParser = videoPlayUrlParser;
	}

	@Override
	public Observable<List<V9PornItem>> loadPorn9VideoIndex(boolean cleanCache) {
		Observable<String> indexPhpObservable = v9PornServiceApi.porn9VideoIndexPhp(HeaderUtils.getIndexHeader(addressHelper));
		return cacheProviders.getIndexPhp(indexPhpObservable, new EvictProvider(cleanCache))
		                     .map(responseBodyReply -> {
			                     switch (responseBodyReply.getSource()) {
				                     case CLOUD:
					                     Logger.t(TAG).d("数据来自：网络");
					                     break;
				                     case MEMORY:
					                     Logger.t(TAG).d("数据来自：内存");
					                     break;
				                     case PERSISTENCE:
					                     Logger.t(TAG).d("数据来自：磁盘缓存");
					                     break;
				                     default:
					                     break;
			                     }
			                     return responseBodyReply.getData();
		                     })
		                     .map(ParseV9PronVideo::parseIndex);
	}

	@Override
	public Observable<BaseResult<List<V9PornItem>>> loadPorn9VideoByCategory(String category, String viewType, int page, String m, boolean cleanCache,
	                                                                         boolean isLoadMoreCleanCache) {
		//RxCache条件区别
		String condition;
		if (TextUtils.isEmpty(m)) {
			condition = category;
		} else {
			condition = category + m;
		}
		DynamicKeyGroup dynamicKeyGroup = new DynamicKeyGroup(condition, page);
		EvictDynamicKey evictDynamicKey = new EvictDynamicKey(cleanCache || isLoadMoreCleanCache);

		Observable<String> categoryPage = v9PornServiceApi.getCategoryPage(category, viewType, page, m, HeaderUtils.getIndexHeader(addressHelper));
		return cacheProviders.getCategoryPage(categoryPage, dynamicKeyGroup, evictDynamicKey)
		                     .map(Reply::getData)
		                     .map(ParseV9PronVideo::parseByCategory);
	}

	@Override
	public Observable<BaseResult<List<V9PornItem>>> loadPorn9authorVideos(String uid, String type, int page, boolean cleanCache) {
		//RxCache条件区别
		String condition = null;
		if (!TextUtils.isEmpty(uid)) {
			condition = uid;
		}
		DynamicKeyGroup dynamicKeyGroup = new DynamicKeyGroup(condition, page);
		EvictDynamicKey evictDynamicKey = new EvictDynamicKey(cleanCache);

		Observable<String> stringObservable = v9PornServiceApi.authorVideos(uid, type, page);
		return cacheProviders.authorVideos(stringObservable, dynamicKeyGroup, evictDynamicKey)
		                     .map(Reply::getData)
		                     .map(ParseV9PronVideo::parseAuthorVideos);
	}

	@Override
	public Observable<BaseResult<List<V9PornItem>>> loadPorn9VideoRecentUpdates(String next, int page, boolean cleanCache, boolean isLoadMoreCleanCache) {

		DynamicKeyGroup dynamicKeyGroup = new DynamicKeyGroup(next, page);
		EvictDynamicKey evictDynamicKey = new EvictDynamicKey(cleanCache || isLoadMoreCleanCache);

		Observable<String> categoryPage = v9PornServiceApi.recentUpdates(next, page, HeaderUtils.getIndexHeader(addressHelper));
		return cacheProviders.getRecentUpdates(categoryPage, dynamicKeyGroup, evictDynamicKey)
		                     .map(Reply::getData)
		                     .map(ParseV9PronVideo::parseByCategory);
	}

	@Override
	public Observable<VideoResult> loadPorn9VideoUrl(String viewKey) {
		//因为登录后不在返回用户uid，需要在此页面获取，所以当前页面不在缓存，确保用户登录后刷新当前页面可以获取到用户uid
		return v9PornServiceApi.getVideoPlayPage(viewKey, AgentUtil.getAgent(), HeaderUtils.getIndexHeader(addressHelper))
		                       .map(html -> videoPlayUrlParser.parseVideoPlayUrl(html, user));
//        return Observable.create(new ObservableOnSubscribe(){
//            @Override
//            public void subscribe(ObservableEmitter emitter) throws Exception {
//                Map<String, String > headers = new HashMap<String, String>();
//                headers.put("X-Forwarded-For",addressHelper.getRandomIPAddress());
//                headers.put("Referer", HeaderUtils.getIndexHeader(addressHelper));
//                MyApplication.getInstance().getWebView().addJavascriptInterface(new InJavaScriptLocalObj(){
//                    @JavascriptInterface
//                    @Override
//                    public void showSource(String html) {
//                        Logger.t(TAG).d("Webview 取回来的html source: "+html);
//                        emitter.onNext(html);
//                    }
//                },"local_obj");
//                MyApplication.getInstance().getWebView().loadUrl(MyApplication.getInstance().getAddressHelper().getVideo9PornAddress()+"view_video
//                .php"+"?viewkey="+viewKey);
//            }
//        }).map(new Function<String,VideoResult>() {
//            @Override
//            public VideoResult apply(String html) throws Exception {
//                return videoPlayUrlParser.parseVideoPlayUrl(html, user);
//            }
//        });
	}

	@Override
	public Observable<List<VideoComment>> loadPorn9VideoComments(String videoId, int page, String viewKey) {
		return v9PornServiceApi.getVideoComments(videoId, page, Constants.PORN9_VIDEO_COMMENT_PER_PAGE_NUM, HeaderUtils.getPlayVideoReferer(viewKey,
		                                                                                                                                    addressHelper))
		                       .map(ParseV9PronVideo::parseVideoComment);
	}

	@Override
	public Observable<String> commentPorn9Video(String cpaintFunction, String comment, String uid, String vid, String viewKey, String responseType) {
		return v9PornServiceApi.commentVideo(cpaintFunction, comment, uid, vid, responseType, HeaderUtils.getPlayVideoReferer(viewKey, addressHelper))
		                       .map(s -> new Gson().fromJson(s, VideoCommentResult.class))
		                       .map(videoCommentResult -> {
			                       String msg = "评论错误，未知错误";
			                       if (videoCommentResult.getA().size() == 0) {
				                       throw new MessageException("评论错误，未知错误");
			                       } else if (videoCommentResult.getA().get(0).getData() == VideoCommentResult.COMMENT_SUCCESS) {
				                       msg = "留言已经提交，审核后通过";
			                       } else if (videoCommentResult.getA().get(0).getData() == VideoCommentResult.COMMENT_ALLREADY) {
				                       throw new MessageException("你已经在这个视频下留言过");
			                       } else if (videoCommentResult.getA().get(0).getData() == VideoCommentResult.COMMENT_NO_PERMISION) {
				                       throw new MessageException("不允许留言!");
			                       }
			                       return msg;
		                       });
	}

	@Override
	public Observable<String> replyPorn9VideoComment(String comment, String username, String vid, String commentId, String viewKey) {
		return v9PornServiceApi.replyVideoComment(comment, username, vid, commentId, HeaderUtils.getPlayVideoReferer(viewKey, addressHelper));
	}

	@Override
	public Observable<BaseResult<List<V9PornItem>>> searchPorn9Videos(String viewType, int page, String searchType, String searchId, String sort) {
		return v9PornServiceApi.searchVideo(viewType, page, searchType, searchId, sort, HeaderUtils.getIndexHeader(addressHelper),
		                                    addressHelper.getRandomIPAddress())
		                       .map(ParseV9PronVideo::parseSearchVideos);
	}

	@Override
	public Observable<String> favoritePorn9Video(String uId, String videoId, String uvid) {
		String cpaintFunction = "addToFavorites";
		String responseType = "json";
//        return v9PornServiceApi.favoriteVideo(cpaintFunction, uId, videoId, uvid, responseType, HeaderUtils.getIndexHeader(addressHelper))
//                .map(s -> {
//                    Logger.t(TAG).d("favoriteStr: " + s);
//                    return new Gson().fromJson(s, FavoriteJsonResult.class);
//                })
//                .map(favoriteJsonResult -> favoriteJsonResult.getAddFavMessage().get(0).getData())
//                .map(code -> {
//                    String msg;
//                    switch (code) {
//                        case FavoriteJsonResult.FAVORITE_SUCCESS:
//                            msg = "收藏成功";
//                            break;
//                        case FavoriteJsonResult.FAVORITE_FAIL:
//                            throw new FavoriteException("收藏失败");
//                        case FavoriteJsonResult.FAVORITE_YOURSELF:
//                            throw new FavoriteException("不能收藏自己的视频");
//                        default:
//                            throw new FavoriteException("收藏失败");
//                    }
//                    return msg;
//                });
		return v9PornServiceApi.favoriteVideo(videoId, uId, uvid, HeaderUtils.getIndexHeader(addressHelper)).map(code -> {
			String msg;
			switch (Integer.parseInt(code)) {
				case FavoriteJsonResult.FAVORITE_SUCCESS:
					msg = "收藏成功";
					break;
				case FavoriteJsonResult.FAVORITE_FAIL:
					throw new FavoriteException("收藏失败");
				case FavoriteJsonResult.FAVORITE_YOURSELF:
					throw new FavoriteException("不能收藏自己的视频");
				default:
					throw new FavoriteException("收藏失败");
			}
			return msg;
		});
	}

	@Override
	public Observable<BaseResult<List<V9PornItem>>> loadPorn9MyFavoriteVideos(String userName, int page, boolean cleanCache) {
		Observable<String> favoriteObservable = v9PornServiceApi.myFavoriteVideo(page, HeaderUtils.getIndexHeader(addressHelper));
		DynamicKeyGroup dynamicKeyGroup = new DynamicKeyGroup(userName, page);
		EvictDynamicKey evictDynamicKey = new EvictDynamicKey(cleanCache);
		return cacheProviders.getFavorite(favoriteObservable, dynamicKeyGroup, evictDynamicKey)
		                     .map(Reply::getData)
		                     .map(ParseV9PronVideo::parseMyFavorite);
	}

	@Override
	public Observable<List<V9PornItem>> deletePorn9MyFavoriteVideo(String rvid) {
		String removeFavour = "Remove FavoriteJsonResult";
		return v9PornServiceApi.deleteMyFavoriteVideo(rvid, removeFavour, 45, 19, HeaderUtils.getFavHeader(addressHelper))
		                       .map(ParseV9PronVideo::parseMyFavorite)
		                       .map(baseResult -> {
			                       if (baseResult.getCode() == BaseResult.ERROR_CODE) {
				                       throw new FavoriteException(baseResult.getMessage());
			                       }
			                       if (baseResult.getCode() != BaseResult.SUCCESS_CODE || TextUtils.isEmpty(baseResult.getMessage())) {
				                       throw new FavoriteException("删除失败了");
			                       }
			                       return baseResult.getData();
		                       });
	}

	@Override
	public Observable<Bitmap> porn9VideoLoginCaptcha() {
		return v9PornServiceApi.captcha().map(responseBody -> BitmapFactory.decodeStream(responseBody.byteStream()));
	}

	@Override
	public Observable<User> userLoginPorn9Video(String username, String password, String captcha) {

		String fingerprint = UserHelper.randomFingerprint();
		String fingerprint2 = UserHelper.randomFingerprint2();
		String actionLogin = "Log In";
		String x = "47";
		String y = "12";
		return v9PornServiceApi.login(username, password, fingerprint, fingerprint2, captcha, actionLogin, x, y, HeaderUtils.getUserHeader(addressHelper,
		                                                                                                                                   "login"))
		                       .retryWhen(new RetryWhenProcess(2))
		                       .map(s -> {
			                       if (!UserHelper.isPornVideoLoginSuccess(s)) {
				                       String errorInfo = ParseV9PronVideo.parseErrorInfo(s);
				                       if (TextUtils.isEmpty(errorInfo)) {
					                       errorInfo = "未知错误，请确认地址是否正确";
				                       }
				                       throw new MessageException(errorInfo);
			                       }
			                       return ParseV9PronVideo.parseUserInfo(s);
		                       });
	}

	@Override
	public Observable<User> userRegisterPorn9Video(String username, String password1, String password2, String email, String captchaInput) {
		String next = "";
//        String fingerprint = "2192328486";
		String fingerprint = UserHelper.randomFingerprint();
		String vip = "";
		String actionSignUp = "Sign Up";
		String submitX = "45";
		String submitY = "13";
		String ipAddress = addressHelper.getRandomIPAddress();
		return v9PornServiceApi.register(next, username, password1, password2, email, captchaInput, fingerprint, vip, actionSignUp, submitX, submitY,
		                                 HeaderUtils.getUserHeader(addressHelper, "signup"), ipAddress)
		                       .retryWhen(new RetryWhenProcess(2))
		                       .map(s -> {
			                       if (!UserHelper.isPornVideoLoginSuccess(s)) {
				                       String errorInfo = ParseV9PronVideo.parseErrorInfo(s);
				                       throw new MessageException(errorInfo);
			                       }
			                       return ParseV9PronVideo.parseUserInfo(s);
		                       });
	}

	@Override
	public Observable<List<PinnedHeaderEntity<F9PronItem>>> loadPorn9ForumIndex() {
		return forum9PronServiceApi.porn9ForumIndex()
		                           .map(s -> {
			                           BaseResult<List<PinnedHeaderEntity<F9PronItem>>> baseResult = ParseForum9Porn.parseIndex(s);
			                           return baseResult.getData();
		                           });
	}

	@Override
	public Observable<BaseResult<List<F9PronItem>>> loadPorn9ForumListData(String fid, final int page) {
		return forum9PronServiceApi.forumdisplay(fid, page).map(s -> ParseForum9Porn.parseForumList(s, page));
	}

	@Override
	public Observable<F9PornContent> loadPorn9ForumContent(Long tid, final boolean isNightModel) {
		return forum9PronServiceApi.forumItemContent(tid).map(s -> ParseForum9Porn.parseContent(s, isNightModel, addressHelper.getForum9PornAddress()).getData());
	}

	@Override
	public Observable<UpdateVersion> checkUpdate() {
		return gitHubServiceApi.checkUpdate(CHECK_UPDATE_URL).map(s -> gson.fromJson(s, UpdateVersion.class));
	}

	@Override
	public Observable<Notice> checkNewNotice() {
		return gitHubServiceApi.checkNewNotice(CHECK_NEW_NOTICE_URL).map(s -> gson.fromJson(s, Notice.class));
	}

	@Override
	public Observable<String> commonQuestions() {
		return gitHubServiceApi.commonQuestions(COMMON_QUESTIONS_URL);
	}

	@Override
	public Observable<BaseResult<List<MeiZiTu>>> listMeiZiTu(String tag, int page, boolean pullToRefresh) {
		switch (tag) {
			case "index":
				return action(meiZiTuServiceApi.meiZiTuIndex(page), tag, page, pullToRefresh);
			case "youwu":
				return action(meiZiTuServiceApi.meiZiTuHot(page), tag, page, pullToRefresh);
			case "meitui":
				return action(meiZiTuServiceApi.meiZiTuBest(page), tag, page, pullToRefresh);
			case "xinggan":
				return action(meiZiTuServiceApi.meiZiTuSexy(page), tag, page, pullToRefresh);
			case "meitun":
				return action(meiZiTuServiceApi.meiZiTuJapan(page), tag, page, pullToRefresh);
			case "zhifu":
				return action(meiZiTuServiceApi.meiZiTuJaiwan(page), tag, page, pullToRefresh);
			case "sm":
				return action(meiZiTuServiceApi.meiZiTuMm(page), tag, page, pullToRefresh);
			default:
				return null;
		}
	}

	@Override
	public Observable<List<DouBanMeizi>> listDouBanMeiZhi(final int cid, int page, boolean pullToRefresh) {
		DynamicKeyGroup dynamicKeyGroup = new DynamicKeyGroup(cid, page);
		EvictDynamicKeyGroup evictDynamicKeyGroup = new EvictDynamicKeyGroup(pullToRefresh);
		Observable<String> stringObservable = douBanServiceApi.listDouBanMeiZhi(cid, page);
		return cacheProviders.douBanMeiZi(stringObservable, dynamicKeyGroup, evictDynamicKeyGroup)
		                     .map(stringReply -> {
			                     switch (stringReply.getSource()) {
				                     case CLOUD:
					                     Logger.t(TAG).d("数据来自：网络");
					                     break;
				                     case MEMORY:
					                     Logger.t(TAG).d("数据来自：内存");
					                     break;
				                     case PERSISTENCE:
					                     Logger.t(TAG).d("数据来自：磁盘缓存");
					                     break;
				                     default:
					                     break;
			                     }
			                     return stringReply.getData();
		                     })
		                     .map(html -> ParseDouBanMeiZi.JsoupDoubanMeizi(html, cid));
	}

	@Override
	public Observable<List<String>> meiZiTuImageList(int id, boolean pullToRefresh) {
		return cacheProviders.meiZiTu(meiZiTuServiceApi.meiZiTuImageList(id), new DynamicKey(id), new EvictDynamicKey(pullToRefresh))
		                     .map(Reply::getData)
		                     .map(s -> {
			                     BaseResult<List<String>> baseResult = ParseMeiZiTu.parsePicturePage(s);
			                     return baseResult.getData();
		                     });
	}

	@Override
	public Observable<PxgavResultWithBlockId> loadPxgavListByCategory(String category, boolean pullToRefresh) {
		DynamicKey dynamicKey = new DynamicKey(category);
		EvictDynamicKey evictDynamicKey = new EvictDynamicKey(pullToRefresh);
		if ("index".equals(category)) {
			return action(cacheProviders.cacheWithLimitTime(pavServiceApi.pigAvVideoList(addressHelper.getPavAddress()), dynamicKey, evictDynamicKey));
		} else {
			return action(cacheProviders.cacheWithLimitTime(pavServiceApi.pigAvVideoList(addressHelper.getPavAddress() + category + "av線上看"), dynamicKey,
			                                                evictDynamicKey));
		}
	}

	@Override
	public Observable<PxgavResultWithBlockId> loadMorePxgavListByCategory(String category, int page, String lastBlockId, boolean pullToRefresh) {
		Map<String, String> map = new HashMap<>();
		map.put("action", "penci_ajax_block");
		map.put("datafilter[build_query]", "post_type:post|size:23|order_by:rand");
		map.put("datafilter[add_title_icon]", "");
		map.put("datafilter[title_i_align]", "left");
		map.put("datafilter[title_icon]", "");
		map.put("datafilter[image_type]", "landscape");
		map.put("datafilter[block_title_meta_settings]", "");
		map.put("datafilter[block_title_align]", "style-title-left");
		map.put("datafilter[block_title_off_uppercase]", "");
		map.put("datafilter[block_title_wborder_left_right]", "5px");
		map.put("datafilter[block_title_wborder]", "3px");
		map.put("datafilter[post_title_trimword_settings]", "");
		map.put("datafilter[post_standard_title_length]", "15");
		map.put("datafilter[hide_comment]", "true");
		map.put("datafilter[hide_post_date]", "true");
		map.put("datafilter[hide_icon_post_format]", "true");
		map.put("datafilter[hide_cat]", "true");
		map.put("datafilter[show_allcat]", "");
		map.put("datafilter[hide_count_view]", "true");
		map.put("datafilter[hide_review_piechart]", "true");
		map.put("datafilter[show_readmore]", "");
		map.put("datafilter[show_author]", "");
		map.put("datafilter[dis_bg_block]", "true");
		map.put("datafilter[enable_stiky_post]", "");
		map.put("datafilter[hide_excrept]", "true");
		map.put("datafilter[post_excrept_length]", "15");
		map.put("datafilter[style_pag]", "load_more");
		map.put("datafilter[limit_loadmore]", "8");
		map.put("datafilter[readmore_css]", "");
		map.put("datafilter[post_category_css]", "");
		map.put("datafilter[pagination_css]", "");
		map.put("datafilter[loadmore_css]", "");
		map.put("datafilter[disable_bg_load_more]", "");
		map.put("datafilter[custom_markup_1]", "");
		map.put("datafilter[ajax_filter_type]", "");
		map.put("datafilter[ajax_filter_selected]", "");
		map.put("datafilter[ajax_filter_childselected]", "");
		map.put("datafilter[ajax_filter_number_item]", "5");
		map.put("datafilter[infeed_ads__order]", "22");
		map.put("datafilter[block_id]", "penci_block_14-1551151497775");
		map.put("datafilter[penci_show_desk]", "1");
		map.put("datafilter[penci_show_tablet]", "1");
		map.put("datafilter[penci_show_mobile]", "1");
		map.put("datafilter[paged]", "1");
		map.put("datafilter[unique_id]", "penci_block_14__83050151");
		map.put("datafilter[shortcode_id]", "block_14");
		map.put("datafilter[category_ids]", "");
		map.put("datafilter[taxonomy]", "");
		map.put("styleAction", "load_more");
		map.put("paged", "" + page);
		map.put("datacontent",
		        "JTNDY2VudGVyJTNFJTNDc2NyaXB0JTIwdHlwZSUzRCUyMnRleHQlMkZqYXZhc2NyaXB0JTIyJTIwZGF0YS1pZHpvbmUlM0QlMjIzMzM3NDA4JTIyJTIwc3JjJTNEJTIyaHR0cHMlM0ElMkYlMkZhZHMuZXhvc3J2LmNvbSUyRm5hdGl2ZWFkcy5qcyUyMiUzRSUzQyUyRnNjcmlwdCUzRSUzQyUyRmNlbnRlciUzRQ==");
		map.put("nonce", "7f02fb57e5");
		return actionMore(pavServiceApi.moreVideoList(map), pullToRefresh);
	}

	@Override
	public Observable<PxgavVideoParserJsonResult> loadPxgavVideoUrl(String url, String pId, boolean pullToRefresh) {
		if (TextUtils.isEmpty(pId)) {
			pId = "aaa1";
			pullToRefresh = true;
		}
		DynamicKey dynamicKey = new DynamicKey(pId);
		return cacheProviders.cacheWithNoLimitTime(pavServiceApi.pigAvVideoUrl(url), dynamicKey, new EvictDynamicKey(pullToRefresh))
		                     .map(Reply::getData)
		                     .map(s -> ParsePxgav.parserVideoUrl(s).getData());
	}

	@Override
	public Observable<BaseResult<List<ProxyModel>>> loadXiCiDaiLiProxyData(final int page) {
		return proxyServiceApi.proxyXiciDaili(page)
		                      .map(s -> ParseProxy.parseXiCiDaiLi(s, page));
	}

	@Override
	public Observable<Boolean> testProxy(String proxyIpAddress, int proxyPort) {
		myProxySelector.setTest(true, proxyIpAddress, proxyPort);
		return v9PornServiceApi.porn9VideoIndexPhp(HeaderUtils.getIndexHeader(addressHelper))
		                       .map(s -> {
			                       List<V9PornItem> list = ParseV9PronVideo.parseIndex(s);
			                       return list.size() != 0;
		                       });
	}

	@Override
	public void existProxyTest() {
		myProxySelector.setTest(false, null, 0);
	}

	@Override
	public Observable<Boolean> testPorn9VideoAddress() {
		return v9PornServiceApi.porn9VideoIndexPhp(HeaderUtils.getIndexHeader(addressHelper))
		                       .map(s -> {
			                       List<V9PornItem> list = ParseV9PronVideo.parseIndex(s);
			                       return list.size() != 0;
		                       });
	}

	@Override
	public Observable<Boolean> testPorn9ForumAddress() {
		return forum9PronServiceApi.porn9ForumIndex()
		                           .map(s -> {
			                           BaseResult<List<PinnedHeaderEntity<F9PronItem>>> baseResult = ParseForum9Porn.parseIndex(s);
			                           return baseResult.getData().size() != 0;
		                           });
	}

	@Override
	public Observable<Boolean> testPavAddress(String url) {
		return pavServiceApi.pigAvVideoList(addressHelper.getPavAddress())
		                    .map(s -> {
			                    BaseResult<PxgavResultWithBlockId> baseResult = ParsePxgav.videoList(s, false);
			                    return baseResult.getData().getPxgavModelList().size() != 0;
		                    });
	}

	@Override
	public Observable<Boolean> testAxgle() {
		int page = 1;
		String o = "mr";
		String t = "a";
		String type = "public";
		return axgleServiceApi.videos(page, o, t, type, "1", 10).map(s -> {
			if (TextUtils.isEmpty(s)) {
				return false;
			}
			Axgle axgle = gson.fromJson(s, Axgle.class);
			return axgle != null && axgle.isSuccess();
		});
	}

	/**
	 * Axgle首页列表
	 */
	@Override
	public Observable<AxgleResponse> axgleVideos(int page, String o, String t, String type, String c, int limit) {
		return axgleServiceApi.videos(page, o, t, type, c, limit).map(s -> {
			Axgle axgle = gson.fromJson(s, Axgle.class);
			return axgle.getResponse();
		});
	}

	@Override
	public Observable<AxgleResponse> searchAxgleVideo(String keyWord, int page) {
		return axgleServiceApi.search(keyWord, page).map(s -> {
			Axgle axgle = gson.fromJson(s, Axgle.class);
			return axgle.getResponse();
		});
	}

	@Override
	public Observable<AxgleResponse> searchAxgleJavVideo(String keyWord, int page) {
		return axgleServiceApi.searchJav(keyWord, page).map(s -> {
			Axgle axgle = gson.fromJson(s, Axgle.class);
			return axgle.getResponse();
		});
	}

	@Override
	public Call<ResponseBody> getPlayVideoUrl(String url) {
		return axgleServiceApi.getPlayVideoUrl(url);
	}

	@Override
	public Observable<List<KeDouModel>> videoList(String category, int page, boolean pullToRefresh) {
//        DynamicKeyGroup dynamicKeyGroup = new DynamicKeyGroup(category, page);
//        EvictDynamicKeyGroup evictDynamicKeyGroup = new EvictDynamicKeyGroup(pullToRefresh);
		return keDouServiceApi.videoList(category, page).map(ParseKeDouWo::parseVideoList);
	}

	@Override
	public Observable<List<KeDouModel>> videoListLatest(int page) {
		return keDouServiceApi.videoListLatest(page).map(ParseKeDouWo::parseVideoList);
	}

	@Override
	public Observable<List<KeDouModel>> videoListTop(int page) {
		return keDouServiceApi.videoListTop(page).map(ParseKeDouWo::parseVideoList);
	}

	@Override
	public Observable<List<KeDouModel>> videoListPopular(int page) {
		return keDouServiceApi.videoListPopular(page).map(ParseKeDouWo::parseVideoList);
	}

	@Override
	public Observable<KeDouRelated> videoRelated(String url) {
		String ip = addressHelper.getRandomIPAddress();
		return keDouServiceApi.videoRelated(url, ip).map(ParseKeDouWo::parseVideoDetail);
	}

	@Override
	public Observable<String> getRealVideoUrl(String url) {
		return Observable.just(url).map(ParseKeDouWo::getRedirectUrl);
//        return keDouServiceApi.getRealVideoUrl(url).map(ParseKeDouWo::parseRealVideoUrl);
	}

	@Override
	public Observable<Response<ResponseBody>> testV9Porn(String url) {
		return v9PornServiceApi.testV9Porn(url);
	}

	@Override
	public Observable<Response<ResponseBody>> verifyGoogleRecaptcha(String action, String r, String id, String recaptcha) {
		return v9PornServiceApi.verifyGoogleRecaptcha(action, r, id, recaptcha);
	}

	private Observable<PxgavResultWithBlockId> actionMore(Observable<String> observable, final boolean pullToRefresh) {
		return observable
				.map(s -> {
					Logger.t(TAG).d("p*gav 更多原始数据：" + s);
					PxgavLoadMoreResponse pxgavLoadMoreResponse = gson.fromJson(s, PxgavLoadMoreResponse.class);
					BaseResult<PxgavResultWithBlockId> baseResult = ParsePxgav.moreVideoList(pxgavLoadMoreResponse.getData().getItems());
					//baseResult.getData().setBlockId(pxgavLoadMoreResponse.getTd_block_id());
					return baseResult.getData();
				});
	}

	private Observable<PxgavResultWithBlockId> action(Observable<Reply<String>> observable) {
		return observable.map(Reply::getData)
		                 .map(s -> {
			                 BaseResult<PxgavResultWithBlockId> baseResult = ParsePxgav.videoList(s, false);
			                 return baseResult.getData();
		                 });
	}

	private Observable<BaseResult<List<MeiZiTu>>> action(Observable<String> stringObservable, String tag, final int page, final boolean pullToRefresh) {
		DynamicKeyGroup dynamicKeyGroup = new DynamicKeyGroup(tag, page);
		EvictDynamicKeyGroup evictDynamicKeyGroup = new EvictDynamicKeyGroup(pullToRefresh);
		return cacheProviders.meiZiTu(stringObservable, dynamicKeyGroup, evictDynamicKeyGroup).map(Reply::getData)
		                     .map(s -> ParseMeiZiTu.parseMeiZiTuList(s, page));
	}
}
