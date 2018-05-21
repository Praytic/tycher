plugins {
    application
    kotlin("jvm")
}

application {
    mainClassName = "Main"
}

dependencies {
    compile(project(":model"))
    compile(kotlin("stdlib"))
    compile(kotlin("reflect"))
    compile("com.j2html:j2html:1.0.0")
    compile("com.sparkjava:spark-core:2.6.0")
    compile("org.slf4j:slf4j-simple:1.7.21")
    compile("com.google.guava:guava:22.0")
    compile("com.github.salomonbrys.kodein:kodein:4.1.0")
    compile("com.github.salomonbrys.kotson:kotson:2.5.0")
    compile("io.github.microutils:kotlin-logging:1.5.4")
    testCompile("junit:junit:4.12")
    testCompile("org.mockito:mockito-core:1.9.5")
    testCompile(kotlin("test"))
    testCompile(kotlin("test-junit"))
}

tasks.withType(JavaExec::class.java) {
    if (System.getProperty("DEBUG") != null) {
        println("Debug mode is on.")
        jvmArgs = listOf("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=9099")
    } else {
        println("Debug mode is off.")
    }
}