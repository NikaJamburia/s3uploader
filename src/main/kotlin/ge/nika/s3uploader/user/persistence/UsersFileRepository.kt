package ge.nika.s3uploader.user.persistence

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.time.Instant
import java.util.UUID

interface UsersFileRepository : MongoRepository<UsersFileDocument, UUID> {

    fun getById(id: UUID): UsersFileDocument

    @Query(value = "{ userName: ?0, 'uploadTime': { \$gte: ?1, \$lte: ?2 }}")
    fun listUsersFilesFromPeriod(
        userName: String,
        fromTime: Instant,
        toTime: Instant
    ): List<UsersFileDocument>
}