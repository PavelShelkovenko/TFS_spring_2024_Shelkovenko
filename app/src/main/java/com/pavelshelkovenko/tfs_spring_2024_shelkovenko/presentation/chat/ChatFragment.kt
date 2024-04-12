package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentChatBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.delegate_adapter.MainAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.date.MessageDateTimeAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.reaction.EmojiAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.reaction.EmojiFactory
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.received_message.ReceivedMessageAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.send_message.SendMessageAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.stackFromEnd = true
        binding.chatRv.layoutManager = linearLayoutManager
        binding.chatRv.adapter = chatAdapter

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
            topicName.text = args.topicName
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.stubMessages.collect { newList ->
                    chatAdapter.submitList(newList)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListeners() {

        val receivedMessageAdapter = chatAdapter.delegates.get(1) as ReceivedMessageAdapter
        val sendMessageAdapter = chatAdapter.delegates.get(2) as SendMessageAdapter

        with(receivedMessageAdapter) {
            localUser = viewModel.localUser
            onMessageLongClickListener = { messageId ->
                showEmojiBottomSheet(messageId)
            }
            onAddIconClickListener = { messageId ->
                showEmojiBottomSheet(messageId)
            }
            onEmojiClickListener = { messageId, emojiCode ->
                viewModel.changeEmojiStatus(messageId, emojiCode)
            }
        }

        with(sendMessageAdapter) {
            localUser = viewModel.localUser
            onMessageLongClickListener = { messageId ->
                showEmojiBottomSheet(messageId)
            }
            onAddIconClickListener = { messageId ->
                showEmojiBottomSheet(messageId)
            }
            onEmojiClickListener = { messageId, emojiCode ->
                viewModel.changeEmojiStatus(messageId, emojiCode)
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.sendMessageButton.setOnClickListener {
            if (binding.messageField.text.toString() != "") {
                viewModel.addMessage()
                binding.messageField.setText("")
                scrollTheEnd(chatAdapter)
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
        emojiAdapter.onEmojiClickListener = { emojiCode ->
            viewModel.addEmoji(messageId, emojiCode)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun scrollTheEnd(chatAdapter: MainAdapter) {
        binding.chatRv.smoothScrollToPosition(chatAdapter.itemCount)
    }
}