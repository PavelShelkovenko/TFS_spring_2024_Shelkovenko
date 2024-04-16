package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.another

import androidx.lifecycle.ViewModel
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase.GetAnotherProfileUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.runCatchingNonCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AnotherProfileViewModel(
    private val getAnotherProfileUseCase: GetAnotherProfileUseCase
): ViewModel() {

    private val _screenState = MutableStateFlow<AnotherProfileScreenState>(AnotherProfileScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    suspend fun downloadData(userId: Int) {
        _screenState.value = AnotherProfileScreenState.Loading
        runCatchingNonCancellation {
            getAnotherProfileUseCase.invoke(userId)
        }.onSuccess { anotherUser ->
            _screenState.value = AnotherProfileScreenState.Content(anotherUser = anotherUser)
        }.onFailure { error ->
            _screenState.value = AnotherProfileScreenState.Error(error.message.toString())
        }
    }
}