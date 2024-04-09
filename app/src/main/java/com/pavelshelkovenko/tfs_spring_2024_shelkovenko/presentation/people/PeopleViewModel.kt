package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.homework_5.GetStubUsersUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.runCatchingNonCancellation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PeopleViewModel(
    private val stubUsersUseCase: GetStubUsersUseCase
): ViewModel() {

    private val _screenState = MutableStateFlow<PeopleScreenState>(PeopleScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    val searchQueryFlow = MutableStateFlow("")

    init {
        collectSearchQuery()
    }

    private fun collectSearchQuery() {
        searchQueryFlow
            .distinctUntilChanged { old, new -> old.contentEquals(new) }
            .debounce(1000L)
            .onEach { processSearch(it.trim()) }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    suspend fun processSearch(query: String) {
        _screenState.value = PeopleScreenState.Loading
        runCatchingNonCancellation {
            stubUsersUseCase.search(query)
        }.onSuccess {
            _screenState.value = PeopleScreenState.Content(it)
        }.onFailure {
          _screenState.value = PeopleScreenState.Error(it.message.toString())
        }
    }

    suspend fun setupStubData() {
        _screenState.value = PeopleScreenState.Loading
        runCatchingNonCancellation {
            stubUsersUseCase.invoke()
        }.onSuccess {
            _screenState.value = PeopleScreenState.Content(userList = it)
        }.onFailure {
            _screenState.value = PeopleScreenState.Error(it.message.toString())
        }
    }
}