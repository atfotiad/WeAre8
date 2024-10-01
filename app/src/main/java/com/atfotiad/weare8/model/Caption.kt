package com.atfotiad.weare8.model

data class Caption(
    val content: String,
    val mentions: List<Mention>
)