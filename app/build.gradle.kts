import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlinx-serialization")
    id("com.google.gms.google-services")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.adamdawi.popcornpicks"
    compileSdk = 34

    tasks.withType<Test> {
        jvmArgs("-XX:+EnableDynamicAgentLoading")
    }

    defaultConfig {
        applicationId = "com.adamdawi.popcornpicks"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.adamdawi.popcornpicks.utils.InstrumentationTestRunner"
    }

    buildTypes {
        release {
            val apiKey = gradleLocalProperties(rootDir, rootProject.providers).getProperty("API_KEY")
            buildConfigField("String", "API_KEY", "\"$apiKey\"")
            buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3\"")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            val apiKey = gradleLocalProperties(rootDir, rootProject.providers).getProperty("API_KEY")
            buildConfigField("String", "API_KEY", "\"$apiKey\"")
            buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging { resources.excludes.add("META-INF/*") }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //logging
    implementation(libs.timber)
    //image loading
    implementation(libs.coil.compose)
    //room db
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.androidx.room.compiler.v250)
    //lifecycles like collectAsStateWithLifecycle()
    implementation(libs.androidx.lifecycle.runtime.compose)
    //viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    //navigation transitions
    implementation(libs.androidx.navigation.compose)
    //data store
    implementation(libs.androidx.datastore.preferences)
    //ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    //koin
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    //serialization
    implementation(libs.gson)
    //slf4j - to fix issue about logging
    testImplementation(libs.slf4j.simple)
    //color picker
    implementation(libs.mhssn.colorpicker)
    //firebase
    implementation(platform(libs.firebase.bom))

    //unit tests
    testImplementation(libs.androidx.core)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.hamcrest)
    testImplementation(libs.truth)
    testImplementation(libs.turbine)
    testImplementation(libs.ktor.client.mock)
    //ui tests
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.ktor.client.mock.v237)
    debugImplementation(libs.ui.test.manifest)
}