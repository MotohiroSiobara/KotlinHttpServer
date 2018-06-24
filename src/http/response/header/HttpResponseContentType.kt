package http.response.header

import util.FileHandler
import java.io.File

enum class HttpResponseContentType(val contentType : String, val extensions : List<String>) {

    HTML("text/html;charset=utf-8", listOf(".html")),
    JS("application/javascript;charset=utf-8", listOf(".js")),
    CSS("text/css;charset=utf-8", listOf(".css")),
    JPEG("image/jpeg", listOf(".jpeg", ".jpg")),
    PNG("image/png", listOf(".png")),
    GIF("image/gif", listOf(".gif")),
    PDF("application/pdf", listOf(".pdf"));

    fun getString(): String {
        return "Content-Type: " + contentType
    }

    /**
     *  @param file File コンテントタイプを判定したいファイルを渡す
     *  @return {@code file}のコンテントタイプをもったクラス
     */
    fun lookupContentType(file : File) : HttpResponseContentType? {
        val fileExtension : String = FileHandler.lookupExtension(file)
        return HttpResponseContentType.values().find { it.extensions.contains(fileExtension) }
    }
}