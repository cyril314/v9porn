package com.u9porn.ui.porn9video.search;

/**
 * @author
 * @date 2018/1/7
 */

public interface ISearch {
    void searchVideos(String searchId, String sort,boolean pullToRefresh);
    int getPlayBackEngine();
    boolean isFirstInSearchPorn91Video();
}
