package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.homework_5.GetStubUsersUseCase

class PeopleViewModelFactory(private val stubUsersUseCase: GetStubUsersUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeopleViewModel::class.java)) {
            return PeopleViewModel(stubUsersUseCase) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }
}