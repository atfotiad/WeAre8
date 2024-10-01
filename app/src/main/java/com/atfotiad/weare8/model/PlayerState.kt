package com.atfotiad.weare8.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerState(
    val currentPosition: Long ,
    val isPlaying: Boolean
): Parcelable
