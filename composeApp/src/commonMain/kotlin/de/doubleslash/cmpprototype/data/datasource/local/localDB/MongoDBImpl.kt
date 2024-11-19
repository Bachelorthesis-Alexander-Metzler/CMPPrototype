package de.doubleslash.cmpprototype.data.datasource.local.localDB

import de.doubleslash.cmpprototype.data.datasource.local.localDB.dto.FileDTO
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

/**
 * Manages local storage operations for files, including saving, retrieving, and deleting files.
 * Utilizes local database to persist file metadata and contents.
 */
class MongoDBImpl : MongoDB {
    private var realm: Realm? = null

    init {
        configureTheRealm()
    }

    private fun configureTheRealm() {
        if (realm == null || realm!!.isClosed()) {
            val config = RealmConfiguration.Builder(
                schema = setOf(FileDTO::class)
            )
                .compactOnLaunch()
                .build()
            realm = Realm.open(config)
        }
    }

    override suspend fun addFile(file: FileDTO) {
        println("FileRepository | Adding file to realm")
        println("File name: ${file.baseName}")
        print("File extension: ${file.extension}")
        println("File path: ${file.path}")

        realm?.write {
            copyToRealm(file)
        }
    }

    override fun getAllFiles(): List<FileDTO> {
        return realm?.query<FileDTO>()?.find()?.toList() ?: emptyList()
    }

    override suspend fun deleteFile(file: FileDTO): Boolean {
        try {
            realm?.write {
                val queriedFile = query<FileDTO>("_id == $0", file._id).first().find()
                queriedFile?.let {
                    delete(it)
                }
            }
            println("FileRepository | File deleted")
            return true
        } catch (e: Exception) {
            println("FileRepository | Error deleting file: ${e.message}")
            return false
        }
    }
}