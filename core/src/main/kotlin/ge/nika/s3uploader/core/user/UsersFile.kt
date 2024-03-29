package ge.nika.s3uploader.core.user

import ge.nika.s3uploader.core.user.persistence.UsersFileType
import java.time.Instant
import java.util.*

data class UsersFile(
    val id: UUID,
    val userName: String,
    val fileKey: String,
    val fileName: String,
    val type: UsersFileType,
    val uploadTime: Instant
)