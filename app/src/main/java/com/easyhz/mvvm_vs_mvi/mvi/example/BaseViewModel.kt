package com.easyhz.mvvm_vs_mvi.mvi.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * State - StateFlow : 초기값을 가져야하고 항상 최신값을 필요로 함
 * Intent - SharedFlow : 이벤트를 처리해야하는 구독자가 존재하지 않으면 무시될 필요가 있음
 * Effect - Channel : 이벤트 공유 X
 *
 */

abstract class UiState

abstract class UiIntent

abstract class UiSideEffect

abstract class BaseViewModel<State: UiState, Intent: UiIntent, SideEffect: UiSideEffect>(
    initialState: State
):ViewModel() {
    val currentState: State
        get() = uiState.value

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState: StateFlow<State>
        get() = _uiState.asStateFlow()

    private val _intent: MutableSharedFlow<Intent> = MutableSharedFlow()
    val intent = _intent.asSharedFlow()

    private val _sideEffect: Channel<SideEffect> = Channel()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        subscribeIntent()
    }

    /**
     * Intent 구독
     */
    private fun subscribeIntent() = viewModelScope.launch {
        intent.collect { handleIntent(it) }
    }

    /**
     * Intent 핸들러
     */
    abstract fun handleIntent(intent: Intent)

    /**
     * State 설정
     */
    protected fun reduce(reducer: State.() -> State) { _uiState.value = currentState.reducer() }

    /**
     * Intent 설정
     */
    fun postIntent(intent: Intent) = viewModelScope.launch { _intent.emit(intent) }

    /**
     * SideEffect 설정
     */
    protected fun postSideEffect(builder: () -> SideEffect) = viewModelScope.launch { _sideEffect.send(builder()) }
}