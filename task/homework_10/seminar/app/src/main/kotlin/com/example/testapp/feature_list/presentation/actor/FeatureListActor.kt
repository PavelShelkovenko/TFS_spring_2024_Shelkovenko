package com.example.testapp.feature_list.presentation.actor

import com.example.testapp.feature_list.presentation.FeatureListCommand
import com.example.testapp.feature_list.presentation.FeatureListCommand.LoadList
import com.example.testapp.feature_list.presentation.FeatureListEvent.FeatureListInternalEvent
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.core.store.Actor

class FeatureListActor(
    private val loadListActorDelegate: LoadListActorDelegate
) : Actor<FeatureListCommand, FeatureListInternalEvent>() {

    override fun execute(command: FeatureListCommand): Flow<FeatureListInternalEvent> {
        return when (command) {
            is LoadList -> loadListActorDelegate.execute(command)
        }
    }
}
