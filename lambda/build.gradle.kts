import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.*

apply(plugin = "com.github.johnrengelman.shadow")

dependencies {
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter:2.7.9")
    implementation("org.springframework.cloud:spring-cloud-function-web")
    implementation("org.springframework.cloud:spring-cloud-function-adapter-aws")
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.9")
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2021.0.6"))

}

tasks.withType<ShadowJar> {
    classifier = "aws"
    dependencies {
        exclude(
            dependency("org.springframework.cloud:spring-cloud-function-web"))
    }
    // Required for Spring
    mergeServiceFiles()
    append("META-INF/spring.handlers")
    append("META-INF/spring.schemas")
    append("META-INF/spring.tooling")
    transform(PropertiesFileTransformer::class.java) {
        paths = listOf("META-INF/spring.factories")
        mergeStrategy = "append"
    }
}