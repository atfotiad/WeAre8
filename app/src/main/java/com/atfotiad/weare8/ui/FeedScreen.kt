package com.atfotiad.weare8.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.atfotiad.weare8.CurrentlyPlayingItem
import com.atfotiad.weare8.FeedUiState
import com.atfotiad.weare8.FeedViewModel
import com.atfotiad.weare8.model.DisplayItem
import com.atfotiad.weare8.ui.common.ShowLoadingIndicator
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@Composable
fun FeedListScreen(viewModel: FeedViewModel) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.getPosts()
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (uiState) {
        is FeedUiState.Loading -> {
            ShowLoadingIndicator()
        }

        is FeedUiState.Success -> {
            FeedList(
                feedItems = (uiState as FeedUiState.Success).feedItems,
                onItemLiked = {
                    scope.launch {
                        viewModel.like(it)
                    }
                },
                onItemUnLiked = {
                    scope.launch {
                        viewModel.unLike(it)
                    }
                }, viewModel
            )
        }

        is FeedUiState.Error -> {
            var showDialog by remember { mutableStateOf(true) }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { },
                    title = { Text(text = "An Error Occurred") },
                    text = {
                        Text(
                            text = (uiState as FeedUiState.Error).exception.message
                                ?: "Unknown Error"
                        )
                    },
                    confirmButton = {
                        Button(onClick = {
                            showDialog = false
                        }) {
                            Text(text = "Dismiss")
                        }
                    },
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    )
                )
            }
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
fun FeedList(
    feedItems: List<DisplayItem>,
    onItemLiked: (DisplayItem) -> Unit,
    onItemUnLiked: (DisplayItem) -> Unit,
    viewModel: FeedViewModel

) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val pxScreenHeight = with(LocalDensity.current) { screenHeight.toPx() }

    val listState = rememberLazyListState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentlyPlayingItem by viewModel.currentlyPlayingItem.collectAsStateWithLifecycle()
    var currentItemIndex by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    val mostVisibleItem by remember {
        derivedStateOf {
            val visibleItemsInfo = listState.layoutInfo.visibleItemsInfo
            val mostVisibleItemIndex = visibleItemsInfo.maxByOrNull {
                calculateVisibilityPercentage(it, pxScreenHeight)
            }?.index

            if (mostVisibleItemIndex != null && mostVisibleItemIndex in feedItems.indices) {
                val mostVisibleItem = feedItems[mostVisibleItemIndex]
                if (mostVisibleItem.post.media.type == "video") {
                    val visibleItemInfo = visibleItemsInfo.find { it.index == mostVisibleItemIndex }
                    if (visibleItemInfo != null) {
                        val visibility =
                            calculateVisibilityPercentage(visibleItemInfo, pxScreenHeight)
                        if (visibility >= 51) {
                            mostVisibleItem
                        } else {
                            null
                        }
                    } else {
                        null
                    }
                } else {
                    null
                }
            } else {
                null
            }
        }
    }

    LaunchedEffect(key1 = mostVisibleItem) {
        if (mostVisibleItem != null) {
            viewModel.updateCurrentlyPlayingItem(
                CurrentlyPlayingItem(
                    mostVisibleItem!!.post.id,
                    true
                )
            )
        } else {
            viewModel.updateCurrentlyPlayingItem(CurrentlyPlayingItem(null, false))
        }
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    viewModel.updateCurrentlyPlayingItem(currentlyPlayingItem.copy(shouldPlay = false))
                }

                Lifecycle.Event.ON_RESUME -> {
                    viewModel.updateCurrentlyPlayingItem(currentlyPlayingItem.copy(shouldPlay = true))
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState
    ) {
        items(feedItems, key = {
            it.post.id
        }) { item ->
            FeedItem(
                displayItem = item,
                Modifier.fillParentMaxSize(),
                onLike = {
                    onItemLiked(item)
                },
                onUnLike = {
                    onItemUnLiked(item)
                }, currentlyPlayingItem = currentlyPlayingItem
            )
        }
    }


//Snap to item on fling
    LaunchedEffect(key1 = Unit) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val firstVisibleItemIndex = layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: 0
            if (firstVisibleItemIndex < feedItems.lastIndex) {
                val nextItem = layoutInfo.visibleItemsInfo.getOrNull(1)
                if (nextItem != null && nextItem.offset < nextItem.size / 2) {
                    firstVisibleItemIndex + 1
                } else {
                    firstVisibleItemIndex
                }
            } else {
                firstVisibleItemIndex
            }
        }.debounce(50L)
            .collect { targetIndex ->
                if (currentItemIndex != targetIndex) {
                    currentItemIndex = targetIndex
                    coroutineScope.launch {
                        listState.animateScrollToItem(targetIndex)
                    }
                }
            }
    }
}


fun calculateVisibilityPercentage(
    itemInfo: LazyListItemInfo,
    pxScreenHeight: Float
): Float {
    val itemTop = itemInfo.offset.toFloat()
    val itemBottom = itemTop + itemInfo.size.toFloat()

    val visibleTop = maxOf(0f, itemTop)
    val visibleBottom = minOf(pxScreenHeight, itemBottom)
    val visibleHeight = visibleBottom - visibleTop

    return (visibleHeight / pxScreenHeight) * 100
}