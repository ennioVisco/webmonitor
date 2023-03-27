package at.ac.tuwien.trustcps.dsl

private val log = mu.KotlinLogging.logger {}

fun parseSelector(queryString: String): List<String> {
//    log.info("Parsing selector: $queryString")
    try {
        val sanitized = queryString
            .replace("\\s+".toRegex(), " ")
            .split("$", "<=", ">=", "<", ">", "==", "&", limit = 3)
            .map { it.trim() }
        val isBinding = queryString.contains('&').toString()
//        log.info("Parsed selector: $sanitized (binding: $isBinding)")
        return if (sanitized.size < 2) {
            listOf(sanitized[0], "", "", isBinding)
        } else {
            sanitized + isBinding
        }
    } catch (e: Exception) {
        throw Exception("Error parsing selector: $queryString", e)
    }
}
