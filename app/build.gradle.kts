plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("kotlin-parcelize")
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.storyapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.storyapp"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "SERVER_URL", "\"https://story-api.dicoding.dev/v1/\"")
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
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.paging.common.android)
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.espresso.idling.resource)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Lottie
    implementation (libs.lottie)

    //Coroutine
    implementation(libs.kotlinx.coroutines.android)

    // Hilt dependencies
    implementation ("com.google.dagger:hilt-android:2.51.1")
    kapt ("com.google.dagger:hilt-compiler:2.51.1")

    // For instrumentation tests
    androidTestImplementation  ("com.google.dagger:hilt-android-testing:2.51.1")
    kaptAndroidTest ("com.google.dagger:hilt-compiler:2.51.1")

    // For local unit tests
    testImplementation ("com.google.dagger:hilt-android-testing:2.51.1")
    kaptTest ("com.google.dagger:hilt-compiler:2.51.1")

    //network
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)

    //share preference
    implementation (libs.krate)

    //swipe refresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")

    //glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    //room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-paging:2.6.1")
    implementation ("androidx.paging:paging-runtime-ktx:3.3.0")
}