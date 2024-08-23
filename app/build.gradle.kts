plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.services)

}

android {
    namespace = "com.lornamobileappsdev.my_marketplace"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lornamobileappsdev.my_marketplace"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {


        debug {
            buildConfigField( "String", "API_URL", "\"https://lovicmarketplace.up.railway.app/\"")
            buildConfigField ("String", "APP_Email", "\"structuredapps@yahoo.com\"")

        }

        release {
            buildConfigField ("String", "API_URL", "\"https://lovicmarketplace.up.railway.app/\"")
            buildConfigField ("String", "APP_Email", "\"structuredapps@yahoo.com\"")
        }



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
    }
}

dependencies {

    val room_version = "2.3.0"
    val ktor_version = "2.3.0"
    val logback_version = "1.2.11"
    val kotlin_version = "1.8.20"
    val lottieVersion = "3.4.0"

    //in-app update
    implementation (libs.play.core.ktx)
    implementation(libs.converter.scalars)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.swiperefreshlayout)
    implementation(libs.test.core.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.lottie)
    implementation(libs.material)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.preference.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.viewbinding)
    implementation(libs.glide)
    kapt(libs.glide.compiler)
    implementation(libs.retrofit.adapter.guava)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.cardview)
    implementation(libs.design)
    implementation(libs.gson)
    implementation(libs.coroutines.core)
    implementation(libs.okhttp)
    implementation(libs.okhttp.urlconnection)
    implementation(libs.retrofit.old)
    implementation(libs.fragment.ktx)
    implementation(libs.lifecycle.extensions)
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.kotlin.reflect)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.serialization)
    implementation(libs.serialization.json)
    implementation(libs.ktor.client.logging.jvm)
    implementation(libs.shimmer)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.ktor.client.json)
    implementation(libs.kotlin.serialization)
    implementation(libs.coil)
    implementation(libs.ump)
    implementation(libs.logging.interceptor)
    implementation(libs.annotation)
    implementation(libs.work.runtime.ktx)
    implementation(libs.play.services.ads)

}