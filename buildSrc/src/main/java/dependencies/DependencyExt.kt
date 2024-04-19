package dependencies

val Dependency.notationWithVersion
    get() = buildString {
        append(notation)

        if (version != null) {
            append(":")
            append(version)
        }
    }
