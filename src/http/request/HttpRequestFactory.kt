package http.request

import constants.JavaEscapeSequenceConstants.LF
import constants.JavaEscapeSequenceConstants.SP
import http.exception.HTTPVersionNotSupportedException
import http.exception.IllegalHttpRequestException
import http.exception.NotImplementedException
import util.RFC1123DateTimeHandler
import java.io.BufferedReader
import java.io.IOException
import java.time.ZonedDateTime
import java.util.*

/**
 * このクラスはHttpRequestクラスを作成するためのクラスです
 * インスタンス化することはできません
 */
object HttpRequestFactory {

    /**
     * [@param bufferedReader]の中身を取り出してHttpRequestのインスタンスを返します
     * @param bufferedReader BufferedReader クライアントから渡されたrequest情報が含まれています
     * @return HttpRequest [@param bufferedReader]の中身を取り出して生成したHttpRequest
     */
    @Throws(IOException::class, IllegalHttpRequestException::class, NotImplementedException::class, HTTPVersionNotSupportedException::class)
    fun create(bufferedReader: BufferedReader): HttpRequest {
        val httpRequestLine: HttpRequestLine
        val readLines : List<String>

        try {
            readLines = bufferedReader.readLines()
            if (readLines.isEmpty()) {
                throw IllegalHttpRequestException("空のリクエストが投げられました")
            }

            httpRequestLine = createHttpRequestLine(readLines[0])
        } catch (e: IOException) {
            throw IOException("HttpRequestFactory#create" + LF + "リクエストの読み込み中にエラーが発生")
        }

        val method = httpRequestLine.method
        val path = httpRequestLine.path
        val ifModifiedSince = getIfModifiedSince(readLines)
        return if (ifModifiedSince is ZonedDateTime) {
            HttpRequest(method, path, ifModifiedSince)
        } else HttpRequest(method, path)
    }

    @Throws(IllegalHttpRequestException::class, NotImplementedException::class, HTTPVersionNotSupportedException::class)
    private fun createHttpRequestLine(line: String): HttpRequestLine {
        val listOfRequestLine = line.split(SP)
        if (listOfRequestLine.size != 3) {
            throw IllegalHttpRequestException("Request-Lineの値が不正です")
        }

        val (method, path, version) = listOfRequestLine
        if (version != "HTTP/1.1") {
            throw HTTPVersionNotSupportedException("このサーバーはHTTP/1.1にしか対応していません")
        }

        return if (method == "GET" || method == "HEAD") {
            HttpRequestLine(method, path, version)
        } else {
            throw NotImplementedException("GETもしくはHEADメソッドのみしか実装されていません")
        }
    }

    private fun getIfModifiedSince(linesOfRequest: List<String>): ZonedDateTime? {
        val ifModifiedSince = linesOfRequest.find { line -> line.startsWith("If-Modified-Since:") }
        if (ifModifiedSince is String) {
            return RFC1123DateTimeHandler.parseStrDateTime(ifModifiedSince)
        }

        return null
    }
}