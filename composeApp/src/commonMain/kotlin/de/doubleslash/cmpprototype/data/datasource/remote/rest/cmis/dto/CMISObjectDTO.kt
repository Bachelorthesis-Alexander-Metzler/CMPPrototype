package de.doubleslash.cmpprototype.data.datasource.remote.rest.cmis.dto

import kotlinx.serialization.Serializable

/** data transfer object for login
 * contains response data from API
 * */
@Serializable
data class CMISObjectDTO(
    val name: String,
    val baseTypeId: String
)