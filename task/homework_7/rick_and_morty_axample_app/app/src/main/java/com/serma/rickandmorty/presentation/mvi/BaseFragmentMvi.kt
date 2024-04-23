package com.serma.rickandmorty.presentation.mvi

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

abstract class BaseFragmentMvi<PartialState : MviPartialState,
        Intent : MviIntent,
        State : MviState,
        Effect : MviEffect>(@LayoutRes layoutId: Int) :
    Fragment(layoutId) {

    protected abstract val store: MviStore<PartialState, Intent, State, Effect>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            store.uiState.collect(::render)
        }
        store.effect.onEach { resolveEffect(it) }.launchIn(lifecycleScope)
    }

    protected abstract fun render(state: State)
    protected abstract fun resolveEffect(effect: Effect)
}