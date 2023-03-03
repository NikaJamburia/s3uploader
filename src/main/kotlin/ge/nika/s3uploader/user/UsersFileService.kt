package ge.nika.s3uploader.user

import ge.nika.s3uploader.storage.AwsFileStorage
import ge.nika.s3uploader.storage.NewFileParameters
import ge.nika.s3uploader.user.persistence.UsersFileDocument
import ge.nika.s3uploader.user.persistence.UsersFileRepository
import ge.nika.s3uploader.user.persistence.UsersFileType
import ge.nika.s3uploader.user.persistence.toDto
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class UsersFileService(
    private val repository: UsersFileRepository,
    private val fileStorage: AwsFileStorage
) {
    fun uploadFile(userName: String, fileParams: NewFileParameters): UsersFile {
        val fileKey = fileStorage.upload(fileParams)
        return repository.save(UsersFileDocument(
            userName = userName,
            fileKey = fileKey,
            fileName = fileParams.fileName,
            type = fileParams.usersFileType()
        )).toDto()
    }

    fun getSignedUrl(fileKey: String): String = fileStorage.getSignedUrl(fileKey)

    fun getSignedUrls(fileKeys: Set<String>): Map<String, String> = runBlocking {
        fileKeys.map { fileKey ->
            async { fileKey to fileStorage.getSignedUrl(fileKey) }
        }.awaitAll().toMap()
    }

    fun getLatestFiles(
        userName: String,
        fromTime: Instant,
        toTime: Instant
    ): List<UsersFile> {
        return repository
            .findAllByUserNameAndUploadTimeGreaterThanEqualAndUploadTimeLessThanEqual(
                userName = userName,
                fromTime = fromTime,
                toTime = toTime
            ).map { it.toDto() }
    }

    private fun NewFileParameters.usersFileType(): UsersFileType =
        if(contentType.startsWith("image")) {
            UsersFileType.IMAGE
        } else {
            UsersFileType.OTHER
        }
}