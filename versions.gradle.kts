mapOf(
    "kotlinVersion" to "1.4.20",
    "junitVersion" to "5.7.0",
    "jetBrainsAnnotationsVersion" to "20.1.0",
    "assertjVersion" to "3.11.1",
    "mockitoVersion" to "3.7.7"
).forEach { (name, version) ->
    project.extra.set(name, version)
}