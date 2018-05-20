plugins {
    kotlin("jvm")
}

the<JavaPluginConvention>().sourceSets {
    "dto" {
        java {
            srcDirs("../dto/src/main/kotlin")
        }
    }
}

dependencies {
    compile(kotlin("stdlib"))
}
