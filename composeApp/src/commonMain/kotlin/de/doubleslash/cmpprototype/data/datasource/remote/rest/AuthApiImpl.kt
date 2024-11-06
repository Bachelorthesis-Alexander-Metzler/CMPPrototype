package de.doubleslash.cmpprototype.data.datasource.remote.rest

import de.doubleslash.cmpprototype.data.datasource.remote.rest.dto.LoginDTO
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
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
class AuthApiImpl: AuthApi {
    private val httpClient = HttpClient {
        // Configure HTTP client
        install(ContentNegotiation){
            json(Json {
                prettyPrint = true // JSON data is printed in a human-readable format with indents and whitespace
                ignoreUnknownKeys = true // allows the client to handle JSON with extra, unknown fields without failing
            })
        }
        install(HttpTimeout){
            requestTimeoutMillis = 15000 // 15 seconds
        }
        install(DefaultRequest){
            headers {
                append("accept", "application/json")
                append("Content-Type", "application/json")
            }
        }
    }

    override suspend fun authenticateUser(
        serverAddress: String,
        username: String,
        password: String): RequestCondition<LoginDTO> {
        return try {
            val endpoint = "https://$serverAddress/rest/v2.0/usersessions"
            val responseFromAPI = httpClient.post(endpoint) {
                setBody(mapOf("username" to username, "password" to password))
            }

            if (responseFromAPI.status.value == 200) {
                println("Response from API=" + responseFromAPI.body<String>())

                val responseData = Json.decodeFromString<LoginDTO>(responseFromAPI.body())
                RequestCondition.SuccessCondition(data = responseData)
            } else {
                RequestCondition.ErrorCondition(errorMsg = "HTTP Error: ${responseFromAPI.status}")
            }

        } catch (error: Exception){
            RequestCondition.ErrorCondition(error.message.toString())
        }
    }
}