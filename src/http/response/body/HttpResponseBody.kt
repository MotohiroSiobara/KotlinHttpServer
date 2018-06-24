package http.response.body

internal class HttpResponseBody(val body : ByteArray) {

    fun calcContentLength(): Int {
        return body.size
    }
}