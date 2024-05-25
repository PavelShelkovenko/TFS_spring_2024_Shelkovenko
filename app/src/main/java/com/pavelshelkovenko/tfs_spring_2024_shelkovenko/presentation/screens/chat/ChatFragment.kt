package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
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
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.AccountInfo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentChatBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.ElmBaseFragment
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.delegate_adapter.DelegateItem
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.delegate_adapter.MainAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.date.MessageDateTimeAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.reaction.EmojiAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.reaction.EmojiFactory
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.received_message.ReceivedMessageAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.message.send_message.SendMessageAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.topic_chooser.TopicChooserAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.getApplication
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.showErrorToast
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.showKeyboard
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

    @Inject
    lateinit var accountInfo: AccountInfo

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

    private val topicChooserAdapter by lazy {
        MainAdapter.Builder()
            .add(TopicChooserAdapter())
            .build()
    }

    private val bottomSheetDialog: BottomSheetDialog by lazy {
        BottomSheetDialog(requireActivity(), R.style.EmojiBottomSheetTheme)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            store.accept(
                ChatEvent.Ui.StartProcess(
                    streamName = args.streamName,
                    topicName = args.topicName
                )
            )
        }
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        initRecyclerView()
        setupClickListeners()
        binding.messageField.addTextChangedListener { editable ->
            editable?.let {
                var isInEditMode = false
                if (store.states.value is ChatState.Content) {
                    isInEditMode =
                        (store.states.value as ChatState.Content).editModeState.isInEditMode
                }
                showCorrectSendButton(isInEditMode)
            }
        }
        with(binding) {
            streamName.text = args.streamName
            topicName.text =
                requireActivity().resources.getString(R.string.topic_prefix, args.topicName)
            topicChooserButton.text = requireActivity().resources.getString(
                R.string.topic_chooser_prefix,
                args.topicName
            )
        }
        addCallbackToOnBackPressed()
    }

    override fun handleEffect(effect: ChatEffect) {
        when (effect) {
            is ChatEffect.MinorError -> {
                showErrorToast(effect.errorMessageId, requireActivity())
            }

            is ChatEffect.NewMessageReceived -> {
                scrollToLastMessage()
            }

            is ChatEffect.CloseChat -> {
                findNavController().popBackStack()
            }

            is ChatEffect.OpenTopicChooser -> {
                showTopicChooserBottomSheet(topics = effect.topics)
            }

            is ChatEffect.DeactivateEditingMode -> {
                with(binding) {
                    editInfoHolder.isVisible = false
                    editMessageButton.isVisible = false
                    messageField.text.clear()
                }
            }
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
                    if (state.editModeState.isInEditMode) {
                        showEditingInfo(
                            editingMessageId = state.editModeState.editingMessageId,
                            oldMessageContent = state.editModeState.oldMessageContent,
                            newMessageContent = state.editModeState.newMessageContent
                        )
                    }
                }

                is ChatState.Error -> {
                    shimmerContainer.isVisible = false
                    chatRecyclerView.isVisible = false
                    errorContainer.isVisible = true
                    errorComponent.errorMessage.text =
                        requireActivity().resources.getString(state.errorMessageId)
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

    override fun onStop() {
        super.onStop()
        if (requireActivity().isChangingConfigurations) {
            if (store.states.value is ChatState.Content) {
                store.accept(
                    ChatEvent.Ui.SaveNewEditingMessageContent(
                        binding.messageField.text.trim().toString()
                    )
                )
            }
        } else {
            store.accept(
                ChatEvent.Ui.ClosingChat(
                    streamName = args.streamName,
                    topicName = args.topicName
                )
            )
        }
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

    private fun showEditingInfo(
        editingMessageId: Int,
        oldMessageContent: String,
        newMessageContent: String,
    ) {
        with(binding) {
            editInfoHolder.isVisible = true
            editMessageButton.isVisible = true
            sendMessageButton.isVisible = false
            messageContentForEditInfo.text = oldMessageContent
            editMessageButton.setOnClickListener {
                store.accept(
                    ChatEvent.Ui.EditMessage(
                        newMessageContent = messageField.text.trim().toString(),
                        messageId = editingMessageId,
                    )
                )
            }
            with(messageField) {
                requestFocusFromTouch()
                setText(newMessageContent)
                setSelection(newMessageContent.length)
                // Микрозадержка, чтобы клавиатура могла открыться (конфликтует с bottomSheet)
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(100)
                    showKeyboard()
                }
            }
        }
    }

    private fun setupClickListeners() {

        val receivedMessageAdapter = chatAdapter.delegates.get(1) as ReceivedMessageAdapter
        val sendMessageAdapter = chatAdapter.delegates.get(2) as SendMessageAdapter

        with(receivedMessageAdapter) {
            localUserId = accountInfo.userId
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
            localUserId = accountInfo.userId
            onMessageLongClickListener = { messageId, messageContent ->
                showMessageOptionsBottomSheet(messageId, messageContent)
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
                            topicName = binding.topicChooserButton.text.toString().drop(1)
                        )
                    )
                    binding.messageField.text.clear()
                    scrollToLastMessage()
                }
            }
            cancelEditModeButton.setOnClickListener {
                store.accept(ChatEvent.Ui.DeactivateEditingMode)
            }
            topicChooserButton.setOnClickListener {
                store.accept(ChatEvent.Ui.OnTopicChooserClick(args.streamId))
            }
        }
    }

    private fun showEmojiBottomSheet(messageId: Int) {
        val bottomSheetDialogView = layoutInflater.inflate(R.layout.emoji_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetDialogView)
        val emojiRecyclerView =
            bottomSheetDialogView.findViewById<RecyclerView>(R.id.emoji_recycler_view)
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
            bottomSheetDialog.cancel()
        }
        bottomSheetDialog.show()
    }

    private fun showMessageOptionsBottomSheet(messageId: Int, messageContent: String) {
        val bottomSheetDialogView =
            layoutInflater.inflate(R.layout.message_options_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetDialogView)
        with(bottomSheetDialogView) {
            findViewById<LinearLayout>(R.id.add_reaction_container).setOnClickListener {
                bottomSheetDialog.cancel()
                showEmojiBottomSheet(messageId)
            }
            findViewById<LinearLayout>(R.id.copy_container).setOnClickListener {
                bottomSheetDialog.cancel()
                copyToClipboard(messageContent)
            }
            findViewById<LinearLayout>(R.id.edit_container).setOnClickListener {
                bottomSheetDialog.cancel()
                store.accept(
                    ChatEvent.Ui.ActivateEditingMode(
                        editingMessageId = messageId,
                        oldMessageContent = messageContent,
                        newMessageContent = messageContent,
                    )
                )
            }
            findViewById<LinearLayout>(R.id.delete_container).setOnClickListener {
                bottomSheetDialog.cancel()
                store.accept(ChatEvent.Ui.DeleteMessage(messageId = messageId))
            }
        }
        bottomSheetDialog.show()
    }

    private fun showTopicChooserBottomSheet(topics: List<DelegateItem>) {
        val bottomSheetDialogView =
            layoutInflater.inflate(R.layout.topic_chooser_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetDialogView)
        val topicRecyclerView =
            bottomSheetDialogView.findViewById<RecyclerView>(R.id.topic_chooser_recycler_view)
        topicRecyclerView.apply {
            setHasFixedSize(true)
            adapter = topicChooserAdapter
        }
        (topicChooserAdapter.delegates.get(0) as TopicChooserAdapter).onTopicClickListener =
            { topic ->
                binding.topicChooserButton.text = requireActivity().resources.getString(
                    R.string.topic_chooser_prefix,
                    topic.name
                )
                bottomSheetDialog.cancel()
            }
        topicChooserAdapter.submitList(topics)
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
        binding.chatRecyclerView.smoothScrollToPosition(chatAdapter.itemCount + 1)
    }

    private fun showCorrectSendButton(isInEditMode: Boolean) {
        if (binding.messageField.text.isBlank()) {
            showSendFileButton()
        } else {
            showSendOrEditMessageButton(isInEditMode)
        }
    }

    private fun showSendFileButton() {
        with(binding) {
            sendFileButton.isVisible = true
            sendMessageButton.isVisible = false
            editMessageButton.isVisible = false
        }
    }

    private fun showSendOrEditMessageButton(isInEditMode: Boolean) {
        if (isInEditMode) {
            with(binding) {
                editMessageButton.isVisible = true
                sendMessageButton.isVisible = false
                sendFileButton.isVisible = false
            }
        } else {
            with(binding) {
                sendMessageButton.isVisible = true
                sendFileButton.isVisible = false
                editMessageButton.isVisible = false
            }
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText(text, text))
        Toast.makeText(context, "Message copied to clipboard.", Toast.LENGTH_SHORT).show()
    }

}