package http.response

import http.exception.AccessProhibitedDirectoryException
import http.exception.NotFoundException
import http.request.HttpRequest
import http.response.body.HttpResponseBody
import http.response.body.HttpResponseBodyFactory
import http.response.header.HttpResponseContentType
import http.response.header.HttpResponseHeaderFactory
import http.response.header.lookupContentType
import util.FileHandler
import java.io.File
import java.io.IOException
import java.time.ZoneId
import java.time.ZonedDateTime


class HttpResponseFactory {

    @Throws(IOException::class, AccessProhibitedDirectoryException::class, NotFoundException::class)
    fun create(httpRequest: HttpRequest): HttpResponse {
        val path = httpRequest.requestPath
        val file = FileHandler.loadFileFromPath(path)
        if (!file.exists()) {
            throw NotFoundException(path + "のファイルが見つかりません")
        }

        if (file.isDirectory()) {
            val indexFile = FileHandler.loadIndexHtml(path)

            if (indexFile.exists()) {
                return buildHttpResonseWithFile(
                        httpRequest,
                        HttpResponseContentType.HTML,
                        indexFile
                )
            }

            return buildHttpResponseContentHTML(
                    HttpResponseStatusLine.OK,
                    HttpResponseBodyFactory.createFileListHtml(file)
            )
        }

        var contentType: HttpResponseContentType? = lookupContentType(file)
        if (contentType is HttpResponseContentType) {
            buildHttpResonseWithFile(httpRequest, contentType, file)
        }

        return buildHttpResponse(
                HttpResponseStatusLine.OK,
                HttpResponseBodyFactory.createFromFile(file)
        )
    }

    fun createNotModifiedHttpResponse(): HttpResponse {
        return buildHttpResponse(
                HttpResponseStatusLine.NOT_MODIFIED,
                HttpResponseBodyFactory.createBlankByte()
        )
    }

    /**
     * @return 403 Forbiddenを表したHttpResponseクラス
     */
    @Throws(IOException::class)
    fun createForbiddenHttpResponse(): HttpResponse {
        return buildHttpResponseContentHTML(
                HttpResponseStatusLine.FORBIDDEN,
                HttpResponseBodyFactory.createFromFile(FileHandler.FORBIDDEN_FILE)
        )
    }

    /**
     * @return 404 Not Foundを表したHttpResponseクラス
     * @throws IOException
     */
    @Throws(IOException::class)
    fun createNotFoundHttpResponse(): HttpResponse {
        return buildHttpResponseContentHTML(
                HttpResponseStatusLine.NOT_FOUND,
                HttpResponseBodyFactory.createFromFile(FileHandler.NOT_FOUND_FILE)
        )
    }

    /**
     * @return 500 Internal Server Errorを表したHttpResponseクラス
     */
    fun createInternalServerError(): HttpResponse {
        return buildHttpResponseContentHTML(
                HttpResponseStatusLine.INTERNAL_SERVER_ERROR,
                HttpResponseBodyFactory.createInternalServerError()
        )
    }

    /**
     * @return 501 NotImplementedを表したHttpResponseクラス
     * @throws IOException
     */
    @Throws(IOException::class)
    fun createNotImplementedHttpResponse(): HttpResponse {
        return buildHttpResponseContentHTML(
                HttpResponseStatusLine.NOT_IMPLEMENTED,
                HttpResponseBodyFactory.createFromFile(FileHandler.NOT_IMPLEMENTED_FILE)
        )

    }

    /**
     *
     * @return 505 HTTP Version Not Supportedを表したHttpResponseクラス
     * @throws IOException
     */
    @Throws(IOException::class)
    fun create505ExceptionHttpResponse(): HttpResponse {
        return buildHttpResponseContentHTML(
                HttpResponseStatusLine.HTTP_VERSION_NOT_SUPPORTED,
                HttpResponseBodyFactory.createFromFile(FileHandler.HTTP_VERSION_NOT_SUPPORTED_FILE)
        )
    }

    private fun buildHttpResponse(statusLine: HttpResponseStatusLine, httpResponseBody: HttpResponseBody): HttpResponse {
        val httpResponseHeader = HttpResponseHeaderFactory.create()
        return HttpResponse(statusLine, httpResponseHeader, httpResponseBody)
    }

    private fun buildHttpResponseContentHTML(statusLine: HttpResponseStatusLine, httpResponseBody: HttpResponseBody): HttpResponse {
        val httpResponseHeader = HttpResponseHeaderFactory.create(HttpResponseContentType.HTML, httpResponseBody.calcContentLength())
        return HttpResponse(statusLine, httpResponseHeader, httpResponseBody)
    }

    private fun buildHttpResonseWithFile(httpRequest: HttpRequest, contentType: HttpResponseContentType, file: File): HttpResponse {
        val lastModified = FileHandler.lookupLastModifiedDateTime(file)
        if (isNotModified(httpRequest, lastModified)) {
            return createNotModifiedHttpResponse()
        }

        val httpResponseBody = HttpResponseBodyFactory.createFromFile(file)
        val httpResponseHeader = HttpResponseHeaderFactory.create(
                contentType,
                httpResponseBody.calcContentLength(),
                lastModified
        )

        return HttpResponse(HttpResponseStatusLine.OK, httpResponseHeader, httpResponseBody)
    }

    private fun isNotModified(httpRequest: HttpRequest, fileModified: ZonedDateTime): Boolean {
        val ifModifiedSince = httpRequest.ifModifiedSince
        if (ifModifiedSince is ZonedDateTime) {
            if (ifModifiedSince.isAfter(ZonedDateTime.now(ZoneId.of("GMT")))) {
                return false
            }

            return ifModifiedSince.isAfter(fileModified) || ifModifiedSince.equals(fileModified)
        }

        return false
    }
}