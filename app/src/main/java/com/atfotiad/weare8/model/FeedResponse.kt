package com.atfotiad.weare8.model

data class FeedResponse(
    val continuationToken: String,
    val posts: List<Post>
)