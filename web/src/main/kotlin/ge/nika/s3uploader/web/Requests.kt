package ge.nika.s3uploader.web

data class UploadFileRequest(
    val userName: String,
)

data class ListFilesRequest(
    val userName: String,
    val fromTimestamp: Long,
    val toTimestamp: Long,
)

data class GetSignedUrlsRequest(
    val fileKeys: List<String>
)