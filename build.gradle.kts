plugins {
    base
    kotlin("jvm") version "1.2.31" apply false
    id("com.heroku.sdk.heroku-gradle") version "1.0.1"
}

allprojects {

    group = "com.vchernogorov"

    version = "1.0"

    repositories {
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
        }
    }
}

dependencies {
    subprojects.forEach {
        archives(it)
    }
}
