package http.response.header

import constants.JavaEscapeSequenceConstants.CRLF
import util.RFC1123DateTimeHandler

internal class HttpResponseHeader(private val contentType : HttpResponseContentType? = null, private val contentLength : String? = null, private val lastModified : String? = null) {
    private val date = setDate()

    fun buildByteArrayRespnoseHeader(): ByteArray {
        var httpResponseHeaderStr = date + CRLF
        if (contentType == null) {
            return httpResponseHeaderStr.toByteArray()
        }

        if (contentLength is String) {
            httpResponseHeaderStr = httpResponseHeaderStr + contentLength + CRLF + contentType.getString() + CRLF
        }

        return if (lastModified is String) {
            (httpResponseHeaderStr + lastModified + CRLF).toByteArray()
        } else httpResponseHeaderStr.toByteArray()
    }

    private fun setDate(): String {
        return "Date: " + RFC1123DateTimeHandler.nowDateTimeStr()
    }
}








