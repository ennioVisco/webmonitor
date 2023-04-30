package at.ac.tuwien.trustcps.cli

import at.ac.tuwien.trustcps.reporting.*
import javax.script.*

class Cli(
    args: Array<String>,
    toConsole: Boolean = false,
    toFile: Boolean = false,
    exec: Cli.(Reporter) -> Unit
) {
    val report = Reporter(toConsole, toFile)
    private val log = mu.KotlinLogging.logger {}

    init {
        report.use {
            validateArgs(args)
            exec(it)
        }
    }

    private fun validateArgs(args: Array<String>) {
        try {
            val (source, spec) = args
            loadScripts(source, spec)
        } catch (e: ArrayIndexOutOfBoundsException) {
            try {
                val (source) = args
                loadScripts(source, spec = source)
            } catch (e: ArrayIndexOutOfBoundsException) {
                log.info("No arguments provided, using default scripts")
                loadScripts(source = "sample", spec = "sample")
            }
        } catch (e: NullPointerException) {
            if (args.size > 1)
                log.error(errorMsg(source = args[0], spec = args[1]))
            else
                log.error(errorMsg(source = args[0], spec = args[0]))
        }
    }

    private fun errorMsg(source: String, spec: String) =
        "Cannot find any files named 'source.${source}.kts' and" +
                " 'spec.${spec}.kts'. Please make sure that the files exist " +
                "and are in the 'resources' directory."

    private fun loadScripts(source: String, spec: String) {
        with(ScriptEngineManager().getEngineByExtension("kts")) {
            eval(loadResource("source.$source.kts"))
            eval(loadResource("spec.$spec.kts"))
        }
    }

    private fun loadResource(name: String) =
        object {}.javaClass.classLoader.getResourceAsStream(name)
            ?.bufferedReader()

}
