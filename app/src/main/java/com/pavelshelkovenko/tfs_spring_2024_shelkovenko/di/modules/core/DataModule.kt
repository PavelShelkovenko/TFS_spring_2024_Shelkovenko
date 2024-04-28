package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.di.modules.core

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.ZulipApi
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
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
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
        private const val BASE_URL = "https://tinkoff-android-spring-2024.zulipchat.com/api/v1/"
        private const val USERNAME = "pavel.shelkovenko@gmail.com"
        private const val PASSWORD = "PIqWnpOVj5pqafJQFefbu1Rd3yMwyQil"
    }
}