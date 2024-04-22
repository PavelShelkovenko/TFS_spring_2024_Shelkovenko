package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.local

import androidx.lifecycle.ViewModel
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase.GetOwnProfileUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.runCatchingNonCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OwnProfileViewModel(
    private val getOwnProfileUseCase: GetOwnProfileUseCase
): ViewModel() {

    private val _screenState = MutableStateFlow<OwnProfileScreenState>(OwnProfileScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    suspend fun downloadData() {
        _screenState.value = OwnProfileScreenState.Loading
        runCatchingNonCancellation {
            getOwnProfileUseCase.invoke()
        }.onSuccess { user ->
            _screenState.value = OwnProfileScreenState.Content(ownUser = user)
        }.onFailure { error ->
            _screenState.value = (OwnProfileScreenState.Error(error.message.toString()))
        }
    }
}