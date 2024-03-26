package com.pavelshelkovenko.tfs_spring_2024_shelkovenko

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.ChatAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.EmojiAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.date.DateAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.message.received_message.ReceivedMessageAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.chat_adapters.message.send_message.SendMessageAdapter
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.ActivityMainBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.homework_3.TestViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val chatAdapter by lazy {
        ChatAdapter.Builder()
            .add(DateAdapter())
            .add(ReceivedMessageAdapter())
            .add(SendMessageAdapter())
            .build()
    }

    private val emojiAdapter by lazy {
        EmojiAdapter(EmojiFactory.getEmojiList())
    }

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw RuntimeException("ActivityMainBinding == null")

    private lateinit var viewModel: TestViewModel    // Будет в будущем через DI сделано

    private val bottomSheetDialog: BottomSheetDialog by lazy {
        BottomSheetDialog(this, R.style.EmojiBottomSheetTheme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[TestViewModel::class.java]
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        binding.chatRv.adapter = chatAdapter
        setupClickListeners()

        binding.messageField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.processMessageFieldChanges(s.toString())
            }

        })

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isMessageTextFieldEmpty.collect { isMessageTextFieldEmpty ->
                    if (isMessageTextFieldEmpty) {
                        binding.sendMessageButton.visibility = View.GONE
                        binding.sendFileButton.visibility = View.VISIBLE
                    } else {
                        binding.sendMessageButton.visibility = View.VISIBLE
                        binding.sendFileButton.visibility = View.GONE
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stubMessages.collect { newList ->
                    chatAdapter.submitList(newList)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        scrollTheEnd(chatAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun scrollTheEnd(chatAdapter: ChatAdapter) {
        binding.chatRv.smoothScrollToPosition(chatAdapter.itemCount)
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
        val emojiRecyclerView = bottomSheetDialogView.findViewById<RecyclerView>(R.id.emoji_recycler_view)
        emojiRecyclerView.setHasFixedSize(true)
        emojiRecyclerView.adapter = emojiAdapter
        bottomSheetDialog.behavior.maxHeight = 1000
        emojiAdapter.onEmojiClickListener = { emojiCode ->
            viewModel.addEmoji(messageId, emojiCode)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }
}