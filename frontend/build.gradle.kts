import com.github.gradle.node.npm.task.NpmTask
import org.yaml.snakeyaml.Yaml

plugins {
  id("com.github.node-gradle.node") version "7.1.0"
}

buildscript {
  dependencies {
    classpath("org.yaml:snakeyaml:1.29")
  }
}

node {
  version.set("20.16.0") // Specify the Node.js version
  npmVersion.set("10.8.2") // Optionally specify the npm version
  download.set(true) // Automatically download and install the specified Node.js version
}

tasks {

  val testFrontend by registering(NpmTask::class) {
    args.set(listOf("run", "test", "--", "--no-watch", "--no-progress", "--browsers=ChromeHeadlessNoSandbox"))
  }

  val buildFrontend by registering(NpmTask::class) {
    dependsOn(npmInstall)
    dependsOn(testFrontend)
    val activeProfile = project.findProperty("activeProfile")?.toString()
    args.set(listOf("run", "build", "--", "--configuration", activeProfile ?: "remote"))
  }

  val setNpmShell by registering(NpmTask::class) {
    args.set(listOf("config", "set", "script-shell", "/bin/bash"))
  }

  val startFrontend by registering(NpmTask::class) {
    dependsOn(setNpmShell)
    dependsOn(npmInstall)

    val activeProfile = project.findProperty("activeProfile")?.toString()


    var port = project.findProperty("frontendPort")?.toString()
    if (port == null) {

      val yaml = Yaml()
      val yamlFileName = if (activeProfile != null) "application-$activeProfile.yml" else "application.yml"
      val applicationYaml = file("$rootDir/backend/src/main/resources/$yamlFileName")
      val config = yaml.load<Map<String, Any>>(applicationYaml.inputStream())

      val backendPort = (config["server"] as? Map<*, *>)?.get("port") ?: "8080"
      port = "420" + backendPort.toString().last()
    }

    args.set(listOf("start", "--", "--port", port, "--configuration", activeProfile ?: "remote"))
  }

  val npmFeTest by registering(NpmTask::class) {
    outputs.upToDateWhen { false }
    dependsOn(npmInstall)
    args.set(listOf("run", "test", "--", "--no-watch", "--no-progress", "--browsers=ChromeHeadlessNoSandbox"))
  }
}
