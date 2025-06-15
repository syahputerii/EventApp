plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.syahna.appdicodingevent"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.syahna.appdicodingevent"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"https://event-api.dicoding.dev/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

    dependencies {
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material3)
        implementation(libs.androidx.constraintlayout)
        implementation(libs.androidx.lifecycle.livedata.ktx)
        implementation(libs.androidx.lifecycle.viewmodel.ktx)
        implementation(libs.androidx.navigation.fragment.ktx)
        implementation(libs.androidx.navigation.fragment)
        implementation(libs.androidx.navigation.ui.ktx)
        implementation(libs.androidxFragmentKtx)
        implementation(libs.androidx.activity.ktx)
        implementation(libs.glide)
        implementation(libs.retrofit)
        implementation(libs.converter.gson)
        implementation(libs.logging.interceptor)
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.datastore.preferences)
        implementation(libs.androidx.lifecycle.livedata.ktx)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.jetbrains.kotlinx.coroutines.android)
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.room.ktx)
        implementation(libs.kotlinx.coroutines.android)
        implementation (libs.androidx.datastore.preferences)
        implementation(libs.androidx.datastore.preferences.core.jvm)
        implementation(libs.androidx.datastore.core.android)
        implementation(libs.androidx.work.runtime.ktx)
        implementation(libs.androidx.legacy.support.v4)
        ksp(libs.room.compiler)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
}