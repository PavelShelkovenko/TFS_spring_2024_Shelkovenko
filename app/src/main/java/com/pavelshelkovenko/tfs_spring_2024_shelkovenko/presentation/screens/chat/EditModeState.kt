package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat

data class EditModeState(
    var isInEditMode: Boolean,
    var editingMessageId: Int,
    var oldMessageContent: String,
    var newMessageContent: String,
) {

    companion object {

        fun provideDeactivatedEditModeState(): EditModeState {
            return EditModeState(
                isInEditMode = false,
                editingMessageId = -1,
                oldMessageContent = "",
                newMessageContent = "",
            )
        }
    }

}
