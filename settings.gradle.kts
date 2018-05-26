rootProject.name = "tycher"
include("client", "model", "server", "loadtests")

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlin2js") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
            if (requested.id.id == "jp.classmethod.aws.beanstalk") {
                useModule("com.amazonaws:aws-java-sdk-elasticbeanstalk:${requested.version}")
            }
        }
    }
}
