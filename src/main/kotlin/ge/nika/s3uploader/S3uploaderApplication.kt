package ge.nika.s3uploader

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class S3uploaderApplication

fun main(args: Array<String>) {
	runApplication<S3uploaderApplication>(*args)
}
