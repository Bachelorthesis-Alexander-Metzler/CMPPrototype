package de.doubleslash.cmpprototype.data.datasource.remote.rest.cmis

import de.doubleslash.cmpprototype.common.Constants
import de.doubleslash.cmpprototype.common.Constants.ACCEPT
import de.doubleslash.cmpprototype.common.Constants.HEADERS_APPLICATION_TYPE
import de.doubleslash.cmpprototype.common.Constants.HTTPS_PROTOCOL
import de.doubleslash.cmpprototype.data.datasource.remote.rest.cmis.dto.CMISObjectDTO
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.core.toByteArray
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class CMISServiceImpl : CMISService {
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
                append(ACCEPT, HEADERS_APPLICATION_TYPE)
            }
        }
    }

    override suspend fun fetchCMISFiles(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<List<CMISObjectDTO>> {
        return fetchFilteredCMISObjects(serverAddress, username, password, "cmis:document")
    }

    override suspend fun fetchCMISFolders(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<List<CMISObjectDTO>> {
        return fetchFilteredCMISObjects(serverAddress, username, password, "cmis:folder")
    }

    private suspend fun fetchFilteredCMISObjects(
        serverAddress: String,
        username: String,
        password: String,
        baseTypeIdFilter: String
    ): RequestCondition<List<CMISObjectDTO>> {
        val baseUrl =
            HTTPS_PROTOCOL + serverAddress + Constants.CMIS_API_HOME_ENDPOINT + "/$username"

        return try {
            println("Sending request to $baseUrl with filter baseTypeId=$baseTypeIdFilter")

            val response: CMISResponse = httpClient.get(baseUrl) {
                header("Authorization", "Basic ${encodeCredentials(username, password)}")
            }.body()

            println("Response received with ${response.objects.size} objects.")

            val filteredObjects = response.objects.mapNotNull { wrapper ->
                val properties = wrapper.objectData.properties
                val name = properties.name?.value
                val baseTypeId = properties.baseTypeId?.value

                if (name != null && baseTypeId == baseTypeIdFilter) {
                    CMISObjectDTO(name = name, baseTypeId = baseTypeId)
                } else {
                    println("Skipping object: name=$name, baseTypeId=$baseTypeId")
                    null
                }
            }

            println("Parsed ${filteredObjects.size} objects matching baseTypeId=$baseTypeIdFilter.")

            RequestCondition.SuccessCondition(filteredObjects)
        } catch (e: Exception) {
            e.printStackTrace()
            RequestCondition.ErrorCondition(errorMsg = e.message ?: "Error fetching CMIS objects")
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun encodeCredentials(username: String, password: String): String {
        val credentials = "$username:$password"
        return Base64.Default.encode(credentials.toByteArray())
    }
}

@Serializable
data class CMISResponse(
    @SerialName("objects") val objects: List<CMISObjectWrapper>
)

@Serializable
data class CMISObjectWrapper(
    @SerialName("object") val objectData: CMISObjectData
)

@Serializable
data class CMISObjectData(
    @SerialName("properties") val properties: CMISProperties
)

@Serializable
data class CMISProperties(
    @SerialName("cmis:name") val name: CMISValue? = null,
    @SerialName("cmis:baseTypeId") val baseTypeId: CMISValue? = null,
)

@Serializable
data class CMISValue(
    @SerialName("value") val value: String? = null
)

