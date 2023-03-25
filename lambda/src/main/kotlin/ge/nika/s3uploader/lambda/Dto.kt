package ge.nika.s3uploader.lambda

data class GetSignedUrlsParams(
    val fileKeys: List<String>
)

data class ListFilesParams(
    val userName: String,
    val fromTimestamp: Long,
    val toTimestamp: Long,
)

data class UserInformation(
    val userName: String,
)