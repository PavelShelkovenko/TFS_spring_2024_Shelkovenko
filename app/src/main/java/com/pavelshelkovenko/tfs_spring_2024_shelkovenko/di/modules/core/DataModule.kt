package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules.core

import android.content.Context
import androidx.room.Room
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.BuildConfig
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.AppDatabase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.ChatDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.StreamDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.local.dao.UserDao
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.ZulipApi
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.utils.NarrowBuilderHelper
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

@Module
class DataModule {

    @ApplicationScope
    @Provides
    fun providesZulipApi(retrofitClient: Retrofit): ZulipApi = retrofitClient.create()

    @ApplicationScope
    @Provides
    fun providesDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @ApplicationScope
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @ApplicationScope
    @Provides
    fun provideStreamDao(db: AppDatabase): StreamDao = db.streamDao()

    @ApplicationScope
    @Provides
    fun provideChatDao(db: AppDatabase): ChatDao = db.chatDao()

    @ApplicationScope
    @Provides
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @ApplicationScope
    @Provides
    fun provideOkHttpClient(
        interceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .addNetworkInterceptor(loggingInterceptor)
        .build()

    @ApplicationScope
    @Provides
    fun provideInterceptor(): Interceptor = Interceptor { chain ->
        val request: Request = chain.request().newBuilder()
            .header(
                "Authorization",
                Credentials.basic(USERNAME, PASSWORD)
            )
            .build()
        chain.proceed(request)
    }

    @ApplicationScope
    @Provides
    fun provideNetworkInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    @ApplicationScope
    @Provides
    fun provideNarrowBuilderHelper(): NarrowBuilderHelper = NarrowBuilderHelper()

    companion object {

        //Эти константы по идее должны из BuildConfig браться

        private const val USERNAME = "pavel.shelkovenko@gmail.com"
        private const val PASSWORD = "PIqWnpOVj5pqafJQFefbu1Rd3yMwyQil"
    }
}