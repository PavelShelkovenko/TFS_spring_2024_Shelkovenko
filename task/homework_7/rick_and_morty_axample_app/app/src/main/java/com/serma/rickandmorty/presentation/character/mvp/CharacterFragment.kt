package com.serma.rickandmorty.presentation.character.mvp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.serma.rickandmorty.R
import com.serma.rickandmorty.databinding.FragmentCharacterBinding
import com.serma.rickandmorty.di.DiContainer
import com.serma.rickandmorty.presentation.character.model.CharacterStatusUi
import com.serma.rickandmorty.presentation.character.model.PersonUi
import com.serma.rickandmorty.presentation.mvp.BaseFragment

class CharacterFragment :
    BaseFragment<CharacterView, CharacterPresenter>(R.layout.fragment_character), CharacterView {

    private val binding: FragmentCharacterBinding by viewBinding(FragmentCharacterBinding::bind)

    override fun createPresenter(): CharacterPresenter = DiContainer.characterPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            getPresenter().loadData(0)
        }
    }


    override fun render(data: PersonUi) {
        with(binding) {
            Glide.with(avatar).load(data.imageUrl).into(avatar)
            name.text = data.name
            status.setImageResource(
                when (data.status) {
                    is CharacterStatusUi.Alive -> R.drawable.alive_ic
                    is CharacterStatusUi.Dead -> R.drawable.dead_ic
                    is CharacterStatusUi.Unknown -> R.drawable.unknown_ic
                }
            )
            type.text = getString(R.string.type, getString(data.status.statusRes), data.species)
            firstSeenIn.text = getString(R.string.first_seen_in, data.firstSeenIn)
            lastSeenLocation.text = getString(R.string.last_seen_location, data.lastLocation)
//            prevItem.isVisible = data.page != 0
//            prevItem.setOnClickListener {
//                getPresenter().loadData(data.page - 1)
//            }
//            nextItem.setOnClickListener {
//                getPresenter().loadData(data.page + 1)
//            }
        }
    }

    override fun showErrorDialog(errorMessage: String?, page: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error_title)
            .setMessage(errorMessage)
            .setPositiveButton(getText(R.string.retry_button)) { dialog, which ->
                getPresenter().loadData(page)
            }
            .setCancelable(false)
            .show()
    }
}