package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.UserRepository

class PeopleViewModelFactory(private val userRepository: UserRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeopleViewModel::class.java)) {
            return PeopleViewModel(userRepository) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }
}