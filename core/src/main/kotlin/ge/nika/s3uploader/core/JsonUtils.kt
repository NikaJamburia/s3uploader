package ge.nika.s3uploader.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

val objectMapper: ObjectMapper = ObjectMapper()
    .registerModule(KotlinModule.Builder().build())
    .registerModule(JavaTimeModule())

fun <T> T.toJson(): String = objectMapper.writeValueAsString(this)

inline fun <reified T> fromJson(jsonString: String): T = objectMapper.readValue(jsonString)