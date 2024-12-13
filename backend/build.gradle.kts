import cz.habarta.typescript.generator.JsonLibrary
import cz.habarta.typescript.generator.TypeScriptFileType
import cz.habarta.typescript.generator.TypeScriptOutputKind
import org.gradle.internal.classpath.Instrumented.systemProperty
import org.yaml.snakeyaml.Yaml

plugins {
  java
  alias(libs.plugins.springBoot)
  alias(libs.plugins.springDependencyManagement)
  alias(libs.plugins.typescriptGenerator)
}

group = "eu.possiblex"
version = "0.0.1"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(17)
  }
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(libs.springBootStarterActuator)
  implementation(libs.springBootStarterWeb)
  implementation(libs.springBootStarterWebflux)
  implementation(libs.openApi)
  implementation(libs.titaniumJsonLd)
  implementation(libs.jakartaJson)
  compileOnly(libs.lombok)
  annotationProcessor(libs.lombokMapStructBinding)
  implementation(libs.mapStruct)
  annotationProcessor(libs.mapStructProcessor)
  developmentOnly(libs.springBootDevtools)
  runtimeOnly(libs.therApi)
  annotationProcessor(libs.lombok)
  annotationProcessor(libs.therApiScribe)
  testImplementation(libs.springBootStarterTest)
  testImplementation(libs.reactorTest)
  testRuntimeOnly(libs.jUnit)
  testImplementation("org.wiremock:wiremock-standalone:3.9.2")
}

tasks.withType<Test> {
  useJUnitPlatform()
}


tasks.bootJar {
  mainClass.set("eu.possiblex.participantportal.ParticipantPortalApplication")
  archiveBaseName.set("backend")
}

tasks.getByName<Jar>("jar") {
  enabled = false
}

tasks.register<Exec>("startFrontend") {
  description = "Starts the frontend application."
  group = "build"
  workingDir = file("$rootDir/frontend")
  dependsOn(":frontend:npmStart")
}

tasks.register<JavaExec>("startBackend") {
  dependsOn("bootJar")
  description = "Runs the backend application."
  group = "application"
  mainClass.set("eu.possiblex.participantportal.ParticipantPortalApplication")
  classpath = sourceSets["main"].runtimeClasspath
  val activeProfile = project.findProperty("activeProfile")?.toString()
  if (activeProfile != null) {
    systemProperty("spring.profiles.active", activeProfile)
  }
}

tasks.named<JavaExec>("bootRun") {
  val activeProfile = project.findProperty("activeProfile")?.toString()
  if (activeProfile != null) {
      systemProperty("spring.profiles.active", activeProfile)
  }
  val yaml = Yaml()
  val yamlFileName = if (activeProfile != null) "application-$activeProfile.yml" else "application.yml"
  val applicationYaml = file("$rootDir/backend/src/main/resources/$yamlFileName")
  val config = yaml.load<Map<String, Any>>(applicationYaml.inputStream())

  val serverPort = (config["server"] as? Map<String, Any>)?.get("port") ?: "8080"
  val incrementedPort = serverPort.toString().toInt().plus(1).toString()
  dependsOn("startBackend")
  doFirst {
    Thread {
      exec {
        workingDir = file("$rootDir")
        commandLine("./gradlew", "startFrontend", "-PactiveProfile=$activeProfile", "-Pport=$incrementedPort")
      }
    }.start()
  }
}

tasks {
  generateTypeScript {
    jsonLibrary = JsonLibrary.jackson2
    outputKind = TypeScriptOutputKind.module
    outputFileType = TypeScriptFileType.implementationFile
    scanSpringApplication = true
    generateSpringApplicationClient = true
    addTypeNamePrefix = "I"
    classPatterns = listOf(
      "eu.possiblex.participantportal.application.entity.**",
      "eu.possiblex.participantportal.application.boundary.**",
      "eu.possiblex.participantportal.business.entity.selfdescriptions.**"
    )
    outputFile = "../frontend/src/app/services/mgmt/api/backend.ts"
    noFileComment = true
  }
}