package ge.nika.s3uploader.core.storage

import java.io.InputStream

interface FileStorage {
    fun upload(params: NewFileParameters): String
    fun getSignedUrl(fileKey: String): String
    fun exists(fileKey: String): Boolean
    fun delete(fileKey: String)
}

data class NewFileParameters(
    val inputStream: InputStream,
    val contentLength: Long,
    val contentType: ContentType,
    val fileName: String
)