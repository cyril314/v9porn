package com.u9porn.data.network.apiservice;

import com.u9porn.data.network.Api;
import io.reactivex.Observable;
import retrofit2.http.*;

public interface KeDouServiceApi {

    @Headers({"Domain-Name: " + Api.KE_DOU_WO_DOMAIN_NAME})
    @GET("{category}/?mode=async&function=get_block")
    Observable<String> videoList(@Path("category") String category, @Query("block_id") String bid,
                                 @Query("sort_by") String sort, @Query("from") int page);

    /**
     * 视频相关
     */
    @Headers({"Domain-Name: " + Api.KE_DOU_WO_DOMAIN_NAME})
    @GET
    Observable<String> videoRelated(@Url String url, @Header("X-Forwarded-For") String ipAddress);

    @Headers({"Domain-Name: " + Api.KE_DOU_WO_DOMAIN_NAME})
    @GET
    Observable<String> getRealVideoUrl(@Url String url);
}
