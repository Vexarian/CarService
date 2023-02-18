plugins {
    id("com.android.application") version Versions.androidPlugin apply false
    id("org.jetbrains.kotlin.android") version Versions.kotlin apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}