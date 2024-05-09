package com.example.testapp.feature_list.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.testapp.R
import com.example.testapp.databinding.ActivityListBinding
import com.example.testapp.feature_details.DetailsActivity
import com.example.testapp.feature_list.di.FeatureListComponentInjector
import com.example.testapp.feature_list.presentation.FeatureListEffect
import com.example.testapp.feature_list.presentation.FeatureListEffect.OpenDetails
import com.example.testapp.feature_list.presentation.FeatureListEvent.FeatureListUiEvent.OnButtonClick
import com.example.testapp.feature_list.presentation.FeatureListEvent.FeatureListUiEvent.OnItemClick
import com.example.testapp.feature_list.ui.recycler.CardItemAdapter
import com.example.testapp.feature_list.ui.state.FeatureListUiState
import kotlinx.coroutines.launch
import vivid.money.elmslie.android.elmStore

class ListActivity : AppCompatActivity(R.layout.activity_list) {

    private val component by lazy { FeatureListComponentInjector.getComponent() }
    private val uiStateMapper by lazy { component.featureListUiStateMapper }
    private val store by elmStore { component.featureListStore }

    private val binding: ActivityListBinding by viewBinding(ActivityListBinding::bind)
    private val adapter: CardItemAdapter by lazy { CardItemAdapter { store.accept(OnItemClick(it)) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(lifecycleScope) {
            launch { store.effects.collect(::handleEffect) }
            launch { store.states.collect { render(uiStateMapper(it)) } }
        }

        binding.recycler.adapter = adapter
        binding.load.setOnClickListener { store.accept(OnButtonClick(category = "hot")) }
    }

    private fun render(uiState: FeatureListUiState) = with(binding) {
        error.isGone = !uiState.isError
        progress.isGone = !uiState.isLoading
        load.isGone = uiState.isLoading
        adapter.setItems(uiState.items)
    }

    private fun handleEffect(effect: FeatureListEffect) {
        when (effect) {
            is OpenDetails -> startActivity(DetailsActivity.createIntent(this, effect.title))
        }
    }
}
