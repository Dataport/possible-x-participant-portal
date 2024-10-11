
plugins {
	java
	alias(libs.plugins.springBoot)
	alias(libs.plugins.springDependencyManagement)
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
	mainClass.set("eu.possible_x.edc_orchestrator.EdcOrchestratorApplication")
	archiveBaseName.set("edc_orchestrator")
}

tasks.getByName<Jar>("jar") {
	enabled = false
}

tasks.register<Copy>("copyWebApp") {
  description = "Copies the GUI into the resources of the Spring project."
  group = "Application"
  from("$rootDir/consumer-provider-frontend/build/resources/browser")
  into(layout.buildDirectory.dir("resources/main/static/."))
}

tasks.named("compileJava") {
  dependsOn(":consumer-provider-frontend:npmBuild")
}

tasks.named("processResources") {
  dependsOn("copyWebApp")
}