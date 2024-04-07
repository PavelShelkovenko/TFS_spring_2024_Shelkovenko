package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentChannelBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.channels.streams.StreamsInfoFragment

class ChannelFragment : Fragment() {

    private var _binding: FragmentChannelBinding? = null
    private val binding: FragmentChannelBinding
        get() = _binding ?: throw RuntimeException("FragmentChannelBinding == null")


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
                StreamsInfoFragment.newInstance(StreamsInfoFragment.SUBSCRIBED_STREAMS_DESTINATION),
                StreamsInfoFragment.newInstance(StreamsInfoFragment.ALL_STREAMS_DESTINATION),
            )
        )
        TabLayoutMediator(binding.tabLayout, binding.channelViewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        binding.searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val str = s.toString()
                if (str.isBlank()) {
                    binding.questionMarkButton.visibility = View.VISIBLE
                    binding.cancelButton.visibility = View.GONE
                } else {
                    binding.questionMarkButton.visibility = View.GONE
                    binding.cancelButton.visibility = View.VISIBLE
                }
            }
        })

        binding.cancelButton.setOnClickListener {
            binding.searchField.setText("")
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}