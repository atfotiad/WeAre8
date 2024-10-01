package com.atfotiad.weare8.model

data class Post(
    val caption: Caption,
    val creationDate: String,
    val externalLink: String,
    val id: String,
    val isEightStage: Boolean,
    val moderationDecision: String,
    val media: Media,
    val postPublisher: PostPublisher,
    val publishDate: String,
    val reportDecision: String,
    val viewCount: Int,
    val visibility: String
)