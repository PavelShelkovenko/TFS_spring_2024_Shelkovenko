package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentChannelBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.homework_5.GetStubStreamsUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.StreamsInfoFragment

class ChannelFragment : Fragment() {

    private var _binding: FragmentChannelBinding? = null
    private val binding: FragmentChannelBinding
        get() = _binding ?: throw RuntimeException("FragmentChannelBinding == null")

    private val stubStreamsUseCase = GetStubStreamsUseCase()
    private val viewModel: ChannelsViewModel by activityViewModels(
        factoryProducer = { ChannelsViewModelFactory(stubStreamsUseCase) }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabs: List<String> = listOf(
            resources.getString(R.string.subscribed),
            resources.getString(R.string.all_streams)
        )
        val pagerAdapter = ChannelPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        binding.channelViewPager.adapter = pagerAdapter
        pagerAdapter.update(
            listOf(
                StreamsInfoFragment.newInstance(StreamDestination.Subscribed),
                StreamsInfoFragment.newInstance(StreamDestination.AllStreams),
            )
        )
        TabLayoutMediator(binding.tabLayout, binding.channelViewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()


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

        binding.cancelButton.setOnClickListener {
            binding.searchField.setText("")
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}