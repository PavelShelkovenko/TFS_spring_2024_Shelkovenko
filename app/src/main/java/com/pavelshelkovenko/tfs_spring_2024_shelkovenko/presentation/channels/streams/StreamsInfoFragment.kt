package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentStreamsInfoBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.DiContainer
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.ElmBaseFragment
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.ChannelFragment
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.ChannelFragmentDirections
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.adapter.StreamAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.adapter.StreamDelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.topics.TopicAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.delegate_adapter.MainAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store

class StreamsInfoFragment :
    ElmBaseFragment<StreamEffect, StreamState, StreamEvent>(R.layout.fragment_streams_info) {

    private val binding: FragmentStreamsInfoBinding by viewBinding(FragmentStreamsInfoBinding::bind)

    private var firstBoot = true

    private val mainAdapter by lazy {
        MainAdapter.Builder()
            .add(StreamAdapter())
            .add(TopicAdapter())
            .build()
    }

    override val store: Store<StreamEvent, StreamEffect, StreamState> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        DiContainer.streamStoreFactory.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.streamsInfoRv.adapter = mainAdapter
        setupClickListeners()

        if (savedInstanceState == null && firstBoot) {
            store.accept(StreamEvent.Ui.Init(streamDestination = getStreamDestinationFromArgs()))
        }
        firstBoot = false
    }

    override fun onResume() {
        super.onResume()
        (parentFragment as ChannelFragment).searchQueryFlow
            .drop(1)
            .debounce(800)
            .onEach { query ->
                store.accept(StreamEvent.Ui.QueryChanged(query, getStreamDestinationFromArgs()))
            }
            .flowOn(Dispatchers.IO)
            .launchIn(lifecycleScope)
    }

    override fun handleEffect(effect: StreamEffect) {
        when (effect) {
            is StreamEffect.OnTopicClick -> {
                findNavController().navigate(
                    ChannelFragmentDirections.actionChannelFragmentToChatFragment(
                        topicName = effect.topicName,
                        streamName = effect.streamName
                    )
                )
            }
        }
    }

    override fun render(state: StreamState) {
        with(binding) {
            when (state) {
                is StreamState.Initial -> {
                    shimmerContainer.isVisible = false
                    streamsInfoRv.isVisible = false
                    errorContainer.isVisible = false
                }

                is StreamState.Loading -> {
                    shimmerContainer.isVisible = true
                    streamsInfoRv.isVisible = false
                    errorContainer.isVisible = false
                    shimmerContainer.startShimmer()
                }

                is StreamState.Content -> {
                    shimmerContainer.stopShimmer()
                    shimmerContainer.isVisible = false
                    errorContainer.isVisible = false
                    streamsInfoRv.isVisible = true
                    when(getStreamDestinationFromArgs()) {
                        StreamDestination.ALL -> {
                            mainAdapter.submitList(state.allStreamsList)
                        }
                        StreamDestination.SUBSCRIBED -> {
                            mainAdapter.submitList(state.subscribedStreamsList)
                        }
                    }

                }

                is StreamState.Error -> {
                    shimmerContainer.stopShimmer()
                    errorContainer.isVisible = true
                    shimmerContainer.isVisible = false
                    streamsInfoRv.isVisible = false
                }
            }
        }
    }

    private fun setupClickListeners() {
        val streamAdapter = mainAdapter.delegates.get(0) as StreamAdapter
        val topicAdapter = mainAdapter.delegates.get(1) as TopicAdapter

        streamAdapter.onStreamClickListener = { stream: StreamDelegateItem ->
            store.accept(
                StreamEvent.Ui.OnStreamClick(
                    stream = stream,
                    streamDestination = getStreamDestinationFromArgs()
                )
            )
        }

        topicAdapter.onTopicClickListener = { topic ->
            store.accept(
                StreamEvent.Ui.OnTopicClick(
                    topic = topic,
                    streamDestination = getStreamDestinationFromArgs()
                )
            )
        }

        binding.errorComponent.retryButton.setOnClickListener {
            store.accept(
                StreamEvent.Ui.QueryChanged(
                    newQuery = (parentFragment as ChannelFragment).searchQueryFlow.value,
                    streamDestination = getStreamDestinationFromArgs()
                )
            )
        }
    }


    private fun getStreamDestinationFromArgs(): StreamDestination {
        return requireArguments().getSerializable(
            STREAMS_DESTINATION_KEY,
        ) as StreamDestination
    }

    companion object {

        private const val STREAMS_DESTINATION_KEY = "streams_destination_key"

        @JvmStatic
        fun newInstance(streamDestination: StreamDestination): StreamsInfoFragment {
            return StreamsInfoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(STREAMS_DESTINATION_KEY, streamDestination)
                }
            }
        }
    }
}