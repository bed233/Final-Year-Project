plugins {
    id 'com.android.application'
    id 'com.mikepenz.aboutlibraries.plugin'
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("com.jakewharton.hugo")
}

android {
    namespace 'com.rhul.fyp.interimsuite'
    compileSdk 32

    defaultConfig {
        applicationId "com.rhul.fyp.interimsuite"
        minSdk 16
        targetSdk 32
        versionCode 1
        versionName "1.0"
        resConfigs(*Config.languages)
        val artifactName =
        if (System.getenv("GITHUB_ACTIONS") != null) "ML" else "ML-$versionName"
        setProperty("archivesBaseName", artifactName)
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
        vectorDrawables.useSupportLibrary = true
    }
    ndkVersion = "21.0.6113669"
    sourceSets {
        getByName("main") {
            res.srcDir("src/main/res/translations")
        }
    }
    lintOptions {
        isAbortOnError = false
        disable("ExtraTranslation")
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        release {
            minifyEnabled true
            zipAlignEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            resValue "string", "app_version",
                    "${defaultConfig.versionName}"
        }
        debug {
            applicationIdSuffix ".debug"
            minifyEnabled true
            zipAlignEnabled true
            resValue "string", "app_version",
                    "${defaultConfig.versionName}${versionNameSuffix}"
        }
    }
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }
    androidExtensions {
        isExperimental = true
    }
    registerTransform(AndroidHiddenTransform())
}

lint {
    abortOnError false
}
buildFeatures {
    viewBinding true
}
androidResources {
    noCompress 'tflite'
}

repositories {
    flatDir {
        dir("libs")
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'androidx.appcompat:appcompat:1.5.1'
    implementation 'com.google.android.material:material:1.7.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.preference:preference:1.2.0'
    implementation 'androidx.localbroadcastmanager:localbroadcastmanager:1.1.0'
    androidTestImplementation 'androidx.test.ext:junit:1.1.4'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.0'
    implementation 'androidx.recyclerview:recyclerview:1.2.1'
    implementation 'org.tensorflow:tensorflow-lite:2.10.0'
    testImplementation 'junit:junit:4.13.2'
    implementation "org.jetbrains.kotlin:kotlin-stdlib:1.7.10"
    implementation 'com.mikepenz:aboutlibraries:8.5.0'
    implementation 'com.github.nekocode:Badge:2.1'
    implementation 'com.github.angads25:filepicker:1.1.1'
    implementation 'net.dongliu:apk-parser:2.6.10'
    implementation 'androidx.appcompat:appcompat:1.5.1'
    implementation 'com.google.android.material:material:1.7.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.navigation:navigation-fragment:2.5.3'
    implementation 'androidx.navigation:navigation-ui:2.5.3'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.4'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.0'

    // Kotlin stdlib & coroutines
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Config.kotlinVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2-1.3.60")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.0")

    // AndroidX
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.browser:browser:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.fragment:fragment:1.2.0-rc02")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("com.google.android.material:material:1.2.0-alpha02")
    //-- KTX
    implementation("androidx.core:core-ktx:1.1.0")
    implementation("androidx.activity:activity-ktx:1.1.0-rc02")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.1.0")

    // From JCenter or other repositories
    //-- 3rd-party
    implementation("com.anjlab.android.iab.v3:library:1.0.38@aar")
    implementation("de.Maxr1998.android:modernpreferences:0.4.1")
    implementation("com.crossbowffs.remotepreferences:remotepreferences:0.5")
    implementation("com.github.attenzione:android-ColorPickerPreference:e3aa301016")
    implementation("com.github.topjohnwu.libsu:core:2.2.0")
    implementation("com.heinrichreimersoftware:material-intro:1.6")
    implementation("${Config.Deps.splittiesBase}:splitties-dimensions:${Config.Deps.splittiesVersion}")
    implementation("com.twofortyfouram:android-plugin-api-for-locale:1.0.2")
    implementation("commons-io:commons-io:2.6")
    implementation("eu.chainfire:librootjava:1.3.1")
    implementation("eu.chainfire:librootjavadaemon:1.3.1")

    // Local files
    implementation("", "PatternLock", ext = "aar")

    // Testing dependencies
    androidTestUtil("androidx.test:orchestrator:1.2.0")
    androidTestImplementation("androidx.test:rules:1.2.0")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.0-alpha4") {
        exclude("com.google.code.findbugs", "jsr305")
    }
    androidTestImplementation("com.github.Zhuinden:espresso-helper:0.1.3") {
        exclude("com.google.code.findbugs", "jsr305")
    }
    compileOnly(files(Config.Deps.androidJar))
}

val copyTask = tasks.register(Config.Tasks.copyAndroidJar) {
    val apiProject = rootProject.project("androidhiddenapi")
    dependsOn(apiProject.tasks["assembleDebug"])
    doLast {
        val output = project.file(Config.Deps.androidJar)
        ZipFile(File(apiProject.buildDir, "outputs/aar/${apiProject.name}-debug.aar")).use { zipFile ->
            zipFile.getInputStream(zipFile.getEntry("classes.jar")).use {
                it.copyTo(output)
            }
        }
    }
}
tasks["preBuild"].dependsOn(copyTask)

tasks.register("updateTranslations") {
    doLast {
        Config.crowdinKey ?: return@doLast
        assert (Config.languages.size == Config.crowdinLanguages.size) {
            "Language arrays are of different length!"
        }
        for (i in Config.languages.indices) {
            val lang = Config.languages[i]
            val crowdinLang = Config.crowdinLanguages[i]
            for (fileName in arrayOf("strings", "implementation_strings")) {
                val outputFile = "\$PWD/src/main/res/translations/values-$lang/$fileName.xml"
                exec {
                    executable = "bash"
                    args("-c", "wget -O \"$outputFile\" \"https://api.crowdin.com/api/project/MaxLock/export-file?file=$fileName.xml&language=$crowdinLang&key=${Config.crowdinKey}\"")
                }
            }
        }
    }
    repositories {
        mavenCentral()
    }
}