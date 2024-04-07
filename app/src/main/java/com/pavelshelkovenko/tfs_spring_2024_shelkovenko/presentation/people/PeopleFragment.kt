package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.people

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentPeopleBinding
import kotlinx.coroutines.launch

class PeopleFragment : Fragment() {


    private var _binding: FragmentPeopleBinding? = null
    private val binding: FragmentPeopleBinding
        get() = _binding ?: throw RuntimeException("FragmentPeopleBinding == null")

    private lateinit var viewModel: PeopleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[PeopleViewModel::class.java]
        val peopleAdapter = PeopleAdapter()
        binding.peopleRv.adapter = peopleAdapter

        binding.cancelButton.setOnClickListener {
            binding.searchField.setText("")
        }

        peopleAdapter.onUserClickListener = { userName ->
            findNavController().navigate(
                PeopleFragmentDirections.actionPeopleFragmentToAnotherProfileFragment(userName)
            )
        }

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.userList.collect { newUserList ->
                    peopleAdapter.submitList(newUserList)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}