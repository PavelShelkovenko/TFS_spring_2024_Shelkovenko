package com.serma.rickandmorty.presentation.mvp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class BasePresenter<V : BaseView> {

    private var baseView: V? = null
    protected val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun attacheView(view: V) {
        baseView = view
    }

    protected fun runIfViewAvailable(action: (V) -> Unit) {
        baseView?.let { action(it) }
    }

    fun detachView() {
        baseView = null
    }

    fun destroy() {
        coroutineScope.cancel()
    }
}