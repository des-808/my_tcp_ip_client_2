plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.tcp_ip_client_2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tcp_ip_client_2"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
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
    implementation(libs.preference)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)



    /*implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation 'androidx.appcompat:appcompat:1.7.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.2.0'
    implementation 'androidx.fragment:fragment:1.8.5'
    implementation 'androidx.annotation:annotation:1.9.1'
    implementation 'androidx.lifecycle:lifecycle-livedata:2.8.7'
    implementation 'androidx.lifecycle:lifecycle-viewmodel:2.8.7'
    implementation 'androidx.preference:preference:1.2.1'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.8.7'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7'
    implementation 'androidx.navigation:navigation-fragment:2.8.4'
    implementation 'androidx.navigation:navigation-ui:2.8.4'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test:runner:1.6.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.6.1'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
    implementation 'org.greenrobot:eventbus:3.1.1'
    implementation 'com.google.android.material:material:1.12.0'*/









}