package com.serma.rickandmorty.presentation.mvp

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

class PresenterWrapper<V : BaseView, P : BasePresenter<V>>(val basePresenter: P) :
    ViewModel(),
    DefaultLifecycleObserver {
    fun attachView(view: V) {
        basePresenter.attacheView(view)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        basePresenter.detachView()
    }

    override fun onCleared() {
        super.onCleared()
        basePresenter.destroy()
    }
}