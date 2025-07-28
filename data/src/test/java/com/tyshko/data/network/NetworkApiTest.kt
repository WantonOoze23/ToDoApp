package com.tyshko.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.fail

class NetworkApiTest {

    @Test
    fun getUserIDTest200Response() = runTest{

        val expectedId = "1.1.1.1"
        val mockEngine = MockEngine{ request ->
            assertEquals("https://api.ipify.org/", request.url.toString())
            assertEquals(request.method, HttpMethod.Get)

            respond(
                content = expectedId,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }

        val httpClient = HttpClient(mockEngine)
        val httpMethod = NetworkApi(httpClient)

        val result = httpMethod.getUserIP()

        assertEquals(result, expectedId)
    }

    @Test
    fun getUserIDTest400Response() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = "Bad Request",
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }

        val httpClient = HttpClient(mockEngine) {
            expectSuccess = true
        }

        val httpMethod = NetworkApi(httpClient)

        try {
            httpMethod.getUserIP()
            fail("Expected ClientRequestException was not thrown")
        } catch (e: ClientRequestException) {
            assertEquals(HttpStatusCode.BadRequest, e.response.status)
        }
    }

    @Test
    fun getUserIDTest500Response() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = "Internal Server Error",
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }

        val httpClient = HttpClient(mockEngine) {
            expectSuccess = true
        }

        val httpMethod = NetworkApi(httpClient)

        try {
            httpMethod.getUserIP()
            fail("Expected ServerResponseException was not thrown")
        } catch (e: ServerResponseException) {
            assertEquals(HttpStatusCode.InternalServerError, e.response.status)
        }
    }
}