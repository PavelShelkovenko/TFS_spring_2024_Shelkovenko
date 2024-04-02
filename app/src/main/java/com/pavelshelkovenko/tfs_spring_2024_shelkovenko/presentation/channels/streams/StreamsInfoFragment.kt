package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.findNavController
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.MainAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentStreamsInfoBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.ChannelFragmentDirections
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.ChannelsViewModel
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.topics.TopicAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.ChatFragment
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.ChatFragmentArgs
import kotlinx.coroutines.launch

class StreamsInfoFragment: Fragment() {

    private var _binding: FragmentStreamsInfoBinding? = null
    private val binding: FragmentStreamsInfoBinding
        get() = _binding ?: throw RuntimeException("FragmentStreamsInfoBinding == null")

    private val mainAdapter by lazy {
        MainAdapter.Builder()
            .add(StreamAdapter())
            .add(TopicAdapter())
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStreamsInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: ChannelsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val streamDestination = requireArguments().getString(STREAMS_DESTINATION_KEY, ALL_STREAMS_DESTINATION)

        binding.streamsInfoRv.adapter = mainAdapter

        setupClickListeners(streamDestination)

        when(streamDestination) {
            ALL_STREAMS_DESTINATION -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModel.allStreamsList.collect {
                            mainAdapter.submitList(it)
                        }
                    }
                }
            }
            SUBSCRIBED_STREAMS_DESTINATION -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModel.subscribedStreamsList.collect {
                            mainAdapter.submitList(it)
                        }
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupClickListeners(streamDestination: String) {
        val streamAdapter = mainAdapter.delegates.get(0) as StreamAdapter
        val topicAdapter = mainAdapter.delegates.get(1) as TopicAdapter

        streamAdapter.onStreamClickListener = { stream: StreamDelegateItem ->
            viewModel.onStreamClick(stream, streamDestination)
        }

        /*
        Tут streamName получаем таким странным образом потому, что непонятно пока вообще как будет
        получаться информация по стримам, по топикам, будут ли методы другие чтобы в сеть и вытащить
        топик по стриму. В общем пока на стабах, сделал так, чтобы работало более менее, но на лучшее
        решение не претендую_
         */
        topicAdapter.onTopicClickListener = { topic ->
            val streamName = try {
                viewModel.findStreamByItsTopic(topic = topic, streamDestination).name
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

    }

    companion object {

        private const val STREAMS_DESTINATION_KEY = "streams_destination_key"
        const val ALL_STREAMS_DESTINATION = "all_streams_destination"
        const val SUBSCRIBED_STREAMS_DESTINATION = "subscribed_streams_destination"

        @JvmStatic
        fun newInstance(streamDestination: String): StreamsInfoFragment {
            return StreamsInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(STREAMS_DESTINATION_KEY, streamDestination)
                }
            }
        }
    }
}