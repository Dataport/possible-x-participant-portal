import cz.habarta.typescript.generator.JsonLibrary
import cz.habarta.typescript.generator.TypeScriptFileType
import cz.habarta.typescript.generator.TypeScriptOutputKind

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
  group = "application"
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

tasks.register<JavaExec>("startFull") {
  dependsOn("startFrontend")
  dependsOn("startBackend")
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