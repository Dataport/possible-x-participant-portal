/*
 *  Copyright 2024-2025 Dataport. All rights reserved. Developed as part of the POSSIBLE project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import cz.habarta.typescript.generator.JsonLibrary
import cz.habarta.typescript.generator.TypeScriptFileType
import cz.habarta.typescript.generator.TypeScriptOutputKind

plugins {
  java
  jacoco
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
  testImplementation(libs.wiremockStandalone)
  testImplementation(libs.springBootStarterTest)
  testImplementation(libs.springSecurityTest)
  testImplementation(libs.reactorTest)
  implementation(libs.springBootStarterActuator)
  implementation(libs.springBootStarterWeb)
  implementation(libs.springBootStarterWebflux)
  implementation(libs.springBootStarterSecurity)
  implementation(libs.springBootStarterValidation)
  implementation(libs.openApi)
  implementation(libs.titaniumJsonLd)
  implementation(libs.jakartaJson)
  implementation(libs.mapStruct)
  compileOnly(libs.lombok)
  annotationProcessor(libs.lombokMapStructBinding)
  annotationProcessor(libs.mapStructProcessor)
  annotationProcessor(libs.lombok)
  annotationProcessor(libs.therApiScribe)
  developmentOnly(libs.springBootDevtools)
  runtimeOnly(libs.therApi)
  testRuntimeOnly(libs.jUnit)
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}


tasks.bootJar {
  mainClass.set("eu.possiblex.participantportal.ParticipantPortalApplication")
  archiveBaseName.set("backend")
}

tasks.getByName<Jar>("jar") {
  enabled = false
}

tasks.register("buildBackend") {
  dependsOn(":backend:build")
  description = "Builds the backend application."
  group = "build"
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

  test {
    finalizedBy(jacocoTestReport) // report is always generated after tests run
  }

  jacocoTestReport {
    dependsOn(test) // tests are required to run before generating the report
  }
}