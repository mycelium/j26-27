plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("edu.stanford.nlp:stanford-corenlp:4.6.0")
    runtimeOnly("edu.stanford.nlp:stanford-corenlp:4.6.0:models")
    runtimeOnly("edu.stanford.nlp:stanford-corenlp:4.6.0:models-english")
    runtimeOnly("org.slf4j:slf4j-nop:1.7.36")
    implementation("org.apache.commons:commons-csv:1.14.1")
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
    applicationName = "sentiments"
    mainClass = "org.example.App"
    applicationDefaultJvmArgs = listOf("-Xmx2g")
}

tasks.named<JavaExec>("run") {
    workingDir(rootDir)
}

tasks.test {
    maxHeapSize = "2g"
}
