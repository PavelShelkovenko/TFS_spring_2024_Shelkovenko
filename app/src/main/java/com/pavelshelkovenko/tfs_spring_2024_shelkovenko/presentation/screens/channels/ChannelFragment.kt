package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentChannelBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.StreamDestination
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams.StreamsInfoFragment
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.hideKeyboard
import kotlinx.coroutines.flow.MutableStateFlow


class ChannelFragment : Fragment(R.layout.fragment_channel) {

    private val binding: FragmentChannelBinding by viewBinding(FragmentChannelBinding::bind)

    val searchQueryFlow = MutableStateFlow("")

    private lateinit var pagerAdapter: ChannelPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabs: List<String> = listOf(
            resources.getString(R.string.subscribed),
            resources.getString(R.string.all_streams),
        )
        pagerAdapter = ChannelPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)

        binding.channelViewPager.adapter = pagerAdapter
        pagerAdapter.update(
            listOf(
                StreamsInfoFragment.newInstance(StreamDestination.SUBSCRIBED),
                StreamsInfoFragment.newInstance(StreamDestination.ALL),
            )
        )
        TabLayoutMediator(binding.tabLayout, binding.channelViewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        setupClickListeners()

        with(binding) {
            searchField.addTextChangedListener { editable ->
                editable?.let { query ->
                    searchQueryFlow.tryEmit(query.trim().toString())
                    if (query.isEmpty()) {
                        showQuestionMarkButton()
                    } else {
                        showCancelButton()
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            cancelButton.setOnClickListener {
                binding.searchField.text.clear()
            }
            addStreamButton.setOnClickListener {
                showAddStreamDialog()
            }
        }
    }

    private fun showCancelButton() {
        with(binding) {
            questionMarkButton.isVisible = false
            cancelButton.isVisible = true
        }
    }

    private fun showQuestionMarkButton() {
        with(binding) {
            questionMarkButton.isVisible = true
            cancelButton.isVisible = false
        }
    }

    private fun showAddStreamDialog() {
        val dialog = Dialog(requireActivity(), R.style.AddStreamDialogTheme)
        dialog.apply {
            setContentView(R.layout.dialog_add_stream)
            window?.attributes?.windowAnimations = R.style.animation;
        }
        val createStreamButton = dialog.findViewById<Button>(R.id.create_stream_button)
        createStreamButton.setOnClickListener {
            dialog.findViewById<EditText>(R.id.stream_name_edit_text).hideKeyboard()
            val currentFragment = pagerAdapter.getCurrentFragment(binding.channelViewPager.currentItem)
            val streamName = dialog.findViewById<EditText>(R.id.stream_name_edit_text).text.trim().toString()
            (currentFragment as? StreamsInfoFragment)?.createStream(streamName)
            dialog.cancel()
        }
        dialog.show()
    }
}


