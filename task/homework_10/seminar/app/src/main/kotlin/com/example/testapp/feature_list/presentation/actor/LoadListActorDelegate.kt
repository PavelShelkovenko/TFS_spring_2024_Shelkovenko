package com.example.testapp.feature_list.presentation.actor

import com.example.testapp.domain_list.repository.CoffeeRepository
import com.example.testapp.feature_list.presentation.FeatureListCommand
import com.example.testapp.feature_list.presentation.FeatureListCommand.LoadList
import com.example.testapp.feature_list.presentation.FeatureListEvent.FeatureListInternalEvent
import com.example.testapp.feature_list.presentation.FeatureListEvent.FeatureListInternalEvent.OnListLoadFailed
import com.example.testapp.feature_list.presentation.FeatureListEvent.FeatureListInternalEvent.OnListLoaded
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class LoadListActorDelegate(
    private val coffeeRepository: CoffeeRepository
) : Actor<LoadList, FeatureListInternalEvent>() {

    override fun execute(command: LoadList): Flow<FeatureListInternalEvent> {
        return flow { emit(coffeeRepository.getCoffee(command.category)) }
            .mapEvents(
                eventMapper = { OnListLoaded(it) },
                errorMapper = {
                    it.printStackTrace()
                    OnListLoadFailed }
            )
    }
}
