package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.people

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentPeopleBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.ElmBaseFragment
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.people.adapter.PeopleAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.getApplication
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.showErrorToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class PeopleFragment :
    ElmBaseFragment<PeopleEffect, PeopleState, PeopleEvent>(R.layout.fragment_people) {

    private val binding: FragmentPeopleBinding by viewBinding(FragmentPeopleBinding::bind)

    private val searchQueryFlow = MutableStateFlow("")

    private var firstBoot = true

    @Inject
    lateinit var peopleStoreFactory: PeopleStoreFactory

    override fun onAttach(context: Context) {
        context.getApplication
            .appComponent
            .peopleComponent()
            .inject(this)
        super.onAttach(context)
    }


    override val store: Store<PeopleEvent, PeopleEffect, PeopleState> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        peopleStoreFactory.create()
    }

    private val peopleAdapter by lazy {
        PeopleAdapter(
            onUserClickListener = { userId ->
                clearSearchFieldText()
                findNavController().navigate(
                    PeopleFragmentDirections.actionPeopleFragmentToAnotherProfileFragment(userId)
                )
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null && firstBoot) {
            store.accept(PeopleEvent.Ui.StartProcess)
        }
        firstBoot = false
        setupClickListeners()
        /*
        drop(1) нужен для того, чтобы мы не делали запрос в сеть лишний раз, так как onEach всегда
        сработает 1 раз, даже если мы не поменяем текст в поиске. Случается это из-за того, что у
        StateFlow replay = 1, так еще и addTextChangedListener на 83 строчке заэмитит значение во флоу
        даже если в searchField ничего не поменялось.
         */

        with(binding) {
            peopleRv.adapter = peopleAdapter
            searchField.addTextChangedListener { editable ->
                editable?.let { query ->
                    searchQueryFlow.tryEmit(query.trim().toString())
                    if (query.isEmpty()) {
                        showQuestionMarkButton()
                    } else {
                        showCancelButton()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        searchQueryFlow
            .drop(1)
            .debounce(800)
            .onEach { query ->
                store.accept(PeopleEvent.Ui.QueryChanged(query))
            }
            .flowOn(Dispatchers.IO)
            .launchIn(lifecycleScope)
    }

    override fun handleEffect(effect: PeopleEffect) {
        when(effect) {
            is PeopleEffect.MinorError -> { showErrorToast(effect.errorMessageId, requireActivity()) }
        }
    }

    override fun render(state: PeopleState) {
        with(binding) {
            when (state) {
                is PeopleState.Content -> {
                    shimmerContainer.stopShimmer()
                    shimmerContainer.isVisible = false
                    peopleRv.isVisible = true
                    errorContainer.isVisible = false
                    peopleAdapter.submitList(state.userList)
                }

                is PeopleState.Error -> {
                    shimmerContainer.stopShimmer()
                    shimmerContainer.isVisible = false
                    peopleRv.isVisible = false
                    errorContainer.isVisible = true
                }

                is PeopleState.Initial -> {
                    shimmerContainer.isVisible = false
                    errorContainer.isVisible = false
                    peopleRv.isVisible = false
                }

                is PeopleState.Loading -> {
                    shimmerContainer.isVisible = true
                    errorContainer.isVisible = false
                    peopleRv.isVisible = false
                    shimmerContainer.startShimmer()
                }
            }
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            cancelButton.setOnClickListener {
                clearSearchFieldText()
            }
            errorComponent.retryButton.setOnClickListener {
                lifecycleScope.launch {
                    store.accept(PeopleEvent.Ui.QueryChanged(newQuery = searchQueryFlow.last()))
                }
            }
        }
    }

    private fun showCancelButton() {
        with(binding) {
            questionMarkButton.isVisible = false
            cancelButton.isVisible = true
        }
    }

    private fun showQuestionMarkButton() {
        with(binding) {
            questionMarkButton.isVisible = true
            cancelButton.isVisible = false
        }
    }

    private fun clearSearchFieldText() {
        binding.searchField.text.clear()
    }
}