package http.response.header

import sun.security.util.Length
import util.RFC1123DateTimeHandler
import java.time.ZonedDateTime

object HttpResponseHeaderFactory {

    fun create(): HttpResponseHeader {
        return HttpResponseHeader()
    }

    fun create(contentType: HttpResponseContentType, contentLength: Int): HttpResponseHeader {
        val responseContentLength = createContentLength(contentLength)
        return HttpResponseHeader(contentType, responseContentLength)
    }

    fun create(contentType: HttpResponseContentType, contentLength: Int, fileLastModified: ZonedDateTime): HttpResponseHeader {
        val responseContentLength = createContentLength(contentLength)
        val lastModified = createLastModified(fileLastModified)

        return HttpResponseHeader(contentType, responseContentLength, lastModified)
    }

    private fun createLastModified(fileLastModified: ZonedDateTime): String {
        return "Last-Modified: ${RFC1123DateTimeHandler.parseZonedDateTime(fileLastModified)}"
    }

    private fun createContentLength(contentLength: Int): String {
        return "Content-Length: ${contentLength}"
    }
}