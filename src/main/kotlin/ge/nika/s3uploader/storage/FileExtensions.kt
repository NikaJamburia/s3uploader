package ge.nika.s3uploader.storage

fun getExtensionFrom(contentType: String): String = when(contentType) {
    "image/jpg" -> "jpg"
    else -> error("File extension not defined for $contentType")
}