import jp.classmethod.aws.gradle.elasticbeanstalk.EbEnvironmentExtension
import org.apache.tools.ant.filters.ReplaceTokens
import java.util.*

plugins {
  application
  war
  kotlin("jvm")
  id("jp.classmethod.aws.beanstalk")
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

val fatJar = task("fatJar", type = Jar::class) {
  baseName = "${project.name}-fat"
  manifest {
    attributes["Implementation-Title"] = "Gradle Jar ${project.name}"
    attributes["Implementation-Version"] = version
    attributes["Main-Class"] = "Main"
  }
  from(configurations.runtime.map({ if (it.isDirectory) it else zipTree(it) }))
  with(tasks["jar"] as CopySpec)
}

val dataContent = copySpec {
  from("src/data")
  include("*.data")
}

val initConfig = task("initConfig", type = Copy::class) {
  includeEmptyDirs = true
  from("$projectDir/../client/build/web")
  into("$projectDir/src/main/resources/public")
  with(dataContent)
}

val removeStaticFiles = task("removeStaticFiles", type = Delete::class) {
  delete("$projectDir/src/main/resources/public")
}

tasks {
  "build" {
    dependsOn(fatJar)
    dependsOn(initConfig)
  }
  "clean" {
    dependsOn(removeStaticFiles)
  }
}

beanstalk {
  appName = "tycher"
  appDesc = "tycher app"

  environment(delegateClosureOf<EbEnvironmentExtension> {
    envName = "Tycher-env"
    envDesc = "Tycher-env"
    templateName = "development"
    versionLabel = "tycher-${Date()}"
  })
}
