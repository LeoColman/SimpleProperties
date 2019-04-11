import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "0.9.17"
    
}

group = "com.kerooker.simpleproperties"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(group = "io.kotlintest", name = "kotlintest-runner-junit5", version = "3.3.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets.getByName("main").allSource)
}

val javadocJar by tasks.registering(Jar::class) {
    val javadoc = tasks["dokka"] as DokkaTask
    javadoc.outputFormat = "javadoc"
    javadoc.outputDirectory = "$buildDir/javadoc"
    dependsOn(javadoc)
    classifier = "javadoc"
    from(javadoc.outputDirectory)
}

publishing {
    repositories {
        
        maven("https://oss.sonatype.org/service/local/staging/deploy/maven2") {
            credentials {
                username = System.getProperty("OSSRH_USERNAME")
                password = System.getProperty("OSSRH_PASSWORD")
            }
        }
    }
    
    publications {
        
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
            artifact(javadocJar.get())
            
            pom {
                name.set("Simple Properties")
                description.set("Simple Properties")
                url.set("https://www.github.com/Kerooker/SimpleProperties")
                
                
                scm {
                    connection.set("scm:git:http://www.github.com/Kerooker/SimpleProperties/")
                    developerConnection.set("scm:git:http://github.com/Kerooker/")
                    url.set("https://www.github.com/Kerooker/SimpleProperties")
                }
                
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                
                developers {
                    developer {
                        id.set("Kerooker")
                        name.set("Leonardo Colman Lopes")
                        email.set("leonardo.colman98@gmail.com")
                    }
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}