package com.atfotiad.weare8.ui

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.atfotiad.weare8.CurrentlyPlayingItem
import com.atfotiad.weare8.FeedViewModel
import com.atfotiad.weare8.R
import com.atfotiad.weare8.model.CommentsOverviewDetails
import com.atfotiad.weare8.model.DisplayItem
import com.atfotiad.weare8.model.LikesOverviewDetails
import com.atfotiad.weare8.model.SharesOverviewDetails

@OptIn(UnstableApi::class)
@Composable
fun FeedItem(
    displayItem: DisplayItem,
    modifier: Modifier = Modifier,
    onLike: () -> Unit,
    onUnLike: () -> Unit,
    currentlyPlayingItem: CurrentlyPlayingItem,
) {

    Box(modifier = modifier) {
        if (displayItem.post.media.type == "image") {
            AsyncImage(
                model = displayItem.post.media.uri,
                error = painterResource(id = R.drawable.camera_image_photo_svgrepo_com),
                contentDescription = "Image description",
                modifier = Modifier.matchParentSize()
            )
        } else {
            val context = LocalContext.current
            val exoplayer = remember {
                ExoPlayer.Builder(context).build().apply {
                    repeatMode = Player.REPEAT_MODE_ALL
                }
            }

            val viewModel: FeedViewModel = hiltViewModel()
            val simpleCache = viewModel.getSimpleCache()
            val cacheDataSourceFactory = CacheDataSource.Factory()
                .setCache(simpleCache)
                .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context))

            AndroidView(
                factory = {
                    PlayerView(it).apply {
                        player = exoplayer
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        setShowNextButton(false)
                        setShowPreviousButton(false)
                        setShowFastForwardButton(false)
                        setShowRewindButton(false)
                        setShowSubtitleButton(false)
                        controllerAutoShow = false
                        useController = false

                    }
                }, modifier = Modifier.matchParentSize()
            )

            LaunchedEffect(key1 = displayItem.post.id) {
                val mediaItem = MediaItem.fromUri(Uri.parse(displayItem.post.media.uri))
                val mediaSource =
                    HlsMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem)
                exoplayer.setMediaSource(mediaSource)
                exoplayer.prepare()
                exoplayer.playWhenReady = true
            }

            DisposableEffect(exoplayer) {
                onDispose {
                    exoplayer.release()
                }
            }

            LaunchedEffect(key1 = currentlyPlayingItem) {
                if (displayItem.post.id == currentlyPlayingItem.id) {
                    if (currentlyPlayingItem.shouldPlay) {
                        exoplayer.play()
                    } else {
                        exoplayer.pause()
                    }
                } else {
                    exoplayer.pause()
                }
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 112.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy((-12).dp)

        ) {
            listOf(
                displayItem.overview?.likesOverviewDetails,
                displayItem.overview?.commentsOverviewDetails,
                displayItem.overview?.sharesOverviewDetails
            ).forEach { type ->
                when (type) {
                    is LikesOverviewDetails -> {
                        var checked by remember { mutableStateOf(type.liked) }

                        IconToggleButton(checked = checked, onCheckedChange = {
                            checked = it
                            if (checked) {
                                type.liked = true
                                type.likesCount += 1
                                onLike()

                            } else {
                                type.liked = false
                                type.likesCount -= 1
                                onUnLike()
                            }
                        }) {
                            Icon(
                                imageVector = if (checked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = if (checked) "Liked" else "Not Liked",
                                tint = if (checked) Color.Red else Color.White
                            )
                        }
                        Text(text = type.likesCount.toString())
                    }

                    is CommentsOverviewDetails -> {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.chat_bubble_24px),
                                contentDescription = "Comment",
                                tint = Color.White
                            )
                        }
                        Text(text = type.commentsCount.toString())
                    }

                    is SharesOverviewDetails -> {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.forward_24px),
                                contentDescription = "Share",
                                tint = Color.White
                            )
                        }
                        Text(text = type.sharesCount.toString())
                    }
                }
            }
        }
    }
}
