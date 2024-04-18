package com.serma.rickandmorty.presentation.mvp

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

abstract class BaseFragment<V : BaseView, P : BasePresenter<V>>(@LayoutRes layoutId: Int) :
    Fragment(layoutId), BaseView {

    private val presenterWrapper: PresenterWrapper<V, P> by viewModels {
        PresenterWrapperFactory(createPresenter())
    }

    protected abstract fun createPresenter(): P

    protected fun getPresenter(): P {
        return presenterWrapper.basePresenter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenterWrapper.attachView(this as V)
        viewLifecycleOwner.lifecycle.addObserver(presenterWrapper)
    }
}