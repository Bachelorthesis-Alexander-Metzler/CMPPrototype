package de.doubleslash.cmpprototype.domain.model.file_mgmt

import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

data class FileModel(
    var _id: ObjectId? = BsonObjectId(),
    val baseName: String,
    val extension: String,
    val isRemoteFile: Boolean,
    val path: String? = null,
    val baseTypeId: String,
    val fileContent: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as FileModel

        if (_id != other._id) return false
        if (baseName != other.baseName) return false
        if (extension != other.extension) return false
        if (path != other.path) return false
        if (!fileContent.contentEquals(other.fileContent)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _id.hashCode()
        result = 31 * result + baseName.hashCode()
        result = 31 * result + extension.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + fileContent.contentHashCode()
        return result
    }
}