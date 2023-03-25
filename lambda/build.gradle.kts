dependencies {
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter:2.7.9")
    implementation("org.springframework.cloud:spring-cloud-function-web")
    implementation("org.springframework.cloud:spring-cloud-function-adapter-aws")
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.9")
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2021.0.6"))

}