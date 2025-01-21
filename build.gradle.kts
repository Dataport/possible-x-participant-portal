tasks.register<Exec>("startFull") {
  description = "Runs the full application (frontend + backend)."
  group = "application"

  val activeProfile = project.findProperty("activeProfile")?.toString()

  print("activeProfile: $activeProfile")

  dependsOn(":frontend:startFrontend")
  dependsOn(":backend:startBackend")
}