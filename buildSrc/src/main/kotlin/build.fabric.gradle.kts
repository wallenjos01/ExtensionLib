import buildlogic.Utils
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapSourcesJarTask

plugins {
    id("build.common")
    id("build.shadow")
    id("fabric-loom")
    id("com.gradleup.shadow")
}

sourceSets {
    register("testmod") {

        val main = sourceSets.main.get()

        compileClasspath += main.compileClasspath
        runtimeClasspath += main.runtimeClasspath
    }
}

loom {
    mods {
        register(project.name + "-testmod") {
            sourceSet(sourceSets["testmod"])
        }
    }
    runs {
        getByName("client") {
            runDir = "run/client"
            ideConfigGenerated(false)
            client()
        }
        getByName("server") {
            runDir = "run/server"
            ideConfigGenerated(false)
            server()
        }
        register("testmodClient") {
            client()
            ideConfigGenerated(false)
            runDir = "run/testclient"
            name = "Testmod Client"
            source(sourceSets.getByName("testmod"))
        }
        register("testmodServer") {
            server()
            ideConfigGenerated(false)
            runDir = "run/testserver"
            name = "Testmod Server"
            source(sourceSets.getByName("testmod"))
        }
    }
    mixin {
        defaultRefmapName = "${rootProject.name}.refmap.json"
    }
}

dependencies {
    "testmodImplementation"(sourceSets.main.get().output)
}

val archiveName = Utils.getArchiveName(project, rootProject)

val finalShadow = tasks.register<ShadowJar>("finalShadow") {

    val remapJar = tasks["remapJar"]
    dependsOn(remapJar)
    from(remapJar)

    archiveClassifier.set("")
    archiveBaseName.set(archiveName)

    configurations = listOf(project.configurations["shadow"])
}

tasks.named("build") {
    dependsOn(finalShadow)
}

tasks.named<Jar>("jar") {
    archiveBaseName.set(archiveName)
    archiveClassifier.set("partial-dev")
}

tasks.named<RemapJarTask>("remapJar") {
    archiveBaseName.set(archiveName)
    archiveClassifier.set("partial")
    inputFile.set(tasks.named<Jar>("jar").get().archiveFile)
}

tasks.named<RemapSourcesJarTask>("remapSourcesJar") {
    archiveBaseName.set(archiveName)
    archiveClassifier.set("sources")
}

tasks.named<ShadowJar>("shadowJar") {
    enabled = false
}
