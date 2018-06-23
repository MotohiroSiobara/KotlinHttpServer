package http.request

import java.time.ZonedDateTime

/**
 * このクラスはHttpリクエストのデータをまとめたクラスです
 * RFCの基準に乗っ取ったHttpリクエストを模したものである
 * https://triple-underscore.github.io/RFC2616-ja.html#section-5
 * @param requestMethod 例 GET request-lineに置けるmethodの部分
 * @param requestPath GET /dir/index.html HTTP1.1のようなRFC標準のリクエストラインに含まれる /dir/index.htmlの部分
 */
class HttpRequest(private val requestMethod: String, val requestPath: String, val ifModifiedSince: ZonedDateTime? = null) {

    /**
     * @return requestMethodがHEADかどうかを返す
     */
    val isHeadRequestMethod: Boolean get() = requestMethod == "HEAD"
}