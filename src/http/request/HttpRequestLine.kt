package http.request

/**
 * HttpRequestのRequestLineを表現したクラス
 */
class HttpRequestLine(val method: String, val path: String, private val version: String)
