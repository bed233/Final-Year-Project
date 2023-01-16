// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    extra["kotlin_version"] = "1.1.16"
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("https://jitpack.io")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    dependencies {
        classpath(Config.BuildPlugins.androidGradlePlugin)
        classpath(Config.BuildPlugins.kotlinGradlePlugin)
        classpath("com.jakewharton.hugo:hugo-plugin:1.2.1")
        classpath("com.mikepenz.aboutlibraries.plugin:aboutlibraries-plugin:8.9.4")
    }

}

allprojects {
    repositories {
        google().includes(
            "androidx.*",
            "com\\.android.*",
            "com\\.google\\.android.*"
        )
        maven { setUrl("https://jitpack.io") }
        jcenter().excludes("com\\.github.*")
        google()
        mavenCentral()
        jcenter()
        maven("https://jitpack.io")
    }
}

tasks.create<Delete>("clean") {
    delete(rootProject.buildDir)
}