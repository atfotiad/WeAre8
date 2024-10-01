package com.atfotiad.weare8.model

data class Overview(
    val commentsOverviewDetails: CommentsOverviewDetails,
    val isFollowed: Boolean,
    val likesOverviewDetails: LikesOverviewDetails,
    val sharesOverviewDetails: SharesOverviewDetails
)