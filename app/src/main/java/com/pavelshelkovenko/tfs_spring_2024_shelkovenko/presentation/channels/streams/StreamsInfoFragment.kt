package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentStreamsInfoBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.MainAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.homework_5.GetStubStreamsUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.ChannelFragmentDirections
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.ChannelsScreenState
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.ChannelsViewModel
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.ChannelsViewModelFactory
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.topics.TopicAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class StreamsInfoFragment : Fragment() {

    private var _binding: FragmentStreamsInfoBinding? = null
    private val binding: FragmentStreamsInfoBinding
        get() = _binding ?: throw RuntimeException("FragmentStreamsInfoBinding == null")

    private val mainAdapter by lazy {
        MainAdapter.Builder()
            .add(StreamAdapter())
            .add(TopicAdapter())
            .build()
    }

    private val stubStreamsUseCase = GetStubStreamsUseCase()
    private val viewModel: ChannelsViewModel by activityViewModels(
        factoryProducer = { ChannelsViewModelFactory(stubStreamsUseCase) }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStreamsInfoBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val streamDestination = requireArguments().getSerializable(
            STREAMS_DESTINATION_KEY,
        ) as StreamDestination

        binding.streamsInfoRv.adapter = mainAdapter

        setupClickListeners()

        when(streamDestination) {
            StreamDestination.Subscribed -> {
                viewModel.subscribedScreenState
                    .flowWithLifecycle(lifecycle)
                    .onEach(::render)
                    .launchIn(lifecycleScope)
            }
            StreamDestination.AllStreams -> {
                viewModel.allStreamsScreenState
                    .flowWithLifecycle(lifecycle)
                    .onEach(::render)
                    .launchIn(lifecycleScope)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        val streamDestination = requireArguments().getSerializable(
            STREAMS_DESTINATION_KEY,
        ) as StreamDestination
        viewModel.streamDestination.tryEmit(streamDestination)
        lifecycleScope.launch {
            viewModel.refreshScreenState()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun render(newScreenState: ChannelsScreenState) {
        when(newScreenState) {
            is ChannelsScreenState.Initial -> {
                with(binding) {
                    shimmerContainer.isVisible = false
                    streamsInfoRv.isVisible = false
                    errorContainer.isVisible = false
                }
            }
            is ChannelsScreenState.Loading -> {
                with(binding) {
                    shimmerContainer.isVisible = true
                    streamsInfoRv.isVisible = false
                    errorContainer.isVisible = false
                    shimmerContainer.startShimmer()
                }
            }
            is ChannelsScreenState.Content -> {
                with(binding) {
                    shimmerContainer.stopShimmer()
                    shimmerContainer.isVisible = false
                    errorContainer.isVisible = false
                    streamsInfoRv.isVisible = true
                }
                mainAdapter.submitList(newScreenState.streamsList)
            }
            is ChannelsScreenState.Error -> {
                with(binding) {
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
            viewModel.onStreamClick(stream)
        }

        /*
        Tут streamName получаем таким странным образом потому, что непонятно пока вообще как будет
        получаться информация по стримам, по топикам, будут ли методы другие чтобы в сеть и вытащить
        топик по стриму. В общем пока на стабах, сделал так, чтобы работало более менее, но на лучшее
        решение не претендую
         */
        topicAdapter.onTopicClickListener = { topic ->
            val streamName = try {
                viewModel.findStreamByItsTopic(topic = topic).name
            } catch (ex: Exception) {
                "Stream name wasn't found"
            }
            findNavController().navigate(
                ChannelFragmentDirections.actionChannelFragmentToChatFragment(
                    topicName = "Topic: ${topic.name}",
                    streamName = streamName
                )
            )
        }

        binding.errorComponent.retryButton.setOnClickListener {
            lifecycleScope.launch {
                if (viewModel.searchQueryFlow.value.isNotEmpty()) {
                    viewModel.processSearch()
                } else {
                    viewModel.setupStubData()
                }
            }
        }

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