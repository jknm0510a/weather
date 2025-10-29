import java.util.Properties
import java.io.FileInputStream
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}
android {
    namespace = "com.on.network"
    compileSdk = 36
    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // 從 localProperties 讀取 token，如果找不到就給一個預設的空值
        val apiToken = localProperties.getProperty("config.token", "")
        buildConfigField("String", "TOKEN", "\"$apiToken\"")

        // 從 localProperties 讀取 url，如果找不到就給一個預設的空值
        val apiUrl = localProperties.getProperty("config.url", "")
        buildConfigField("String", "BASE_URL", "\"$apiUrl\"")
    }

    buildTypes {
        debug {
            buildConfigField("Boolean", "ENABLE_LOG", "true")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("Boolean", "ENABLE_LOG", "false")
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    api(libs.retrofit)
    implementation(libs.startup)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.logging.interceptor)
    implementation(libs.koin)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}