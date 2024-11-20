package com.u9porn.data.network.apiservice;

import com.u9porn.data.network.Api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * 妹子图服务接口
 */
public interface MeiZiTuServiceApi {

	@Headers({"Referer: " + Api.APP_MEI_ZI_TU_DOMAIN, "Domain-Name: " + Api.MEI_ZI_TU_DOMAIN_NAME})
	@GET("photo/page/{page}/")
	Observable<String> meiZiTuIndex(@Path("page") int page);

	@Headers({"Referer: " + Api.APP_MEI_ZI_TU_DOMAIN, "Domain-Name: " + Api.MEI_ZI_TU_DOMAIN_NAME})
	@GET("photo/tag/youwu/page/{page}/")
	Observable<String> meiZiTuHot(@Path("page") int page);

	@Headers({"Referer: " + Api.APP_MEI_ZI_TU_DOMAIN, "Domain-Name: " + Api.MEI_ZI_TU_DOMAIN_NAME})
	@GET("photo/tag/meitui/page/{page}/")
	Observable<String> meiZiTuBest(@Path("page") int page);

	@Headers({"Referer: " + Api.APP_MEI_ZI_TU_DOMAIN, "Domain-Name: " + Api.MEI_ZI_TU_DOMAIN_NAME})
	@GET("photo/tag/xinggan/page/{page}/")
	Observable<String> meiZiTuSexy(@Path("page") int page);

	@Headers({"Referer: " + Api.APP_MEI_ZI_TU_DOMAIN, "Domain-Name: " + Api.MEI_ZI_TU_DOMAIN_NAME})
	@GET("photo/tag/meitun/page/{page}/")
	Observable<String> meiZiTuJapan(@Path("page") int page);

	@Headers({"Referer: " + Api.APP_MEI_ZI_TU_DOMAIN, "Domain-Name: " + Api.MEI_ZI_TU_DOMAIN_NAME})
	@GET("photo/tag/zhifu/page/{page}/")
	Observable<String> meiZiTuJaiwan(@Path("page") int page);

	@Headers({"Referer: " + Api.APP_MEI_ZI_TU_DOMAIN, "Domain-Name: " + Api.MEI_ZI_TU_DOMAIN_NAME})
	@GET("photo/tag/sm/page/{page}/")
	Observable<String> meiZiTuMm(@Path("page") int page);

	@Headers({"Referer: " + Api.APP_MEI_ZI_TU_DOMAIN, "Domain-Name: " + Api.MEI_ZI_TU_DOMAIN_NAME})
	@GET("photo/{id}")
	Observable<String> meiZiTuImageList(@Path("id") int id);
}
