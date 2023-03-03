package ge.nika.s3uploader

import com.amazonaws.services.s3.AmazonS3
import ge.nika.s3uploader.storage.AwsFileStorage
import ge.nika.s3uploader.storage.NewFileParameters
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.Resource

@SpringBootTest
class AwsFileStorageTest(
    @Autowired private val subject: AwsFileStorage,
    @Autowired private val s3Client: AmazonS3,
    @Value("\${s3.bucket-name}") private val bucketName: String,
    @Value("classpath:/test-image.jpg") private val testFile: Resource
) {

    @Test
    fun `can upload a file to a bucket`() {
        val fileParams = NewFileParameters(
            inputStream = testFile.inputStream,
            contentLength = testFile.contentLength(),
            contentType = "image/jpg",
        )

        val fileKey = subject.upload(fileParams)

        s3Client.doesObjectExist(bucketName, fileKey) shouldBe true
    }

    @Test
    fun `throws exception when content type is unknown`() {
        val fileParams = NewFileParameters(
            inputStream = testFile.inputStream,
            contentLength = testFile.contentLength(),
            contentType = "some/thing",
        )

        val exception = shouldThrow<IllegalStateException> {
            subject.upload(fileParams)
        }

        exception.message shouldBe "File extension not defined for some/thing"
    }
}