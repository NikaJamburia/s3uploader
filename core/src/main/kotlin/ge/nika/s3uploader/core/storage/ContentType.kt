package ge.nika.s3uploader.core.storage

enum class ContentType(val text: String, val extension: String) {
    IMAGE_JPG("image/jpg", "jpg"),
    IMAGE_JPEG("image/jpeg", "jpeg"),
    IMAGE_PNG("image/png", "png"),
    APPLICATION_JSON("application/json", "json"),
    APPLICATION_XML("application/xml", "xml"),
    PLAIN_TEXT("text/plain", "txt"),
}

fun String.toContentType(): ContentType {
    val found = ContentType.values().firstOrNull { it.text == this }
    require(found != null) { "Content type $this not supported!" }
    return found
}