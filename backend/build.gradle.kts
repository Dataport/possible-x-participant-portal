import cz.habarta.typescript.generator.JsonLibrary
import cz.habarta.typescript.generator.TypeScriptFileType
import cz.habarta.typescript.generator.TypeScriptOutputKind

plugins {
  java
  alias(libs.plugins.springBoot)
  alias(libs.plugins.springDependencyManagement)
  alias(libs.plugins.typescriptGenerator)
}

group = "eu.possible-x"
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
  compileOnly(libs.lombok)
  developmentOnly(libs.springBootDevtools)
  runtimeOnly(libs.therApi)
  annotationProcessor(libs.lombok)
  annotationProcessor(libs.therApiScribe)
  testImplementation(libs.springBootStarterTest)
  testImplementation(libs.reactorTest)
  testRuntimeOnly(libs.jUnit)
}

tasks.withType<Test> {
  useJUnitPlatform()
}


tasks.bootJar {
  mainClass.set("eu.possible_x.backend.BackendApplication")
  archiveBaseName.set("backend")
}

tasks.getByName<Jar>("jar") {
  enabled = false
}

tasks.register<Copy>("copyWebApp") {
  description = "Copies the GUI into the resources of the Spring project."
  group = "Application"
  from("$rootDir/frontend/build/resources/browser")
  into(layout.buildDirectory.dir("resources/main/static/."))
}

tasks.named("compileJava") {
  dependsOn(":frontend:npmBuild")
}

tasks.named("processResources") {
  dependsOn("copyWebApp")
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
      "eu.possible_x.backend.application.entity.*",
      "eu.possible_x.backend.application.boundary.*"
    )
    outputFile = "../frontend/src/app/services/mgmt/api/backend.ts"
    noFileComment = true
  }
}