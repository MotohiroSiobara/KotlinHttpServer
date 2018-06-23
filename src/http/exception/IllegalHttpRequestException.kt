package http.exception

/**
 * RFCに準拠していないHttpリクエストが送られた場合に発生
 */
class IllegalHttpRequestException(s: String) : Exception(s)