// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    ext.kotlin_version = '1.0.0'
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven { url "https://plugins.gradle.org/m2/" }
        maven { url 'https://jitpack.io' }
        maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:7.3.1'
        classpath "com.mikepenz.aboutlibraries.plugin:aboutlibraries-plugin:8.9.4"
        classpath(Config.BuildPlugins.androidGradlePlugin)
        classpath(Config.BuildPlugins.kotlinGradlePlugin)
        classpath("com.jakewharton.hugo:hugo-plugin:1.2.1")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
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
        mavenCentral()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}