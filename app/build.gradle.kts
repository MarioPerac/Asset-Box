import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "org.unibl.etf.mr.assetbox"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.unibl.etf.mr.assetbox"
        minSdk = 26
        targetSdk = 34
        versionCode = 100
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        properties.load(FileInputStream(rootProject.file("local.properties")))

        buildConfigField("String", "MAPS_API_KEY", "\"${properties.getProperty("MAPS_API_KEY")}\"")


    }

    buildTypes {
        getByName("release") {
            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type. Make sure to use a build
            // variant with `isDebuggable=false`.
            isMinifyEnabled = false
            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.

            proguardFiles(
                    // Includes the default ProGuard rules files that are packaged with
                    // the Android Gradle plugin. To learn more, go to the section about
                    // R8 configuration files.
                    getDefaultProguardFile("proguard-android-optimize.txt"),

                    // Includes a local, custom Proguard rules file
                    "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.gridlayout)
    implementation(libs.legacy.support.v4)
    implementation(libs.recyclerview)
    implementation(libs.camera.core)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.google.android.libraries.places:places:2.4.0")
    implementation("com.android.volley:volley:1.2.1")

    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    implementation("com.github.dhaval2404:imagepicker:2.1")

    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

}