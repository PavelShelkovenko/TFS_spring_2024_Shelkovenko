package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.ZulipApi
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.ZulipChatRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils.MyUserId
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentChatBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.MainAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.date.MessageDateTimeAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.reaction.EmojiAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.reaction.EmojiFactory
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.received_message.ReceivedMessageAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.send_message.SendMessageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding: FragmentChatBinding
        get() = _binding ?: throw RuntimeException("FragmentChatBinding == null")

    private val args by navArgs<ChatFragmentArgs>()

    private lateinit var viewModel: ChatViewModel

    private val chatAdapter by lazy {
        MainAdapter.Builder()
            .add(MessageDateTimeAdapter())
            .add(ReceivedMessageAdapter())
            .add(SendMessageAdapter())
            .build()
    }

    private val emojiAdapter by lazy {
        EmojiAdapter(EmojiFactory.getEmojiList())
    }

    private val bottomSheetDialog: BottomSheetDialog by lazy {
        BottomSheetDialog(requireActivity(), R.style.EmojiBottomSheetTheme)
    }

    private var isLoading = false

    private var registerForEventsJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatRepository = ZulipChatRepository(ZulipApi())
        viewModel = ViewModelProvider(
            this,
            ChatViewModelFactory(chatRepository)
        )[ChatViewModel::class.java]

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        initRecyclerView()

        setupClickListeners()

        binding.messageField.addTextChangedListener {
            viewModel.processMessageFieldChanges(it.toString())
            val str = it.toString()
            if (str.isBlank()) {
                binding.sendMessageButton.visibility = View.GONE
                binding.sendFileButton.visibility = View.VISIBLE
            } else {
                binding.sendMessageButton.visibility = View.VISIBLE
                binding.sendFileButton.visibility = View.GONE
            }
        }

        with(binding) {
            streamName.text = args.streamName
            topicName.text = "Topic: ${args.topicName}"
        }

        viewModel.screenState
            .flowWithLifecycle(lifecycle)
            .onEach(::render)
            .launchIn(lifecycleScope)


        viewModel.isLoadingNextMessages
            .flowWithLifecycle(lifecycle)
            .onEach { isLoading = it }
            .launchIn(lifecycleScope)

        viewModel.load60messages(streamName = args.streamName, topicName = args.topicName)

        binding.errorComponent.retryButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.load60messages(streamName = args.streamName, topicName = args.topicName)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        registerForEvents()
    }

    override fun onPause() {
        super.onPause()
        registerForEventsJob?.cancel()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.chatRecyclerView.clearOnScrollListeners()
        _binding = null
    }

    private fun registerForEvents() {
        registerForEventsJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                //Нужно из-за того, что очередь для evets от ZulipApi живет только 10 мин
                viewModel.registerForEvents(streamName = args.streamName, topicName = args.topicName)
                delay(10 * 60 * 1000)
            }
        }
    }
    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.stackFromEnd = true
        with(binding) {
            chatRecyclerView.layoutManager = linearLayoutManager
            chatRecyclerView.adapter = chatAdapter
        }

        binding.chatRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = linearLayoutManager.itemCount
                val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
                val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

                if (totalItemCount >= 40 && !isLoading) {
                    if (lastVisibleItemPosition + 10 >= totalItemCount) {
                        viewModel.load20NewestMessages(
                            streamName = args.streamName,
                            topicName = args.topicName
                        )
                    }
                    if (firstVisibleItemPosition <= 10) {
                        viewModel.load20OldestMessages(
                            streamName = args.streamName,
                            topicName = args.topicName
                        )
                    }
                }
            }
        })
    }

    private fun render(newScreenState: ChatScreenState) {
        with(binding) {
            when (newScreenState) {
                is ChatScreenState.Content -> {
                    shimmerContainer.stopShimmer()
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = false
                    chatRecyclerView.isVisible = true
                    chatAdapter.submitList(newScreenState.messages)
                }

                is ChatScreenState.Error -> {
                    shimmerContainer.isVisible = false
                    chatRecyclerView.isVisible = false
                    errorContainer.isVisible = true
                }

                is ChatScreenState.Initial -> {
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = false
                    chatRecyclerView.isVisible = false
                }

                is ChatScreenState.Loading -> {
                    errorContainer.isVisible = false
                    chatRecyclerView.isVisible = false
                    shimmerContainer.isVisible = true
                    shimmerContainer.startShimmer()
                }
            }
        }
    }

    private fun setupClickListeners() {

        val receivedMessageAdapter = chatAdapter.delegates.get(1) as ReceivedMessageAdapter
        val sendMessageAdapter = chatAdapter.delegates.get(2) as SendMessageAdapter

        with(receivedMessageAdapter) {
            localUserId = MyUserId.MY_USER_ID
            onMessageLongClickListener = { messageId ->
                showEmojiBottomSheet(messageId)
            }
            onAddIconClickListener = { messageId ->
                showEmojiBottomSheet(messageId)
            }
            onEmojiClickListener = { messageId, reaction ->
                viewModel.changeEmojiStatus(
                    messageId = messageId,
                    emojiCode = reaction.emojiCode,
                    emojiName = reaction.emojiName
                )
            }
        }

        with(sendMessageAdapter) {
            localUserId = MyUserId.MY_USER_ID
            onMessageLongClickListener = { messageId ->
                showEmojiBottomSheet(messageId)
            }
            onAddIconClickListener = { messageId ->
                showEmojiBottomSheet(messageId)
            }
            onEmojiClickListener = { messageId, reaction ->
                viewModel.changeEmojiStatus(
                    messageId = messageId,
                    emojiCode = reaction.emojiCode,
                    emojiName = reaction.emojiName
                )
            }
        }

        with(binding) {
            backButton.setOnClickListener {
                findNavController().popBackStack()
            }
            sendMessageButton.setOnClickListener {
                if (binding.messageField.text.toString() != "") {
                    viewModel.sendMessage(args.streamName, args.topicName)
                    binding.messageField.setText("")
                    lifecycleScope.launch {
                        delay(500)
                        scrollTheEnd(chatAdapter)
                    }
                }
            }
        }
    }

    private fun showEmojiBottomSheet(messageId: Int) {
        val bottomSheetDialogView = layoutInflater.inflate(R.layout.emoji_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetDialogView)
        val emojiRecyclerView =
            bottomSheetDialogView.findViewById<RecyclerView>(R.id.emoji_recycler_view)
        emojiRecyclerView.setHasFixedSize(true)
        emojiRecyclerView.adapter = emojiAdapter
        bottomSheetDialog.behavior.maxHeight = 1000
        emojiAdapter.onEmojiClickListener = { emoji ->
            viewModel.sendReaction(
                messageId = messageId,
                emojiCode = emoji.emojiCode,
                emojiName = emoji.emojiName
            )
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun scrollTheEnd(chatAdapter: MainAdapter) {
        binding.chatRecyclerView.smoothScrollToPosition(chatAdapter.itemCount)
    }
}