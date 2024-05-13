package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.ChatDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.StreamDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.UserDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.MessageDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.StreamDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.UserDbo

@Database(
    entities = [UserDbo::class, StreamDbo::class, MessageDbo::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun streamDao(): StreamDao

    abstract fun chatDao(): ChatDao

    companion object {
        const val USERS_TABLE_NAME = "users"
        const val STREAMS_TABLE_NAME = "streams"
        const val MESSAGES_TABLE_NAME = "messages"
        const val DB_NAME = "shelkovenko.db"
    }
}