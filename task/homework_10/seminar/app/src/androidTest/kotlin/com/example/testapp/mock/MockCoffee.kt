package com.example.testapp.mock

import com.example.testapp.util.AssetsUtils.fromAssets
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.github.tomakehurst.wiremock.client.WireMock.verify

class MockCoffee(private val wireMockServer: WireMockServer) {

    private val matcher = WireMock.get(urlPattern)

    fun withSingleCoffee() {
        wireMockServer.stubFor(matcher.willReturn(ok(fromAssets("coffee/singleCoffee.json"))))
    }

    fun withEmptyList() {
        wireMockServer.stubFor(matcher.willReturn(ok("[]")))
    }

    companion object {

        val urlPattern = urlPathMatching("/coffee/.+")

        fun WireMockServer.coffee(block: MockCoffee.() -> Unit) {
            MockCoffee(this).apply(block)
        }
    }
}
