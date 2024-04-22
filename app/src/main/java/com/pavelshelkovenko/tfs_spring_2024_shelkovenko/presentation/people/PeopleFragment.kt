package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.ZulipApi
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.ZulipUserRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentPeopleBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PeopleFragment : Fragment() {


    private var _binding: FragmentPeopleBinding? = null
    private val binding: FragmentPeopleBinding
        get() = _binding ?: throw RuntimeException("FragmentPeopleBinding == null")

    private lateinit var viewModel: PeopleViewModel

    private val peopleAdapter by lazy {
        PeopleAdapter(
            onUserClickListener = { userId ->
                binding.searchField.setText("")
                findNavController().navigate(
                    PeopleFragmentDirections.actionPeopleFragmentToAnotherProfileFragment(userId)
                )
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = ZulipUserRepository(ZulipApi())
        viewModel = ViewModelProvider(
            this,
            PeopleViewModelFactory(repository)
        )[PeopleViewModel::class.java]
        binding.peopleRv.adapter = peopleAdapter

        setupClickListeners()

        binding.searchField.addTextChangedListener {
            it?.let {
                viewModel.searchQueryFlow.tryEmit(it.toString())
                with(binding) {
                    if (it.toString().isEmpty()) {
                        questionMarkButton.isVisible = true
                        cancelButton.isVisible = false
                    } else {
                        questionMarkButton.isVisible = false
                        cancelButton.isVisible = true
                    }
                }

            }
        }

        viewModel.screenState
            .flowWithLifecycle(lifecycle)
            .onEach(::render)
            .launchIn(lifecycleScope)
    }

    private fun setupClickListeners() {
        with(binding) {
            cancelButton.setOnClickListener {
                binding.searchField.setText("")
            }
            errorComponent.retryButton.setOnClickListener {
                lifecycleScope.launch {
                    if (viewModel.searchQueryFlow.value.isNotEmpty()) {
                        viewModel.processSearch(viewModel.searchQueryFlow.value)
                    } else {
                        viewModel.downloadData()
                    }
                }
            }
        }
    }

    private fun render(newScreenState: PeopleScreenState) {
        with(binding) {
            when (newScreenState) {
                is PeopleScreenState.Content -> {
                    shimmerContainer.stopShimmer()
                    shimmerContainer.isVisible = false
                    peopleRv.isVisible = true
                    errorContainer.isVisible = false
                    peopleAdapter.submitList(newScreenState.userList)
                }
                is PeopleScreenState.Error -> {
                    shimmerContainer.stopShimmer()
                    shimmerContainer.isVisible = false
                    peopleRv.isVisible = false
                    errorContainer.isVisible = true
                }

                is PeopleScreenState.Initial -> {
                    shimmerContainer.isVisible = false
                    errorContainer.isVisible = false
                    peopleRv.isVisible = false
                }

                is PeopleScreenState.Loading -> {
                    shimmerContainer.isVisible = true
                    shimmerContainer.startShimmer()
                    errorContainer.isVisible = false
                    peopleRv.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}