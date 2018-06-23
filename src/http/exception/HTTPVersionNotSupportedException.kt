package http.exception

/**
 * 505 HTTP Version Not Supportedを表現した例外クラス
 */
class HTTPVersionNotSupportedException(s: String) : Exception(s)