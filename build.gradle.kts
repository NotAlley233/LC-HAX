plugins {
    id("net.weavemc.gradle") version "1.1.0"
}

group = "com.example"
version = "1.0.0"

weave {
    configure {
        name = "LC-HAX"
        modId = "lchax"
        entryPoints = listOf("com.example.mod.LCHax")
        hooks = listOf("com.example.mod.hook.MinecraftHook")
        mixinConfigs = listOf("lchax.mixins.json")
        mcpMappings()
    }
    version("1.8.9")
}

repositories {
    maven("https://repo.spongepowered.org/maven/")
    // Check available packages at https://gitlab.com/weave-mc/weave/-/packages/
    maven("https://gitlab.com/api/v4/projects/80566527/packages/maven")
    mavenCentral()
}

dependencies {
    implementation("net.weavemc:loader:1.1.0") // For advanced modifications
    implementation("net.weavemc:internals:1.1.0")
    implementation("net.weavemc.api:api:1.1.0")
    implementation("net.weavemc.api:api-v1_8:1.1.0") // For 1.8 events
    implementation("com.google.code.gson:gson:2.8.9")

    compileOnly("org.apache.logging.log4j:log4j-api:2.17.1")
    compileOnly("org.apache.logging.log4j:log4j-core:2.17.1")
    compileOnly("org.lwjgl.lwjgl:lwjgl:2.9.4-nightly-20150209")
    compileOnly("com.mojang:authlib:1.5.21")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("org.apache.logging.log4j:log4j-api:2.17.1")
    testImplementation("org.apache.logging.log4j:log4j-core:2.17.1")

    compileOnly("org.spongepowered:mixin:0.8.5")
}

java {
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
