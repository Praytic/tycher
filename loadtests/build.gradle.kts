plugins {
    application
    kotlin("jvm")
}

application {
    mainClassName = "Main"
}

dependencies {
    compile(kotlin("stdlib"))
    compile("com.github.myzhan:locust4j:1.0.0")
    compile("com.jayway.restassured:rest-assured:2.9.0")
}