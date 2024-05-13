package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentChatBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.ElmBaseFragment
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.delegate_adapter.MainAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.date.MessageDateTimeAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.reaction.EmojiAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.reaction.EmojiFactory
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.received_message.ReceivedMessageAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.send_message.SendMessageAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.MyUserId
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.getApplication
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.showErrorToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ChatFragment :
    ElmBaseFragment<ChatEffect, ChatState, ChatEvent>(R.layout.fragment_chat) {

    private val binding: FragmentChatBinding by viewBinding(FragmentChatBinding::bind)

    private val args by navArgs<ChatFragmentArgs>()

    private var registerForEventsJob: Job? = null

    @Inject
    lateinit var chatStoreFactory: ChatStoreFactory

    @Inject
    lateinit var emojiFactory: EmojiFactory

    override fun onAttach(context: Context) {
        context.getApplication
            .appComponent
            .chatComponent()
            .inject(this)
        super.onAttach(context)
    }

    override val store: Store<ChatEvent, ChatEffect, ChatState> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        chatStoreFactory.create()
    }

    private val chatAdapter by lazy {
        MainAdapter.Builder()
            .add(MessageDateTimeAdapter())
            .add(ReceivedMessageAdapter())
            .add(SendMessageAdapter())
            .build()
    }

    private val emojiAdapter by lazy {
        EmojiAdapter(emojiFactory.getEmojiList())
    }

    private val bottomSheetDialog: BottomSheetDialog by lazy {
        BottomSheetDialog(requireActivity(), R.style.EmojiBottomSheetTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            store.accept(ChatEvent.Ui.StartProcess(streamName = args.streamName, topicName = args.topicName))
        }
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        initRecyclerView()
        setupClickListeners()

        binding.messageField.addTextChangedListener { editable ->
            editable?.let { text ->
                if (text.isBlank()) {
                    showSendFileButton()
                } else {
                    showSendMessageButton()
                }
            }
        }

        with(binding) {
            streamName.text = args.streamName
            topicName.text = "Topic: ${args.topicName}"
        }

        addCallbackToOnBackPressed()
    }

    override fun handleEffect(effect: ChatEffect) {
        when(effect) {
            is ChatEffect.MinorError -> { showErrorToast(effect.errorMessageId, requireActivity()) }
            is ChatEffect.NewMessageReceived -> { scrollToLastMessage() }
            is ChatEffect.CloseChat -> { findNavController().popBackStack() }
        }
    }

    override fun render(state: ChatState) {
        with(binding) {
            when (state) {
                is ChatState.Content -> {
                    shimmerContainer.stopShimmer()
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = false
                    chatRecyclerView.isVisible = true
                    chatAdapter.submitList(state.messages)
                }

                is ChatState.Error -> {
                    shimmerContainer.isVisible = false
                    chatRecyclerView.isVisible = false
                    errorContainer.isVisible = true
                }

                is ChatState.Initial -> {
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = false
                    chatRecyclerView.isVisible = false
                }

                is ChatState.Loading -> {
                    errorContainer.isVisible = false
                    chatRecyclerView.isVisible = false
                    shimmerContainer.isVisible = true
                    shimmerContainer.startShimmer()
                }
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


    private fun registerForEvents() {
        registerForEventsJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                //Нужно из-за того, что очередь для evets от ZulipApi живет только 10 мин
                store.accept(
                    ChatEvent.Ui.RegisterForChatEvents(
                        streamName = args.streamName,
                        topicName = args.topicName
                    )
                )
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

                if (totalItemCount >= 25) {
                    if (lastVisibleItemPosition + 5 >= totalItemCount) {
                        store.accept(
                            ChatEvent.Ui.LoadPagingNewerMessages(
                                streamName = args.streamName,
                                topicName = args.topicName
                            )
                        )
                    }
                    if (firstVisibleItemPosition <= 5) {
                        store.accept(
                            ChatEvent.Ui.LoadPagingOlderMessages(
                                streamName = args.streamName,
                                topicName = args.topicName
                            )
                        )
                    }
                }
            }
        })
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
                store.accept(
                    ChatEvent.Ui.OnEmojiClick(
                        messageId = messageId,
                        emojiCode = reaction.emojiCode,
                        emojiName = reaction.emojiName
                    )
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
                store.accept(
                    ChatEvent.Ui.OnEmojiClick(
                        messageId = messageId,
                        emojiCode = reaction.emojiCode,
                        emojiName = reaction.emojiName
                    )
                )
            }
        }

        with(binding) {
            errorComponent.retryButton.setOnClickListener {
                store.accept(
                    ChatEvent.Ui.ReloadData(
                        streamName = args.streamName,
                        topicName = args.topicName
                    )
                )
            }
            backButton.setOnClickListener {
               store.accept(
                   ChatEvent.Ui.ClosingChat(
                       streamName = args.streamName,
                       topicName = args.topicName
                   )
               )
            }
            sendMessageButton.setOnClickListener {
                if (binding.messageField.text.trim().toString() != "") {
                    store.accept(
                        ChatEvent.Ui.SendMessage(
                            message = binding.messageField.text.toString(),
                            streamName = args.streamName,
                            topicName = args.topicName
                        )
                    )
                    binding.messageField.text.clear()
                    scrollToLastMessage()
                }
            }
        }
    }

    private fun showEmojiBottomSheet(messageId: Int) {
        val bottomSheetDialogView = layoutInflater.inflate(R.layout.emoji_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetDialogView)
        val emojiRecyclerView = bottomSheetDialogView.findViewById<RecyclerView>(R.id.emoji_recycler_view)
        emojiRecyclerView.apply {
            setHasFixedSize(true)
            adapter = emojiAdapter
        }
        bottomSheetDialog.behavior.maxHeight = 1000
        emojiAdapter.onEmojiClickListener = { emoji ->
            store.accept(
                ChatEvent.Ui.SendReaction(
                    messageId = messageId,
                    emojiCode = emoji.emojiCode,
                    emojiName = emoji.emojiName
                )
            )
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun addCallbackToOnBackPressed() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    store.accept(
                        ChatEvent.Ui.ClosingChat(
                            streamName = args.streamName,
                            topicName = args.topicName
                        )
                    )
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            })
    }

    private fun scrollToLastMessage() {
        lifecycleScope.launch {
            scrollTheEnd(chatAdapter)
        }
    }

    private fun scrollTheEnd(chatAdapter: MainAdapter) {
        binding.chatRecyclerView.smoothScrollToPosition(chatAdapter.itemCount)
    }

    private fun showSendFileButton() {
        with(binding) {
            sendFileButton.isVisible = true
            sendMessageButton.isVisible = false
        }
    }

    private fun showSendMessageButton() {
        with(binding) {
            sendMessageButton.isVisible = true
            sendFileButton.isVisible = false
        }
    }

}