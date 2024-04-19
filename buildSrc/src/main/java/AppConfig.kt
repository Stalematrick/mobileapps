import org.gradle.api.Project
import java.util.Properties

object AppConfig {
    const val applicationId = "ru.abramovkirill.languageapp"
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    const val jvmTarget = "1.8"
    const val composeKotlinCompilerExtensionVersion = "1.5.11"

    fun buildConfigFields(project: Project): Array<BuildConfigField> {
        val properties = Properties()

        properties.load(project.rootProject.file("local.properties").inputStream())

        val supaBaseAnonKey = properties.getProperty("supabase.anon.key")
        val supaBaseUrl = properties.getProperty("supabase.url")
        val supaBasePathStorage = properties.getProperty("supabase.path.storage")
        val supaBaseRedirectScheme = properties.getProperty("supabase.redirect.scheme")
        val supaBaseRedirectHost = properties.getProperty("supabase.redirect.host")
        val googleServerClientId = properties.getProperty("google.server.client_id")

        return arrayOf(
            BuildConfigField(
                type = "String",
                name = "APPLICATION_ID",
                releaseValue = applicationId,
                debugValue = applicationId,
            ),
            BuildConfigField(
                type = "String",
                name = "SUPABASE_ANON_KEY",
                releaseValue = supaBaseAnonKey,
                debugValue = supaBaseAnonKey,
            ),
            BuildConfigField(
                type = "String",
                name = "SUPABASE_URL",
                releaseValue = supaBaseUrl,
                debugValue = supaBaseUrl,
            ),
            BuildConfigField(
                type = "String",
                name = "SUPABASE_STORAGE_URL",
                releaseValue = "$supaBaseUrl/$supaBasePathStorage",
                debugValue = "$supaBaseUrl/$supaBasePathStorage",
            ),
            BuildConfigField(
                type = "String",
                name = "GOOGLE_SERVER_CLIENT_ID",
                releaseValue = googleServerClientId,
                debugValue = googleServerClientId,
            ),
            BuildConfigField(
                type = "String",
                name = "SUPABASE_REDIRECT_SCHEME",
                releaseValue = supaBaseRedirectScheme,
                debugValue = supaBaseRedirectScheme,
                includeManifestPlaceholder = true,
            ),
            BuildConfigField(
                type = "String",
                name = "SUPABASE_REDIRECT_HOST",
                releaseValue = supaBaseRedirectHost,
                debugValue = supaBaseRedirectHost,
                includeManifestPlaceholder = true,
            ),
        )
    }

    object Sdk {
        const val compile = 34
        const val min = 30
        const val target = 34
    }

    object Version {
        const val code = 1
        const val name = "1.0"
    }
}