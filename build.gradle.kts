import buildlogic.Utils

plugins {
    id("build.fabric")
    id("build.publish")
}

Utils.setupResources(project, rootProject, "fabric.mod.json")

dependencies {

    minecraft("com.mojang:minecraft:${project.properties["minecraft-version"]}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${project.properties["fabric-loader-version"]}")

    // Fabric API
    listOf(
        "fabric-networking-api-v1"
    ).forEach {
        modApi(include(fabricApi.module(it, "${project.properties["fabric-api-version"]}"))!!)
    }

    compileOnly(libs.jetbrains.annotations)

    modImplementation("org.wallentines:midnightcfg-platform-minecraft:3.4.0")

    modImplementation(libs.semver)
    include(libs.semver)

    modImplementation("org.wallentines:databridge:0.8.1")
}