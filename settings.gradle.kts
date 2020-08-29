pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

rootProject.name = "jirareport"

include(
    "application",
    "business-library",
    "client-library",
    "common-library",
    "database-library",
    "test-library"
)
