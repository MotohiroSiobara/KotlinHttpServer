package http.response.header

import http.response.HttpResponse

enum class HttpResponseContentType(val contentType : String, val extensions : List<String>) {

    HTML("text/html;charset=utf-8", listOf(".html")),
    JS("application/javascript;charset=utf-8", listOf(".js")),
    CSS("text/css;charset=utf-8", listOf(".css")),
    JPEG("image/jpeg", listOf(".jpeg", ".jpg")),
    PNG("image/png", listOf(".png")),
    GIF("image/gif", listOf(".gif")),
    PDF("application/pdf", listOf(".pdf"))

    fun getString(): String {
        return "Content-Type: " + contentType
    }

    fun getContentType(File file) : HttpResponseContentType? {
        val fileExtension : String = FileHandler.getExtension(file)
    }
}