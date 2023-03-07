package ge.nika.s3uploader.integrationtests

import ge.nika.s3uploader.fixtures.fakeUsersFileDocument
import ge.nika.s3uploader.fromJson
import ge.nika.s3uploader.storage.ContentType
import ge.nika.s3uploader.storage.FileStorage
import ge.nika.s3uploader.toJson
import ge.nika.s3uploader.toUtcInstant
import ge.nika.s3uploader.user.UsersFile
import ge.nika.s3uploader.user.persistence.UsersFileRepository
import ge.nika.s3uploader.user.persistence.UsersFileType
import ge.nika.s3uploader.web.ListFilesRequest
import ge.nika.s3uploader.web.UploadFileRequest
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.multipart
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
class UsersFilesControllerTest(
    @Autowired private val repository: UsersFileRepository,
    @Autowired private val context: WebApplicationContext,
    @Autowired private val fileStoreage: FileStorage,
    @Value("classpath:/test-file.txt") private val txtFile: Resource
) {

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun clearData() {
        repository.deleteAll()
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun `uploads given file for a user`() {

        val file = MockMultipartFile(
            "file",
            "test-file.txt",
            ContentType.PLAIN_TEXT.text,
            txtFile.inputStream
        )

        val responseJson = mockMvc.multipart("/upload") {
            file(file)
            param("request", UploadFileRequest("nika").toJson())
        }.andExpect { status { isOk() } }.andReturn().response.contentAsString

        fromJson<UsersFile>(responseJson).asClue {
            it.fileName shouldBe "test-file.txt"
            it.userName shouldBe "nika"
            it.type shouldBe UsersFileType.OTHER
            fileStoreage.exists(it.fileKey) shouldBe true
        }

    }

    @Test
    fun `lists files uploaded by user`() {
        repository.saveAll(listOf(
            fakeUsersFileDocument(uploadTime = "2023-03-02T18:00:00".toUtcInstant(), userName = "nika"),
            fakeUsersFileDocument(uploadTime = "2023-03-03T11:59:00".toUtcInstant(), userName = "nika"),

            fakeUsersFileDocument(uploadTime = "2023-03-03T12:01:00".toUtcInstant(), userName = "nika"),
            fakeUsersFileDocument(uploadTime = "2023-03-05T12:00:00".toUtcInstant(), userName = "nika"),
            fakeUsersFileDocument(uploadTime = "2023-03-10T12:59:00".toUtcInstant(), userName = "nika"),

            fakeUsersFileDocument(uploadTime = "2023-03-03T12:01:00".toUtcInstant(), userName = "somebody"),
            fakeUsersFileDocument(uploadTime = "2023-03-05T12:00:00".toUtcInstant(), userName = "somebody"),
            fakeUsersFileDocument(uploadTime = "2023-03-10T12:59:00".toUtcInstant(), userName = "somebody"),

            fakeUsersFileDocument(uploadTime = "2023-03-10T13:01:00".toUtcInstant(), userName = "nika"),
            fakeUsersFileDocument(uploadTime = "2023-03-11T12:00:00".toUtcInstant(), userName = "nika"),
        ))

        val nikasFilesJson = mockMvc.post("/list-files") {
            contentType = MediaType.APPLICATION_JSON
            content = ListFilesRequest(
                userName = "nika",
                fromTimestamp = "2023-03-03T12:00:00".toUtcInstant().toEpochMilli(),
                toTimestamp = "2023-03-10T13:00:00".toUtcInstant().toEpochMilli(),
            ).toJson()
        }.andExpect { status { isOk() } }.andReturn().response.contentAsString

        fromJson<List<UsersFile>>(nikasFilesJson).asClue { files ->
            files.size shouldBe 3
            files.all { it.userName == "nika" } shouldBe true
        }

        val somebodiesFilesJson = mockMvc.post("/list-files") {
            contentType = MediaType.APPLICATION_JSON
            content = ListFilesRequest(
                userName = "somebody",
                fromTimestamp = "2023-03-03T12:00:00".toUtcInstant().toEpochMilli(),
                toTimestamp = "2023-03-05T11:59:00".toUtcInstant().toEpochMilli(),
            ).toJson()
        }.andExpect { status { isOk() } }.andReturn().response.contentAsString

        fromJson<List<UsersFile>>(somebodiesFilesJson).asClue { files ->
            files.size shouldBe 1
            files.first().userName shouldBe "somebody"
        }
    }


}