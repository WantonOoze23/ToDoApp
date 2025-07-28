plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.google.ksp)
}

android {
    namespace = "com.tyshko.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":domain"))

    // Ktor
    implementation(libs.ktor.ktor.client.core)
    testImplementation(libs.ktor.client.mock)
    implementation(libs.ktor.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    testImplementation(libs.kotlinx.coroutines.test)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.junit)

    // Room
    ksp(libs.androidx.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.androidx.room.ktx)

    //Mockk
    testImplementation(libs.mockk)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(kotlin("test"))
    testImplementation(libs.slf4j.simple)
}