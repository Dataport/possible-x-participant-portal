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
    args.set(listOf("clean-install"))
  }

  val npmBuild by registering(NpmTask::class) {
    dependsOn(npmInstalll)
    args.set(listOf("run", "build", "--", "--configuration", project.findProperty("npmEnv") as String? ?: "consumer-dev"))
  }
}
