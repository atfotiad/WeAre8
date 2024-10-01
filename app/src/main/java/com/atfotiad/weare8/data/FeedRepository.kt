package com.atfotiad.weare8.data

import com.atfotiad.weare8.api.AppClient
import com.atfotiad.weare8.model.LikeRequest
import com.atfotiad.weare8.model.Overview
import com.atfotiad.weare8.model.Post
import retrofit2.Response
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val appClient: AppClient
) {

    suspend fun getPosts(): List<Post> {
        return appClient.getFeed(20).body()?.posts ?: emptyList()
    }

    suspend fun getOverview(creatorsContentId: String): Response<Overview> {
        return appClient.getOverview(
            creatorsContentId,
        )
    }

    suspend fun like(creatorsContentId: String): Response<Unit> {
        return appClient.like(
            LikeRequest(creatorsContentId)
        )
    }

    suspend fun unLike(creatorsContentId: String): Response<Unit> {
        return appClient.unLike(creatorsContentId)
    }
}
