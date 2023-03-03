package ge.nika.s3uploader.user.persistence

import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant
import java.util.UUID

interface UsersFileRepository : MongoRepository<UsersFileDocument, UUID> {

    fun getById(id: UUID): UsersFileDocument

    fun findAllByUserNameAndUploadTimeGreaterThanEqualAndUploadTimeLessThanEqual(
        userName: String,
        fromTime: Instant,
        toTime: Instant
    ): List<UsersFileDocument>
}