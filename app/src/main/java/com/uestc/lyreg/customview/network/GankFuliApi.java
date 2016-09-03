package com.uestc.lyreg.customview.network;

import com.uestc.lyreg.customview.models.GankResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2016/6/23.
 *
 * @Author lyreg
 */
public interface GankFuliApi {

    @GET("data/福利/{number}/{page}")
    Observable<GankResult> getGankFuli(@Path("number") int number, @Path("page") int page);
}
