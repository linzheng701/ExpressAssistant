pluginManagement {
    repositories {
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://repo.huaweicloud.com/repository/maven/")
        maven("https://jitpack.io")
        maven("https://developer.huawei.com/repo/")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://jitpack.io")
        maven("https://developer.huawei.com/repo/")
        google()
        mavenCentral()
    }
}

rootProject.name = "ExpressAssistant"
include(":app")
