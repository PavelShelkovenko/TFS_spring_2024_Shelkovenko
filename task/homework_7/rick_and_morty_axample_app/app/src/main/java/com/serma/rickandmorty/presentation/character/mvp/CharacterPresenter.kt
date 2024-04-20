package com.serma.rickandmorty.presentation.character.mvp

import com.serma.rickandmorty.domain.RandomCharacterUseCase
import com.serma.rickandmorty.presentation.character.mapper.toPersonUi
import com.serma.rickandmorty.presentation.mvp.BasePresenter
import kotlinx.coroutines.launch

class CharacterPresenter(private val useCase: RandomCharacterUseCase) :
    BasePresenter<CharacterView>() {

    fun loadData(page: Int) {
        coroutineScope.launch {
            runCatching {
                useCase.invoke(page)
            }.fold(
                onSuccess = { data ->
                    runIfViewAvailable { view ->
                        view.render(data.toPersonUi())
                    }
                },
                onFailure = {
                    runIfViewAvailable { view ->
                        view.showErrorDialog(it.message, page)
                    }
                })
        }
    }
}