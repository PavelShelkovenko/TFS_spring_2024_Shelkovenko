package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.StreamRepository

class ChannelsViewModelFactory(private val repository: StreamRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChannelsViewModel::class.java)) {
            return ChannelsViewModel(repository) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }
}