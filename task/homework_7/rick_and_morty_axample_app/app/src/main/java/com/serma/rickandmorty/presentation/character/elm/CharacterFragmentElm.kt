package com.serma.rickandmorty.presentation.character.elm

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.serma.rickandmorty.R
import com.serma.rickandmorty.databinding.FragmentCharacterBinding
import com.serma.rickandmorty.di.DiContainer
import com.serma.rickandmorty.presentation.character.model.CharacterStatusUi
import com.serma.rickandmorty.presentation.elm.ElmBaseFragment
import com.serma.rickandmorty.presentation.mvi.MviStore
import com.serma.rickandmorty.presentation.utils.LceState
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store

class CharacterFragmentElm :
    ElmBaseFragment<
            CharacterEffectElm,
            CharacterStateElm,
            CharacterEvent
            >(R.layout.fragment_character) {

    private val binding: FragmentCharacterBinding by viewBinding(FragmentCharacterBinding::bind)
    override val store: Store<
            CharacterEvent,
            CharacterEffectElm,
            CharacterStateElm
            > by elmStoreWithRenderer(elmRenderer = this) {
        DiContainer.storeFactory.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            store.accept(CharacterEvent.Ui.Init)
        }
    }

    override fun handleEffect(effect: CharacterEffectElm){
        when (effect) {
            is CharacterEffectElm.ShowError -> showErrorDialog(effect.throwable.message)
        }
    }

    override fun render(state: CharacterStateElm) {
        when (val dataToRender = state.personUi) {
            is LceState.Content -> {
                val data = dataToRender.content
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
                    type.text =
                        getString(R.string.type, getString(data.status.statusRes), data.species)
                    firstSeenIn.text = getString(R.string.first_seen_in, data.firstSeenIn)
                    lastSeenLocation.text =
                        getString(R.string.last_seen_location, data.lastLocation)
                    prevItem.isVisible = state.page != 0
                    prevItem.setOnClickListener {
                        store.accept(CharacterEvent.Ui.PrevPage)
                    }
                    nextItem.setOnClickListener {
                        store.accept(CharacterEvent.Ui.NextPage)
                    }
                }
            }

            is LceState.Error -> Unit
            LceState.Loading -> Unit
        }
    }

    private fun showErrorDialog(errorMessage: String?) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error_title)
            .setMessage(errorMessage)
            .setPositiveButton(getText(R.string.retry_button)) { dialog, which ->
                store.accept(CharacterEvent.Ui.ReloadPage)
            }
            .setCancelable(false)
            .show()
    }
}