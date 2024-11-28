package de.doubleslash.cmpprototype.domain.model.file_mgmt

import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

data class FileModel(
    var _id: ObjectId = BsonObjectId(),
    val baseName: String,
    val extension: String,
    val path: String
)
