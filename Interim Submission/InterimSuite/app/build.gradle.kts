plugins {
    id("com.android.application")
    id("com.mikepenz.aboutlibraries.plugin")
}

android {
    namespace = "com.rhul.fyp.interimsuite"
    compileSdk = 32

    defaultConfig {
        applicationId = "com.rhul.fyp.interimsuite"
        minSdk = 16
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
//            zipAlignEnabled = true
            isShrinkResources = true
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
//            resValue("string", "app_version"),
//            "${defaultConfig.versionName}" = versionName}"
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = true
//            zipAlignEnabled = true
//        resValue("string", "app_version"),
//        "${defaultConfig.versionName}${versionNameSuffix}" = {versionName}${versionNameSuffix}"
        }
    }
    lint {
        abortOnError = false
    }
    buildFeatures {
        viewBinding = true
    }
    androidResources {
        noCompress("tflite")
    }
}

dependencies {
    implementation (fileTree (mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation ("com.google.android.material:material:1.7.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.preference:preference:1.2.0")
    implementation ("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
    androidTestImplementation ("androidx.test.ext:junit:1.1.4")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("org.tensorflow:tensorflow-lite:2.10.0")
    testImplementation ("junit:junit:4.13.2")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.7.10")
    implementation ("com.mikepenz:aboutlibraries:8.5.0")
    implementation ("com.github.nekocode:Badge:2.1")
    implementation ("com.github.angads25:filepicker:1.1.1")
    implementation ("net.dongliu:apk-parser:2.6.10")
    implementation ("androidx.appcompat:appcompat:1.5.1")
    implementation ("com.google.android.material:material:1.7.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.navigation:navigation-fragment:2.5.3")
    implementation ("androidx.navigation:navigation-ui:2.5.3")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.4")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.0")
}
repositories {
    mavenCentral()
}