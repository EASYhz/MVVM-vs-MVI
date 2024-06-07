package com.easyhz.mvvm_vs_mvi.mvvm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhz.mvvm_vs_mvi.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MvvmViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    var post by mutableStateOf<Post?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var isLiked by mutableStateOf(false)
        private set

    init {
        savedStateHandle.get<String>("id")?.let { id ->
            loadPost(id)
        }
    }

    fun toggleLike() {
        post?.let {
            post = it.copy(isLiked = !it.isLiked)
        }
    }

    private fun loadPost(id: String) = viewModelScope.launch {
//        isLoading = true
//        post = repository.fetchPost(id)
//        isLoading = false
    }
}