package com.easyhz.mvvm_vs_mvi.mvi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhz.mvvm_vs_mvi.domain.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch


data class MviState(
    val post: Post? = null,
    val isLoading: Boolean = false,
    val isLiked: Boolean = false
)

sealed interface MviAction {
    data object ToggleLike: MviAction
}

@HiltViewModel
class MviViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    var state by mutableStateOf(MviState())
        private set

    init {
        savedStateHandle.get<String>("id")?.let { id ->
            loadPost(id)
        }
    }

    fun onAction(action: MviAction) {
        when(action) {
            MviAction.ToggleLike -> toggleLike()
            else -> Unit
        }
    }

    private fun toggleLike() {
        state.post?.let {
            state = state.copy(
                post = it.copy(isLiked = !it.isLiked)
            )
        }
    }
    private fun loadPost(id: String) = viewModelScope.launch {
        // 글 불러오는 코드 ..
    }
}