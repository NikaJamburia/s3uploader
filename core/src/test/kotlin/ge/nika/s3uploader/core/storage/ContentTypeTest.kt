package ge.nika.s3uploader.core.storage

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ContentTypeTest {

    @Test
    fun `throws error when trying to create unsupported content type`() {
        val exception = shouldThrow<IllegalArgumentException> {
            "something".toContentType()
        }
        exception.message shouldBe "Content type something not supported!"
    }

    @Test
    fun `no duplicate content types exist`() {
        val enumSize = ContentType.values().size

        val textsSet = ContentType.values().mapTo(HashSet()) { it.text }
        val extensionsSet = ContentType.values().mapTo(HashSet()) { it.extension }

        assert(textsSet.size == enumSize) { "ContentType text values contain duplicates" }
        assert(extensionsSet.size == enumSize) { "ContentType extensions values contain duplicates" }
    }
}