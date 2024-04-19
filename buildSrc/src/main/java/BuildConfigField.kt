data class BuildConfigField(
    val type: String,
    val name: String,
    val releaseValue: String,
    val debugValue: String,
    val includeManifestPlaceholder: Boolean = false,
)
