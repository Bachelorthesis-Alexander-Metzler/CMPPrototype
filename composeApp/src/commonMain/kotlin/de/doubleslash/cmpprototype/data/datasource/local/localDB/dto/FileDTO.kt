package de.doubleslash.cmpprototype.data.datasource.local.localDB.dto

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class FileDTO : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId() // unique id for file
    var baseName: String = "" // file name
    var extension: String = "" // file extension
    var path: String = "" // file path
    var fileContent: ByteArray = byteArrayOf() // file content
}