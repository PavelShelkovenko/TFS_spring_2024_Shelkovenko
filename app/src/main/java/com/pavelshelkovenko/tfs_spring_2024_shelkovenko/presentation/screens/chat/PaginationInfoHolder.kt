package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat

data class PaginationInfoHolder(
   var isLoadingPagingData: Boolean = false,
   var hasLoadedNewestMessage: Boolean = false,
   var hasLoadedOldestMessage: Boolean = false,
   var oldestAnchor: String = "0",
   var newestAnchor: String = "first_unread"
)