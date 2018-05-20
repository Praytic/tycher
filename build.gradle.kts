plugins {
    base
    kotlin("jvm") version "1.2.31" apply false
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
    // Make the root project archives configuration depend on every subproject
    subprojects.forEach {
        archives(it)
    }
}