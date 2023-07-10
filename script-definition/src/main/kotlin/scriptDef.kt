import kotlinx.coroutines.*
import kotlin.script.experimental.annotations.*
import kotlin.script.experimental.api.*
import kotlin.script.experimental.dependencies.*
import kotlin.script.experimental.dependencies.maven.*
import kotlin.script.experimental.jvm.*

@KotlinScript(
    fileExtension = "webmonitor.kts",
    compilationConfiguration = ScriptWithMavenDepsConfiguration::class
)
abstract class ScriptWithMavenDeps

object ScriptWithMavenDepsConfiguration : ScriptCompilationConfiguration(
    {
        // Implicit imports for all scripts of this type
        defaultImports(
            "org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3",
            "kotlinx.html.*",
            "kotlinx.html.attributes.*",
            "kotlinx.html.stream.*"
        )
        defaultImports(DependsOn::class, Repository::class)
        jvm {
            // Extract the whole classpath from context classloader and use it as dependencies
            dependenciesFromCurrentContext(wholeClasspath = true)
        }
        // Callbacks
        refineConfiguration {
            // Process specified annotations with the provided handler
            onAnnotations(
                DependsOn::class,
                Repository::class,
                handler = ::configureMavenDepsOnAnnotations
            )
        }
    }
)

// Handler that reconfigures the compilation on the fly
fun configureMavenDepsOnAnnotations(context: ScriptConfigurationRefinementContext): ResultWithDiagnostics<ScriptCompilationConfiguration> {
    val annotations =
        context.collectedData?.get(ScriptCollectedData.collectedAnnotations)
            ?.takeIf { it.isNotEmpty() }
            ?: return context.compilationConfiguration.asSuccess()
    return runBlocking {
        resolver.resolveFromScriptSourceAnnotations(annotations)
    }.onSuccess {
        context.compilationConfiguration.with {
            dependencies.append(JvmDependency(it))
        }.asSuccess()
    }
}

private val resolver = CompoundDependenciesResolver(
    FileSystemDependenciesResolver(),
    MavenDependenciesResolver()
)
