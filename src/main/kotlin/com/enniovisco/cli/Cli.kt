package com.enniovisco.cli

import com.enniovisco.reporting.*
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
            when (args.size) {
                0 -> defaultScript()
                1 -> oneArgument(args)
                2 -> twoArguments(args)
                else -> log.info("Using custom scripts")
            }
        } catch (e: NullPointerException) {
            if (args.size > 1)
                log.error(errorMsg(source = args[0], spec = args[1]))
            else
                log.error(errorMsg(source = args[0], spec = args[0]))
        }
    }

    private fun defaultScript() {
        log.info("No arguments provided, using default scripts")
        loadScripts(source = "sample", spec = "sample")
    }

    private fun oneArgument(args: Array<String>) {
        val (source) = args
        log.info(
            "Using 'source.$source.kts' as source script and " +
                    "'spec.$source.kts' as spec script"
        )
        loadScripts(source = source, spec = source)
    }

    private fun twoArguments(args: Array<String>) {
        val (source, spec) = args
        log.info(
            "Using 'source.$source.kts' as source script and " +
                    "'spec.$spec.kts' as spec script"
        )
        loadScripts(source = source, spec = spec)
    }

    private fun errorMsg(source: String, spec: String) =
        "Cannot find any files named 'source.$source.kts' and" +
                " 'spec.$spec.kts'. Please make sure that the files exist " +
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
