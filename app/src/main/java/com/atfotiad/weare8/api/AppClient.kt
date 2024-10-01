package com.atfotiad.weare8.api

import com.atfotiad.weare8.model.FeedResponse
import com.atfotiad.weare8.model.LikeRequest
import com.atfotiad.weare8.model.Overview
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AppClient {

    @GET("eightcontentapi/v7/feed/eight-stage")
    suspend fun getFeed(
        @Query("size") pageSize: Int = 20,
        @Query("country") country: String = "gb",
        @Query("timestamp") timestamp: Long = System.currentTimeMillis()
    ): Response<FeedResponse>


    //gets Likes, Shares, Comments
    @GET("eightsocialapi/v3/feeds/{creatorsContentId}/overview")
    suspend fun getOverview(
        @Path("creatorsContentId") creatorsContentId: String
    ): Response<Overview>

    @POST("eightusersapi/v2/citizens/me/creators-content/like")
    suspend fun like(
        @Body likeRequest: LikeRequest
    ): Response<Unit>

    @POST("eightusersapi/influencers/me/un-liked-creators-content/{creatorsContentId}")
    suspend fun unLike(
        @Path("creatorsContentId") creatorsContentId: String,
        @Header("X-API-Version") version: String = "3.0"
    ): Response<Unit>
}
