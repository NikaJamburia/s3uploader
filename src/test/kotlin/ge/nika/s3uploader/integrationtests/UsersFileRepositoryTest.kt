package ge.nika.s3uploader.integrationtests

import ge.nika.s3uploader.toUtc
import ge.nika.s3uploader.toUtcInstant
import ge.nika.s3uploader.user.persistence.UsersFileDocument
import ge.nika.s3uploader.user.persistence.UsersFileRepository
import ge.nika.s3uploader.user.persistence.UsersFileType
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import java.time.Instant
import java.util.UUID

@DataMongoTest
class UsersFileRepositoryTest(
    @Autowired private val subject: UsersFileRepository
) {

    @BeforeEach
    fun clearDb() {
        subject.deleteAll()
    }

    @Test
    fun `saves and fetches documents from database`() {
        val saved = subject.save(fakeUsersFileDocument())

        val fromDb = subject.getById(saved.id)

        fromDb.id shouldBe saved.id
        fromDb.userName shouldBe saved.userName
        fromDb.fileKey shouldBe saved.fileKey
        fromDb.fileName shouldBe saved.fileName
        fromDb.type shouldBe saved.type
    }

    @Test
    fun `fetches records from given time period fo a given user`() {
        val from = "2023-03-03T12:00:00".toUtcInstant()
        val to = "2023-03-10T13:00:00".toUtcInstant()
        val user = "nika"

        subject.saveAll(listOf(
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

        val result = subject.listUsersFilesFromPeriod(
            userName = user,
            fromTime = from,
            toTime = to,
        )

        result.size shouldBe 3
        result.all { it.userName == user } shouldBe true
        result.firstOrNull { it.uploadTime == "2023-03-03T12:01:00".toUtcInstant() } shouldNotBe null
        result.firstOrNull { it.uploadTime == "2023-03-05T12:00:00".toUtcInstant() } shouldNotBe null
        result.firstOrNull { it.uploadTime == "2023-03-10T12:59:00".toUtcInstant() } shouldNotBe null
    }

    private fun fakeUsersFileDocument(
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
}