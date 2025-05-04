pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "BaseProject"
include(":app")
include(":domain")
include(":data")
include(":core") 