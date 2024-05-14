package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.mock


import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.github.tomakehurst.wiremock.matching.UrlPathPattern
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.AssetsUtils.fromAssets

class MockZulip(private val wireMockServer: WireMockServer) {

    private val messageMatcher = WireMock.get(urlPatternForMessages)
    private val subscribedStreamsMatcher = WireMock.get(urlPatternForSubscribedStreams)
    private val topicsMatcher = WireMock.get(urlPatterForTopics)
    private val ownProfileMatcher = WireMock.get(urlPatterForOwnProfile)
    private val userOnlineStatusMatcher = WireMock.get(urlPatterForUserOnlineStatus)
    private val messageEventMatcher = WireMock.get(urlPathPatternForMessageEvent)
    private val registrationForEventMatcher = WireMock.post(urlPathPatternForRegistrationForEvents)
    private val sendMessageMatcher = WireMock.post(urlPathPatternForSendMessage)


    fun stubTestMessages() {
        wireMockServer.stubFor(messageMatcher.willReturn(ok(fromAssets("zulip/testMessages.json"))))
    }

    fun stubRegisterEvent() {
        wireMockServer.stubFor(registrationForEventMatcher.willReturn(ok(fromAssets("zulip/testRegisterForEvents.json"))))
        wireMockServer.stubFor(sendMessageMatcher.willReturn(ok("[]")))
    }

    fun stubMessageEvent() {
        wireMockServer.stubFor(
            messageEventMatcher.willReturn(
                ResponseDefinitionBuilder()
                    .withFixedDelay(5000)
                    .withStatus(200)
                    .withBody(fromAssets("zulip/testMessageEvent.json"))
            )
        )
    }

    fun stubSubscribedStreams() {
        wireMockServer.stubFor(subscribedStreamsMatcher.willReturn(ok(fromAssets("zulip/testSubscribedStreams.json"))))
        wireMockServer.stubFor(topicsMatcher.willReturn(ok(fromAssets("zulip/testTopics.json"))))
    }

    fun stubOwnProfile() {
        wireMockServer.stubFor(ownProfileMatcher.willReturn(ok(fromAssets("zulip/testOwnUser.json"))))
        wireMockServer.stubFor(userOnlineStatusMatcher.willReturn(ok(fromAssets("zulip/testUserOnlineStatus.json"))))
    }

    companion object {

        val urlPatternForMessages: UrlPathPattern = urlPathMatching("/messages.*")

        val urlPatternForSubscribedStreams: UrlPathPattern = urlPathMatching("/users/me/subscriptions")

        val urlPatterForTopics: UrlPathPattern = urlPathMatching("/.*/topics")

        val urlPatterForOwnProfile: UrlPathPattern = urlPathMatching("/users/me")

        val urlPatterForUserOnlineStatus: UrlPathPattern = urlPathMatching("/.*/presence")

        val urlPathPatternForMessageEvent: UrlPathPattern = urlPathMatching("/events.*")

        val urlPathPatternForRegistrationForEvents: UrlPathPattern = urlPathMatching("/register.*")

        val urlPathPatternForSendMessage: UrlPathPattern = urlPathMatching("/messages?.*content=Hello")

        fun WireMockServer.zulip(block: MockZulip.() -> Unit) {
            MockZulip(this).apply(block)
        }
    }
}
