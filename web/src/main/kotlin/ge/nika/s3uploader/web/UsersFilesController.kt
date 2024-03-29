package ge.nika.s3uploader.web

import ge.nika.s3uploader.core.fromJson
import ge.nika.s3uploader.core.storage.NewFileParameters
import ge.nika.s3uploader.core.storage.toContentType
import ge.nika.s3uploader.core.toUtc
import ge.nika.s3uploader.core.user.UsersFile
import ge.nika.s3uploader.core.user.UsersFileService
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.Instant

@RestController
class UsersFilesController(
    private val usersFileService: UsersFileService,
) {

    @GetMapping("/ping")
    fun ping(): ResponseEntity<String> = ResponseEntity.ok("pong")

    @PostMapping("/upload")
    fun uploadFile(
        @RequestParam(value = "request") requestJson: String,
        @RequestParam(value = "file") file: MultipartFile
    ): UsersFile {
        val fileParams = NewFileParameters(
            inputStream = file.inputStream,
            contentLength = file.size,
            fileName = file.originalFilename ?: "",
            contentType = file.contentType?.toContentType() ?: error("Content type not provided!")
        )
        return usersFileService.uploadFile(fromJson<UploadFileRequest>(requestJson).userName, fileParams)
    }

    @PostMapping("/list-files")
    fun listFiles(@RequestBody request: ListFilesRequest): List<UsersFile> {
        return usersFileService.getLatestFiles(
            userName = request.userName,
            fromTime = Instant.ofEpochMilli(request.fromTimestamp).toUtc(),
            toTime = Instant.ofEpochMilli(request.toTimestamp).toUtc()
        )
    }

    @PostMapping("/get-signed-urls")
    fun getSignedUrls(@RequestBody request: GetSignedUrlsRequest): Map<String, String> =
        runBlocking {
            usersFileService.getSignedUrls(request.fileKeys.toSet())
        }
}