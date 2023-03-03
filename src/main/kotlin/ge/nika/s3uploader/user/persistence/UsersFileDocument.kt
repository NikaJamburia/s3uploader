package ge.nika.s3uploader.user.persistence

import ge.nika.s3uploader.toUtc
import ge.nika.s3uploader.user.UsersFile
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.UUID

@Document
data class UsersFileDocument(
    @Id
    val id: UUID = UUID.randomUUID(),
    val userName: String,
    val fileKey: String,
    val fileName: String,
    val type: UsersFileType,
    val uploadTime: Instant = Instant.now().toUtc()
)

fun UsersFileDocument.toDto() = UsersFile(
    id = id,
    userName = userName,
    fileKey = fileKey,
    fileName = fileName,
    type = type,
    uploadTime = uploadTime
)
