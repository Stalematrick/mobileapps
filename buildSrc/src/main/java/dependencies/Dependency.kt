package dependencies

data class Dependency(
    val notation: String,
    val version: String? = null,
    val isPlatform: Boolean = false,
)
