package com.easyhz.mvvm_vs_mvi.mvi

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.easyhz.mvvm_vs_mvi.util.Loading

@Composable
fun MviScreen(
    navHostController: NavHostController,
    viewModel: MviViewModel = hiltViewModel()
) {
    MviScreenContent(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun MviScreenContent(
    state: MviState,
    onAction: (MviAction) -> Unit
) {
    if (state.isLoading) {
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