val buildConfigFields = mapOf(
    "SECRET_KEY" to  project.property("SECRET_KEY") as String
)

extra.set("buildConfigFields", buildConfigFields)