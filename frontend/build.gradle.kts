import com.github.gradle.node.npm.task.NpmTask

plugins {
  id("com.github.node-gradle.node") version "7.0.2"
}

node {
  version.set("20.16.0") // Specify the Node.js version
  npmVersion.set("10.8.2") // Optionally specify the npm version
  download.set(true) // Automatically download and install the specified Node.js version
}

tasks {
  val npmInstalll by registering(NpmTask::class) {
    args.set(listOf("install"))
  }

  val npmBuild by registering(NpmTask::class) {
    dependsOn(npmInstalll)
    val activeProfile = project.findProperty("activeProfile")?.toString()
    args.set(listOf("run", "build", "--", "--configuration", activeProfile ?: "consumer-dev"))
  }

  val npmStart by registering(NpmTask::class) {
    dependsOn(npmBuild)
    val activeProfile = project.findProperty("activeProfile")?.toString()
    val port = project.findProperty("port")?.toString()
    args.set(listOf("start", "--", "--port", port ?: "8081", "--configuration", activeProfile ?: "consumer-dev"))
  }
}
