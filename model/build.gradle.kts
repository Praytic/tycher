plugins {
    kotlin("jvm")
}

the<JavaPluginConvention>().sourceSets {
    "main" {
        java {
            srcDirs("../dto/src/main/kotlin")
        }
    }
}

dependencies {
    compile(kotlin("stdlib"))
}
