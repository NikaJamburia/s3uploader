package ge.nika.s3uploader.web

import ge.nika.s3uploader.core.CoreApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(CoreApplication::class)
class S3uploaderWebApplication

fun main(args: Array<String>) {
	runApplication<S3uploaderWebApplication>(*args)
}
