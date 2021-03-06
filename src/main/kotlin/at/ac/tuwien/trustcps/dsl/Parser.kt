package at.ac.tuwien.trustcps.dsl

// TODO: currently doesn't recognize >= and <= correctly
fun parseSelector(queryString: String): List<String> {
    val sanitized = queryString
        .replace("\\s+".toRegex(), " ")
        .split('$', '<', '>', '=', limit = 3)
        .map { it.trim() }
    return if (sanitized.size < 2) {
        listOf(sanitized[0], "", "")
    } else {
        sanitized
    }
}

