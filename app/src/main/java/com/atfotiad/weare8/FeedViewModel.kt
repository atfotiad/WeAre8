package com.atfotiad.weare8

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.SimpleCache
import com.atfotiad.weare8.data.FeedRepository
import com.atfotiad.weare8.model.DisplayItem
import com.atfotiad.weare8.model.Overview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis


@HiltViewModel
@androidx.annotation.OptIn(UnstableApi::class)
class FeedViewModel
@Inject constructor(
    private val repository: FeedRepository,
    private val simpleCache: SimpleCache
) : ViewModel() {

    private val _uiState = MutableStateFlow<FeedUiState>(FeedUiState.Loading)
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private val _currentlyPlayingItem = MutableStateFlow(CurrentlyPlayingItem())
    val currentlyPlayingItem: StateFlow<CurrentlyPlayingItem> = _currentlyPlayingItem.asStateFlow()

    fun updateCurrentlyPlayingItem(item: CurrentlyPlayingItem) {
        _currentlyPlayingItem.value = item
    }

    fun getSimpleCache(): SimpleCache {
        return simpleCache
    }

    suspend fun getPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val time = measureTimeMillis {
                try {
                    val posts = repository.getPosts()
                    val itemList = coroutineScope {
                        posts.map { post ->
                            async {
                                val overview = fetchOverview(post.id)
                                DisplayItem(post, overview)
                            }
                        }.awaitAll()
                    }
                    _uiState.emit(FeedUiState.Success(itemList))
                } catch (e: Exception) {
                    _uiState.emit(FeedUiState.Error(e))
                }
            }
            println("Time taken: $time ms")
        }
    }

    private suspend fun fetchOverview(postId: String): Overview? {
        val result = repository.getOverview(postId)
        return if (result.isSuccessful && result.body() != null) {
            result.body()!!
        } else {
            null
        }
    }


    suspend fun like(displayItem: DisplayItem) {
        viewModelScope.launch {
            repository.like(displayItem.post.id)
        }
    }

    suspend fun unLike(displayItem: DisplayItem) {
        viewModelScope.launch {
            repository.unLike(displayItem.post.id)
        }
    }
}

sealed interface FeedUiState {
    data object Loading : FeedUiState
    data class Success(val feedItems: List<DisplayItem>) : FeedUiState
    data class Error(val exception: Throwable) : FeedUiState
}

data class CurrentlyPlayingItem(val id: String? = null, val shouldPlay: Boolean = false)