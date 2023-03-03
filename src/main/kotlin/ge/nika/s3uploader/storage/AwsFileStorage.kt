package ge.nika.s3uploader.storage

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AwsFileStorage(
    private val s3Client: AmazonS3,
    @Value("\${s3.bucket-name}") private val bucketName: String,
) : FileStorage {

    override fun upload(params: NewFileParameters): String {
        val objectKey = "${UUID.randomUUID()}.${getExtensionFrom(params.contentType)}"
        val metadata = ObjectMetadata().apply {
            contentLength = params.contentLength
            contentType = params.contentType
        }
        val request = PutObjectRequest(bucketName, objectKey, params.inputStream, metadata)

        s3Client.putObject(request)
        return objectKey
    }

    override fun getSignedUrl(fileKey: String): String {
        val request = GeneratePresignedUrlRequest(bucketName, fileKey, HttpMethod.GET)
        return s3Client.generatePresignedUrl(request).toString()
    }
}