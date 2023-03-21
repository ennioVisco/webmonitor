package at.ac.tuwien.trustcps.dsl

fun parseSelector(queryString: String): List<String> {
    try {
        val sanitized = queryString
            .replace("\\s+".toRegex(), " ")
            .split("$", "<=", ">=", "<", ">", "=", "&", limit = 3)
            .map { it.trim() }
        val isBinding = queryString.contains('&').toString()
        return if (sanitized.size < 2) {
            listOf(sanitized[0], "", "", isBinding)
        } else {
            sanitized + isBinding
        }
    } catch (e: Exception) {
        throw Exception("Error parsing selector: $queryString", e)
    }
}

