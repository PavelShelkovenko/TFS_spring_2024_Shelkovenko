package com.example.testapp.feature_list.presentation

import com.example.testapp.feature_list.presentation.FeatureListCommand.LoadList
import com.example.testapp.feature_list.presentation.FeatureListEvent.FeatureListUiEvent.OnButtonClick
import com.example.testapp.feature_list.presentation.model.FeatureListStatus.LOADING
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FeatureListReducerTestJUnit {

    @Test
    fun `reduce WHEN status initial AND event OnButtonClick THEN status loading AND commands should contain LoadList`() {
        // Given
        val category = "category"
        val reducer = FeatureListReducer()
        val state = FeatureListState()
        val event = OnButtonClick(category)
        // When
        val actual = reducer.reduce(event, state)
        // Then
        assertEquals(LOADING, actual.state.status)
        assertTrue(actual.commands.contains(LoadList(category)))
        assertEquals(listOf(LoadList(category)), actual.commands)
    }
}
