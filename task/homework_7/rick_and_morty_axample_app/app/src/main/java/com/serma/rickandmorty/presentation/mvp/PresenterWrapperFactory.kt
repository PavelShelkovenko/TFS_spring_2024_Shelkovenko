package com.serma.rickandmorty.presentation.mvp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PresenterWrapperFactory<V : BaseView, P : BasePresenter<V>>(private val presenter: P) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PresenterWrapper(presenter) as T
    }
}