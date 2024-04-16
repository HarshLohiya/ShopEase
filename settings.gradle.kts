pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { isAllowInsecureProtocol = true; url = uri("http://dl.bintray.com/tosinmath007/Carteasy") }

    }
}

rootProject.name = "ECom"
include(":app")
 