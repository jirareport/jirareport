pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

rootProject.name = "jirareport"

include(
    "application",
    "business-library",
    "common-library",
    "database-library",
    "holiday-library",
    "jira-library",
    "test-library"
)
