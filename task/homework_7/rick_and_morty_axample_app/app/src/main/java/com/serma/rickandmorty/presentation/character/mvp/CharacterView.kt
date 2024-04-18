package com.serma.rickandmorty.presentation.character.mvp

import com.serma.rickandmorty.presentation.character.model.PersonUi
import com.serma.rickandmorty.presentation.mvp.BaseView

interface CharacterView: BaseView {
    fun render(data: PersonUi)

    fun showErrorDialog(errorMessage: String?, page: Int)
}