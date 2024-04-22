package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.local

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase.GetOwnProfileUseCase

class OwnProfileViewModelFactory(private val getOwnProfileUseCase: GetOwnProfileUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OwnProfileViewModel::class.java)) {
            return OwnProfileViewModel(getOwnProfileUseCase) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }
}