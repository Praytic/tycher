import jp.classmethod.aws.gradle.elasticbeanstalk.EbEnvironmentExtension
import java.util.*

plugins {
    base
    kotlin("jvm") version "1.2.31" apply false
    id("jp.classmethod.aws") version "0.30"
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

buildscript {
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("jp.classmethod.aws:gradle-aws-plugin:0.30")
    }
}

aws {
    profileName = "default"
    region = "us-east-2"
}

dependencies {
    subprojects.forEach {
        archives(it)
    }
}