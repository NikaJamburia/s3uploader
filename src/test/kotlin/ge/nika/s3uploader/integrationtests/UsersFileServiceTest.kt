package ge.nika.s3uploader.integrationtests

import ge.nika.s3uploader.storage.ContentType
import ge.nika.s3uploader.storage.FileStorage
import ge.nika.s3uploader.storage.NewFileParameters
import ge.nika.s3uploader.user.UsersFileService
import ge.nika.s3uploader.user.persistence.UsersFileRepository
import ge.nika.s3uploader.user.persistence.UsersFileType
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.Resource

@SpringBootTest
class UsersFileServiceTest(
    @Autowired private val subject: UsersFileService,
    @Autowired private val repository: UsersFileRepository,
    @Autowired private val fileStorage: FileStorage,
    @Value("classpath:/test-image.jpg") private val testImage: Resource,
    @Value("classpath:/test-file.txt") private val testTxtFile: Resource,
) {

    @BeforeEach
    fun clearData() {
        repository.deleteAll()
    }

    @Test
    fun `uploads file and saves its data in repository`() {
        val uploadedImage = subject.uploadFile("nika", NewFileParameters(
            inputStream = testImage.inputStream,
            contentLength = testImage.contentLength(),
            contentType = ContentType.IMAGE_JPG,
            fileName = "Myimage"
        ))

        val uploadedTxt = subject.uploadFile("nika", NewFileParameters(
            inputStream = testTxtFile.inputStream,
            contentLength = testTxtFile.contentLength(),
            contentType = ContentType.PLAIN_TEXT,
            fileName = "Mytxt"
        ))

        fileStorage.exists(uploadedImage.fileKey) shouldBe true
        fileStorage.exists(uploadedTxt.fileKey) shouldBe true

        repository.getByFileKey(uploadedImage.fileKey).asClue { uploadedImageFromDb ->
            uploadedImageFromDb.fileName shouldBe uploadedImage.fileName
            uploadedImageFromDb.fileKey shouldBe uploadedImage.fileKey
            uploadedImageFromDb.type shouldBe UsersFileType.IMAGE
        }

        repository.getByFileKey(uploadedTxt.fileKey).asClue { uploadedTxtFromDb ->
            uploadedTxtFromDb.fileName shouldBe uploadedTxt.fileName
            uploadedTxtFromDb.fileKey shouldBe uploadedTxt.fileKey
            uploadedTxtFromDb.type shouldBe UsersFileType.OTHER
        }

    }
}