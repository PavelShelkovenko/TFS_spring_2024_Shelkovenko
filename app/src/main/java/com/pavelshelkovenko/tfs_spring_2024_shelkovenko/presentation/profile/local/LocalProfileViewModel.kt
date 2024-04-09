package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.local

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.homework_5.GetStubLocalUserUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.runCatchingNonCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocalProfileViewModel(
    private val stubLocalUserUseCase: GetStubLocalUserUseCase
): ViewModel() {

    private val _screenState = MutableStateFlow<LocalProfileScreenState>(LocalProfileScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            setupStubData()
        }
    }

    suspend fun setupStubData() {
        _screenState.value = LocalProfileScreenState.Loading
        runCatchingNonCancellation {
            stubLocalUserUseCase.invoke()
        }.onSuccess { stubLocalUser ->
            _screenState.value = LocalProfileScreenState.Content(localUser = stubLocalUser)
        }.onFailure {
            _screenState.value = (LocalProfileScreenState.Error("Some error occurred"))
        }
    }
}