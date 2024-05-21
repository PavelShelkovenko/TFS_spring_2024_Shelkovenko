package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules


import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.AccountInfo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.scopes.ChatScope
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.ChatRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.ChatActor
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.ChatReducer
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.ChatStoreFactory
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.LongPollingInfoHolder
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.PaginationInfoHolder
import dagger.Module
import dagger.Provides

@Module
class ChatModule {

    @ChatScope
    @Provides
    fun provideLongPollingInfoHolder(): LongPollingInfoHolder = LongPollingInfoHolder()

    @ChatScope
    @Provides
    fun providePaginationInfoHolder(): PaginationInfoHolder = PaginationInfoHolder()

    @ChatScope
    @Provides
    fun provideChatActor(
        chatRepository: ChatRepository
    ): ChatActor = ChatActor(chatRepository)


    @ChatScope
    @Provides
    fun provideChatReducer(
        longPollingInfoHolder: LongPollingInfoHolder,
        paginationInfoHolder: PaginationInfoHolder,
        accountInfo: AccountInfo
    ): ChatReducer = ChatReducer(
        longPollingInfoHolder = longPollingInfoHolder,
        paginationInfoHolder = paginationInfoHolder,
        accountInfo = accountInfo,
    )

    @ChatScope
    @Provides
    fun provideStreamStoreFactory(
        actor: ChatActor,
        reducer: ChatReducer
    ): ChatStoreFactory = ChatStoreFactory(actor, reducer)
}