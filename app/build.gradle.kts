plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.tepinhui"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.tepinhui"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // 使用传统的字符串依赖，避免版本目录问题
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.activity:activity:1.11.0")  // 添加 activity 依赖
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")

    // 测试依赖
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // 图片加载库 - 只保留一个（建议保留 Glide）
    implementation("com.github.bumptech.glide:glide:4.15.1")
    // 注释掉 Picasso 以避免依赖冲突
    // implementation("com.squareup.picasso:picasso:2.8")

    // 本地 JAR 文件
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

// 添加配置来排除冲突的依赖
configurations.all {
    resolutionStrategy {
        // 强制使用特定版本的依赖
        force("com.squareup.okhttp3:okhttp:4.11.0")
        force("com.squareup.okio:okio:3.4.0")

        // 排除冲突的模块
        exclude(group = "com.squareup.okhttp", module = "okhttp")
        exclude(group = "com.squareup.okio", module = "okio")
    }
}