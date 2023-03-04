package ge.nika.s3uploader.web

import ge.nika.s3uploader.storage.ContentType
import ge.nika.s3uploader.storage.NewFileParameters
import ge.nika.s3uploader.storage.toContentType
import ge.nika.s3uploader.user.UsersFile
import ge.nika.s3uploader.user.UsersFileService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.time.Instant

@RestController
class UsersFilesController(
    private val usersFileService: UsersFileService,
) {

    @PostMapping("/upload")
    fun uploadFile(@RequestPart request: UploadFileRequest, @RequestPart file: MultipartFile): UsersFile {
        val fileParams = NewFileParameters(
            inputStream = file.inputStream,
            contentLength = file.size,
            fileName = file.name,
            contentType = file.contentType?.toContentType() ?: error("Content type not provided!")
        )
        return usersFileService.uploadFile(request.userName, fileParams)
    }

    @PostMapping("/list-files")
    fun listFiles(@RequestBody request: ListFilesRequest): List<UsersFile> {
        return usersFileService.getLatestFiles(
            userName = request.userName,
            fromTime = Instant.ofEpochMilli(request.fromTimestamp),
            toTime = Instant.ofEpochMilli(request.toTimestamp)
        )
    }
}