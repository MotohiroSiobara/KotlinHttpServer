package http.request

/**
 * HttpRequestのRequestLineを表現したdata class
 */
data class HttpRequestLine(val method: String, val path: String, private val version: String)
