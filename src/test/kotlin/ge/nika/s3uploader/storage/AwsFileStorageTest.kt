package ge.nika.s3uploader.storage

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.PutObjectRequest
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.InputStream
import java.net.URL

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AwsFileStorageTest {

    private val s3Client = mockk<AmazonS3>()
    private val bucketName = "test-bucket"
    private val subject = AwsFileStorage(s3Client, bucketName)

    @AfterEach
    fun afterEach() = clearAllMocks()

    @AfterAll
    fun afterAll() = unmockkAll()

    @Test
    fun `provides given file to aws client`() {
        val uploadRequest = slot<PutObjectRequest>()
        every { s3Client.putObject(capture(uploadRequest)) } returns mockk()

        val inputStream = InputStream.nullInputStream()
        val params = NewFileParameters(
            inputStream = inputStream,
            contentLength = 0,
            contentType = ContentType.IMAGE_JPG,
            fileName = "test.jpg"
        )

        val key = subject.upload(params)

        verify(exactly = 1) {
            s3Client.putObject(any())
        }

        uploadRequest.captured.asClue {
            it.inputStream shouldBe inputStream
            it.bucketName shouldBe bucketName
            it.key shouldBe key
            it.metadata.contentLength shouldBe 0
            it.metadata.contentType shouldBe "image/jpg"
        }
    }

    @Test
    fun `requests pre signed url for a file from s3 and returns it`() {
        val urlString = "https://awss3.com/myfile"

        val performedRequest = slot<GeneratePresignedUrlRequest>()
        every { s3Client.generatePresignedUrl(capture(performedRequest)) } returns URL(urlString)

        val result = subject.getSignedUrl("filekey")

        result.shouldBe(urlString)
        performedRequest.captured.asClue {
            it.bucketName shouldBe bucketName
            it.key shouldBe "filekey"
            it.method shouldBe HttpMethod.GET
        }
    }
}