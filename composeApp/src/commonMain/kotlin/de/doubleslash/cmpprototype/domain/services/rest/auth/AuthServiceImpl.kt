package de.doubleslash.cmpprototype.domain.services.rest.auth

import de.doubleslash.cmpprototype.common.Constants.ACCEPT
import de.doubleslash.cmpprototype.common.Constants.AUTH_API_ENDPOINT
import de.doubleslash.cmpprototype.common.Constants.CONTENT_TYPE
import de.doubleslash.cmpprototype.common.Constants.HEADERS_APPLICATION_TYPE
import de.doubleslash.cmpprototype.common.Constants.HTTPS_PROTOCOL
import de.doubleslash.cmpprototype.common.Constants.HTTP_ERROR_MSG
import de.doubleslash.cmpprototype.common.Constants.PASSWORD
import de.doubleslash.cmpprototype.common.Constants.USERNAME
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.services.rest.auth.dto.LoginDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Handles REST API requests related to user authentication.
 * Sends login and session renewal requests to the server.
 */
class AuthServiceImpl : AuthService {
    private val httpClient = HttpClient {
        // Configure HTTP client
        install(ContentNegotiation) {
            json(Json {
                prettyPrint =
                    true // JSON data is printed in a human-readable format with indents and whitespace
                ignoreUnknownKeys =
                    true // allows the client to handle JSON with extra, unknown fields without failing
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000 // 15 seconds
        }
        install(DefaultRequest) {
            headers {
                append(ACCEPT, HEADERS_APPLICATION_TYPE)
                append(CONTENT_TYPE, HEADERS_APPLICATION_TYPE)
            }
        }
    }

    override suspend fun authenticateUser(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<LoginDTO> {
        return try {
            val baseUrl = HTTPS_PROTOCOL + serverAddress + AUTH_API_ENDPOINT
            val responseFromAPI = httpClient.post(baseUrl) {
                setBody(mapOf(USERNAME to username, PASSWORD to password))
            }

            if (responseFromAPI.status.value == 200) {
                val responseData = Json.decodeFromString<LoginDTO>(responseFromAPI.body())
                RequestCondition.SuccessCondition(data = responseData)
            } else {
                RequestCondition.ErrorCondition(
                    errorMsg = HTTP_ERROR_MSG + responseFromAPI.status
                )
            }

        } catch (error: Exception) {
            RequestCondition.ErrorCondition(error.message.toString())
        }
    }
}