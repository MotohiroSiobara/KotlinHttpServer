package http.response

import http.response.body.HttpResponseBody
import http.response.header.HttpResponseHeader

internal class HttpResponse(val statusLine : String, val header : HttpResponseHeader, val body : HttpResponseBody) {

    fun buildByteArrayResponse(): ByteArray {
        return buildByteArrayResponseOnlyHeader().plus(body.body)
    }

    fun buildByteArrayResponseOnlyHeader(): ByteArray {
        return statusLine.toByteArray().plus(header.buildByteArrayRespnoseHeader())
    }
}