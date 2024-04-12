package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.local

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.homework_5.GetStubLocalUserUseCase

class LocalProfileViewModelFactory(private val stubLocalUserUseCase: GetStubLocalUserUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocalProfileViewModel::class.java)) {
            return LocalProfileViewModel(stubLocalUserUseCase) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }
}