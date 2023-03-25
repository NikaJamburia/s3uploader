package ge.nika.s3uploader.lambda

import ge.nika.s3uploader.core.fromJson
import ge.nika.s3uploader.core.storage.NewFileParameters
import ge.nika.s3uploader.core.storage.toContentType
import ge.nika.s3uploader.core.toUtc
import ge.nika.s3uploader.core.user.UsersFile
import ge.nika.s3uploader.core.user.UsersFileService
import kotlinx.coroutines.runBlocking
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.RequestEntity
import org.springframework.messaging.Message
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartRequest
import java.io.InputStream
import java.time.Instant
import java.util.ArrayList

@Configuration
class LambdaFunctions(
    private val usersFileService: UsersFileService
) {

    @Bean
    fun ping(): () -> String = { "Pong" }

    @Bean
    fun getSignedUrls(): (params: GetSignedUrlsParams) -> Map<String, String> = { params ->
        runBlocking {
            usersFileService.getSignedUrls(params.fileKeys.toSet())
        }
    }

    @Bean
    fun listFiles(): (params: ListFilesParams) -> List<UsersFile> = { params ->
        usersFileService.getLatestFiles(
            userName = params.userName,
            fromTime = Instant.ofEpochMilli(params.fromTimestamp).toUtc(),
            toTime = Instant.ofEpochMilli(params.toTimestamp).toUtc()
        )
    }

    @Bean
    fun uploadFile(): (request: Message<MultipartFile>) -> UsersFile = { request ->
        val userInfo = (request.headers["user"] as ArrayList<String>).firstOrNull()
            ?.let { fromJson<UserInformation>(it) }
            ?: error("No user info present in request headers")

        val file = request.payload

        val fileParams = NewFileParameters(
            inputStream = file.inputStream,
            contentLength = file.size,
            fileName = file.originalFilename ?: "",
            contentType = file.contentType?.toContentType() ?: error("Content type not provided!")
        )

        usersFileService.uploadFile(userInfo.userName, fileParams)
    }
}