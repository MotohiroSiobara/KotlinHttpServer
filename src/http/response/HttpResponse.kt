package http.response

import http.response.body.HttpResponseBody
import http.response.header.HttpResponseHeader

internal class HttpResponse(val statusLine : HttpResponseStatusLine, val header : HttpResponseHeader, val body : HttpResponseBody) {

    fun buildByteArrayResponse(): ByteArray {
        return buildByteArrayResponseOnlyHeader().plus(body.body)
    }

    fun buildByteArrayResponseOnlyHeader(): ByteArray {
        return statusLine.toString().toByteArray().plus(header.buildByteArrayRespnoseHeader())
    }
}