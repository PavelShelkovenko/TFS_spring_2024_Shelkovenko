package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response.GetAllStreamsResponse
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response.GetAllUserPresenceResponse
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response.GetAllUsersResponse
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response.GetMessageEventResponse
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response.GetMessagesResponse
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response.GetOwnProfileResponse
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response.GetReactionEventResponse
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response.GetSubscribedStreamsResponse
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response.GetTopicsResponse
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response.GetUserPresenceResponse
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response.GetUserResponse
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.remote.models.response.RegisterEventsResponse
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ZulipApi {

    @GET("streams")
    suspend fun getAllStreams(): GetAllStreamsResponse

    @GET("users/me/{stream_id}/topics")
    suspend fun getTopics(@Path("stream_id") streamId: Int): GetTopicsResponse

    @GET("messages")
    suspend fun getMessages(
        @Query("anchor") anchor: String,
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("narrow") narrow: JSONArray,
        @Query("apply_markdown") applyMarkdown: Boolean = false,
        @Query("client_gravatar") clientGravatar: Boolean = false,
    ): GetMessagesResponse

    @GET("users/{user_email_or_id}/presence")
    suspend fun getUserPresence(
        @Path("user_email_or_id") emailOrId: String
    ): GetUserPresenceResponse


    @GET("realm/presence")
    suspend fun getAllUsersPresence(): GetAllUserPresenceResponse

    @GET("users/{user_id}")
    suspend fun getUser(
        @Path("user_id") userId: Int,
        @Query("client_gravatar") clientGravatar: Boolean = false,
    ): GetUserResponse

    @GET("users")
    suspend fun getAllUsers(
        @Query("client_gravatar") clientGravatar: Boolean = false,
    ): GetAllUsersResponse

    @GET("users/me")
    suspend fun getOwnProfile(): GetOwnProfileResponse

    @GET("users/me/subscriptions")
    suspend fun getSubscribedStreams(): GetSubscribedStreamsResponse

    @POST("messages")
    suspend fun sendMessage(
        @Query("type") type: String = "stream",
        @Query("to") streamName: String,
        @Query("topic") topicName: String,
        @Query("content") message: String
    )

    @POST("messages/{message_id}/reactions")
    suspend fun sendReaction(
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String,
        @Query("emoji_code") emojiCode: String,
    )

    @DELETE("messages/{message_id}/reactions")
    suspend fun removeReaction(
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String,
        @Query("emoji_code") emojiCode: String,
    )

    @POST("register")
    suspend fun registerMessageEvents(
        @Query("narrow") narrow: JSONArray,
        @Query("event_types") eventTypes: JSONArray = JSONArray().put("message")
    ): RegisterEventsResponse

    @GET("events")
    suspend fun getMessageEvents(
        @Query("queue_id") queueId: String,
        @Query("last_event_id") lastEventId: String?
    ): GetMessageEventResponse

    @POST("register")
    suspend fun registerReactionEvents(
        @Query("narrow") narrow: JSONArray,
        @Query("event_types") eventTypes: JSONArray = JSONArray().put("reaction")
    ): RegisterEventsResponse


    @GET("events")
    suspend fun getReactionEvents(
        @Query("queue_id") queueId: String,
        @Query("last_event_id") lastEventId: String
    ): GetReactionEventResponse
}

fun ZulipApi(
    baseUrl: String = "https://tinkoff-android-spring-2024.zulipchat.com/api/v1/",
    okHttpClient: OkHttpClient? = null,
): ZulipApi {
    return retrofit(
        baseUrl = baseUrl,
        okHttpClient = okHttpClient
    ).create()
}

private fun retrofit(
    baseUrl: String,
    okHttpClient: OkHttpClient?
): Retrofit {

    val modifiedOkHttpClient: OkHttpClient = (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .header(
                        name = "Authorization",
                        value = Credentials.basic(
                            "pavel.shelkovenko@gmail.com",
                            "PIqWnpOVj5pqafJQFefbu1Rd3yMwyQil"
                        )
                    )
                    .build()
            )
        }
        .addNetworkInterceptor(
            HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        )
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(modifiedOkHttpClient)
        .build()
}