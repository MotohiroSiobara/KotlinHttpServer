package http.response

enum class HttpResponseStatusLine(val statusLine: String) {

    OK("200 OK"),
    NOT_MODIFIED("304 Not Modified"),
    BAD_REQUEST("400 Bad Request"),
    FORBIDDEN("403 Forbidden"),
    NOT_FOUND("404 Not Found"),
    INTERNAL_SERVER_ERROR("500 Internal Server Error"),
    NOT_IMPLEMENTED("501 Not Implemented"),
    HTTP_VERSION_NOT_SUPPORTED("505 HTTP Version Not Supported");

    fun getString(): String {
        return "HTTP/1.1${statusLine}"
    }
}