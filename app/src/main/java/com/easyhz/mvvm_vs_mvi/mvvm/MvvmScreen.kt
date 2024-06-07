package com.easyhz.mvvm_vs_mvi.mvvm

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.easyhz.mvvm_vs_mvi.domain.model.Post
import com.easyhz.mvvm_vs_mvi.util.Loading

@Composable
fun MvvmScreen(
    navController: NavHostController,
    viewModel: MvvmViewModel = hiltViewModel()
) {
    MvvmScreenContent(
        post = viewModel.post,
        isLoading = viewModel.isLoading,
        isLiked = viewModel.isLiked,
        onToggleLike = { viewModel.toggleLike() } ,
        onBackClick = { navController.navigateUp() }
    )
}

@Composable
private fun MvvmScreenContent(
    post: Post?,
    isLoading: Boolean,
    isLiked: Boolean,
    onToggleLike: () -> Unit,
    onBackClick: () -> Unit
) {
    if (isLoading) {
        // 로딩
        Loading()
    } else {
        /*
         * `post` 내용
         * `isLiked`의 표기
         * `onToggleLike`, `onBackClick` 등이 발생할 수 있음
         */
    }
}