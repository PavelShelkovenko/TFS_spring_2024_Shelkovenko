package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.another

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.homework_5.GetStubAnotherUserUseCase

class AnotherProfileViewModelFactory(private val stubAnotherUserUseCase: GetStubAnotherUserUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnotherProfileViewModel::class.java)) {
            return AnotherProfileViewModel(stubAnotherUserUseCase) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }
}