package com.easyhz.mvvm_vs_mvi.mvi.example

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.easyhz.mvvm_vs_mvi.domain.model.entity.ExamplePost
import com.easyhz.mvvm_vs_mvi.util.Loading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun PostScreen() {
    val viewModel: PostViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )
    val context = LocalContext.current

    Column {
        Button(onClick = { viewModel.postIntent(PostIntent.Refresh) }) {
            Text(text = "Refresh")
        }
        LazyColumn {
            items(uiState.post) { post ->
                PostCard(post = post) {
                    viewModel.postIntent(PostIntent.OnClick(it))
                }
            }
        }
    }
    if (uiState.isLoading) {
        Loading()
    }

    viewModel.sideEffect.collectInLaunchedEffectWithLifecycle { sideEffect ->
        when (sideEffect) {
            is PostSideSideEffect.ShowToast -> {
                showToast(context, sideEffect.message)
            }
            is PostSideSideEffect.ShowErrorToast -> {
                showToast(context, "알 수 없는 에러입니다.")
            }
        }
    }
//    LaunchedEffect(key1 = viewModel) {
//        viewModel.effect.collect {sideEffect ->
//            when (sideEffect) {
//                is PostSideEffect.ShowToast -> {
//                    showToast(context, sideEffect.message)
//                }
//                is PostSideEffect.ShowErrorToast -> {
//                    showToast(context, "알 수 없는 에러입니다.")
//                }
//            }
//
//        }
//    }
}

@Composable
private fun PostCard(
    post: ExamplePost,
    onClick: (ExamplePost) -> Unit
) {
    Column(
        modifier = Modifier
            .border(width = 1.dp, color = Color.Gray)
            .clickable { onClick(post) }
    ) {
        Text(text = "id: ${post.id}")
        Text(text = "title: ${post.title}")
        Text(text = "content: ${post.body}")
        Text(text = "userId: ${post.userId}")
    }
}
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@SuppressLint("ComposableNaming")
@Composable
fun <T> Flow<T>.collectInLaunchedEffectWithLifecycle(
    vararg keys: Any?,
    lifecycle: Lifecycle = androidx.compose.ui.platform.LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    collector: suspend CoroutineScope.(sideEffect: T) -> Unit,
) {
    val flow = this
    val currentCollector by rememberUpdatedState(collector)

    LaunchedEffect(flow, lifecycle, minActiveState, *keys) {
        withContext(Dispatchers.Main.immediate) {
            lifecycle.repeatOnLifecycle(minActiveState) {
                flow.collect { currentCollector(it) }
            }
        }
    }
}