package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.another

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase.GetAnotherProfileUseCase

class AnotherProfileViewModelFactory(private val getAnotherProfileUseCase: GetAnotherProfileUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnotherProfileViewModel::class.java)) {
            return AnotherProfileViewModel(getAnotherProfileUseCase) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }
}