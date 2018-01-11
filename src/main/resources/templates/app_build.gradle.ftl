apply plugin: 'com.android.application'

android {
    compileSdkVersion 21
    buildToolsVersion "21.1.2"

    defaultConfig {
        applicationId "${packageName}"
        minSdkVersion ${minSdk}
        targetSdkVersion ${targetSdk}
        versionCode ${versionCode}
        versionName "${versionName}"
    }

    signingConfigs {
        release {
            storeFile file("${keystore}")
            storePassword "${storepass}"
            keyAlias "${key}"
            keyPassword "${storepass}"
        }
    }


    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.google.android.gms:play-services:6.5.87'
}
