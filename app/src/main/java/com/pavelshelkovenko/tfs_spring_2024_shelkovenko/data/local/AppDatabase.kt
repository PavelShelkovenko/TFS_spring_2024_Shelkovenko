package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.AccountSettingsDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.ChatDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.StreamDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.TopicDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.UserDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.AccountSettingsDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.MessageDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.StreamDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.TopicDbo
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.models.UserDbo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(
    entities = [UserDbo::class, StreamDbo::class, MessageDbo::class, TopicDbo::class, AccountSettingsDbo::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun streamDao(): StreamDao

    abstract fun chatDao(): ChatDao

    abstract fun topicDao(): TopicDao

    abstract fun accountSettingsDao(): AccountSettingsDao

    companion object {

        const val USERS_TABLE_NAME = "users"
        const val STREAMS_TABLE_NAME = "streams"
        const val MESSAGES_TABLE_NAME = "messages"
        const val TOPICS_TABLE_NAME = "topics"
        const val ACCOUNT_SETTINGS_TABLE_NAME = "account_settings"
        private const val DB_NAME = "shelkovenko.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .addCallback(
                        AppDatabaseCallback(
                            CoroutineScope(SupervisorJob() + Dispatchers.IO)
                        )
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val coroutineScope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                coroutineScope.launch {
                    database.accountSettingsDao().insert(AccountSettingsDbo())
                }
            }
        }
    }

}