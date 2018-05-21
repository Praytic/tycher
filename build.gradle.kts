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
//    compile("com.github.jsimone:webapp-runner:8.5.11.3")
    // Make the root project archives configuration depend on every subproject
    subprojects.forEach {
        archives(it)
    }
}

//val copyToLib = task("copyToLib", Copy::class) {
//    into("$buildDir/server")
//    from(configurations.compile) {
//        include("webapp-runner*")
//    }
//}

//tasks {
//    "stage"{
//        dependsOn("clean", "war", "copyLib")
//    }
//    "war"{
//        mustRunAfter("clean")
//    }
//    "copyToLib"{
//
//    }
//}

heroku {
    includes = listOf("server/build/libs/server-fat-1.0.jar")
    includeBuildDir = false
    processTypes = mapOf("web" to "server/build/libs/server-fat-1.0.jar")
}

