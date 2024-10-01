package com.atfotiad.weare8.model

data class Media(
    val aspectRatio: String,
    val blurHash: String,
    val cover: Cover,
    val height: Int,
    val type: String,
    val uri: String,
    val uriMedium: String,
    val uriSmall: String,
    val width: Int
)