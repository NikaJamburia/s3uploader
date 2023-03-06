package ge.nika.s3uploader.fixtures

import ge.nika.s3uploader.toUtc
import ge.nika.s3uploader.user.persistence.UsersFileDocument
import ge.nika.s3uploader.user.persistence.UsersFileType
import java.time.Instant
import java.util.*

fun fakeUsersFileDocument(
    id: UUID = UUID.randomUUID(),
    userName: String = UUID.randomUUID().toString(),
    fileKey: String = UUID.randomUUID().toString(),
    fileName: String = UUID.randomUUID().toString(),
    type: UsersFileType = UsersFileType.IMAGE,
    uploadTime: Instant = Instant.now().toUtc()
) = UsersFileDocument(
    id = id,
    userName = userName,
    fileKey = fileKey,
    fileName = fileName,
    type = type,
    uploadTime = uploadTime
)