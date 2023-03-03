package ge.nika.s3uploader.storage

import java.io.InputStream

interface FileStorage {
    fun upload(params: NewFileParameters): String
    fun getSignedUrl(fileKey: String): String
}

data class NewFileParameters(
    val inputStream: InputStream,
    val contentLength: Long,
    val contentType: String,
    val fileName: String
)