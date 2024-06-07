package com.easyhz.mvvm_vs_mvi.mvi.example

import androidx.lifecycle.viewModelScope
import com.easyhz.mvvm_vs_mvi.domain.model.entity.ExamplePost
import com.easyhz.mvvm_vs_mvi.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


data class PostState(
    val post: List<ExamplePost>,
    val isLoading: Boolean
): UiState() {
    companion object {
        fun init() = PostState(post = emptyList(), isLoading = false)
    }
}

sealed class PostIntent : UiIntent() {
    data class OnClick(val post: ExamplePost): PostIntent()
    data object Refresh: PostIntent()
}

sealed class PostSideSideEffect: UiSideEffect() {
    data object ShowErrorToast: PostSideSideEffect()
    data class ShowToast(val message: String) : PostSideSideEffect()
}

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository
):BaseViewModel<PostState, PostIntent, PostSideSideEffect>(
    PostState.init()
) {

    init {
        fetchPost()
    }

    override fun handleIntent(intent: PostIntent) {
        when(intent) {
            is PostIntent.OnClick -> {
                onClick(intent.post.title)
            }
            is PostIntent.Refresh -> {
                onClick("Refresh")
                fetchPost()
            }
        }
    }

    private fun fetchPost() = viewModelScope.launch {
        reduce { copy(isLoading = true) }
        delay(1000)
        repository.getPost()
            .onSuccess {
                reduce { copy(post = it) }
            }.onFailure {
                postSideEffect { PostSideSideEffect.ShowErrorToast }
            }.also {
                reduce { copy(isLoading = false) }
            }
    }

    private fun onClick(message: String) {
        println("onclick  :$message")
        postSideEffect { PostSideSideEffect.ShowToast(message) }
    }
}