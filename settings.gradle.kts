rootProject.name = "possible-x-edc-management"

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("libs.versions.toml")) }
  }
}

include("edc-orchestrator", "consumer-provider-frontend")