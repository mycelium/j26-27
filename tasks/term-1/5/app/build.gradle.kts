plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.corenlp)
    implementation(variantOf(libs.corenlp) { classifier("models") })
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.12.1")
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

application {
    mainClass = "org.example.App"
    applicationDefaultJvmArgs = listOf("-Xmx4g")
}

tasks.named<JavaExec>("run") {
    workingDir = rootProject.projectDir
}