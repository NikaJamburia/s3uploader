package ge.nika.s3uploader.core.integrationtests

import com.amazonaws.services.s3.AmazonS3
import ge.nika.s3uploader.core.storage.AwsFileStorage
import ge.nika.s3uploader.core.storage.ContentType
import ge.nika.s3uploader.core.storage.NewFileParameters
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.Resource

@SpringBootTest
class AwsFileStorageIntegrationTest(
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
            contentType = ContentType.IMAGE_JPG,
            fileName = testFile.filename!!
        )

        val fileKey = subject.upload(fileParams)

        s3Client.doesObjectExist(bucketName, fileKey) shouldBe true
    }
}