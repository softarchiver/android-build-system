buildscript {
	repositories {
		mavenCentral()
	}

	dependencies {
		classpath 'com.android.tools.build:gradle:${gradleVersion}'
	}
}

apply plugin: 'android'
android {
	compileSdkVersion '${target}'
	buildToolsVersion '${buildTools}'

	defaultConfig {
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
			zipAlignEnabled true
			minifyEnabled false
			proguardFile getDefaultProguardFile('proguard-android.txt')
		}
	}
}

dependencies {
	<#if libs?has_content>
    	<#list libs as lib>
    compile '${lib}'
    	</#list>
	</#if>
}
