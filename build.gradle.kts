plugins {
    id("java")
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm")
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.apache.tomcat.embed:tomcat-embed-jasper")
    implementation("jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api")
    implementation("org.glassfish.web:jakarta.servlet.jsp.jstl")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")
    implementation("org.apache.pdfbox:pdfbox:3.0.1")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    "developmentOnly"("org.springframework.boot:spring-boot-devtools")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(kotlin("stdlib-jdk8"))
    runtimeOnly("com.oracle.database.jdbc:ojdbc11:23.3.0.23.09")
    implementation("org.jsoup:jsoup:1.15.2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.oracle.database.jdbc:ojdbc11")
    implementation("org.projectlombok:lombok")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.apache.logging.log4j:log4j-core:2.19.0")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}