package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.another

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.homework_5.GetStubAnotherUserUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.runCatchingNonCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnotherProfileViewModel(
    private val stubAnotherUserUseCase: GetStubAnotherUserUseCase
): ViewModel() {

    private val _screenState = MutableStateFlow<AnotherProfileScreenState>(AnotherProfileScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            setupStubData()
        }
    }

    suspend fun setupStubData() {
        _screenState.value = AnotherProfileScreenState.Loading
        runCatchingNonCancellation {
            stubAnotherUserUseCase.invoke()
        }.onSuccess { stubAnotherUser ->
            _screenState.value = AnotherProfileScreenState.Content(anotherUser = stubAnotherUser)
        }.onFailure { error ->
            _screenState.value = AnotherProfileScreenState.Error(error.message.toString())
        }
    }
}