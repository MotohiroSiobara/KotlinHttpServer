package http.response.body

import util.FileHandler
import java.io.File
import java.io.IOException

object HttpResponseBodyFactory {

    @Throws(IOException::class)
    fun createFromFile(file : File): HttpResponseBody {
        val bytes = FileHandler.readFile(file)
        return createFromByte(bytes)
    }

    fun createBlankByte(): HttpResponseBody {
        return createFromByte(ByteArray(0))
    }

    fun createInternalServerError(): HttpResponseBody {
        return createFromByte(createInternalServerErrorHTML().toByteArray())
    }

    @Throws(IOException::class)
    fun createFileListHtml(file: File): HttpResponseBody {
        if (!file.isDirectory()) {
            throw IllegalAccessError()
        }

        val path = file.path
        val htmlBuilder = StringBuilder("<h1>Index of ${path}</h1><ul>")
        val fileList = file.list()
        if (fileList is Array) {
            fileList.forEach { createLiTagAndATag(path, it) }
            htmlBuilder.append("</ul>")
            return createFromByte(htmlBuilder.toString().toByteArray())
        }

        throw IOException("file.list()の部分でI/O errorが発生")
    }

    private fun createLiTagAndATag(path: String, fileName: String): String {
        if (fileName[0] == '.') {
            return ""
        }

        return if (path == "/") {
            "<li><a href=/$fileName >$fileName<a/></li>"
        } else {
            "<li><a href=$path/$fileName >$fileName<a/></li>"
        }
    }

    private fun createInternalServerErrorHTML(): String {
        return ("""
                <!doctype html>\n
                  <html lang='en'>\n
                  <head>\n
                    <meta charset='UTF-8'>\n
                    <title>500 Internal Server Error</title>\n
                  </head>\n
                  <body>\n
                    <h1>500 Internal Server Error</h1>\n
                  </body>\n
                </html>""")
    }

    private fun createFromByte(bytes: ByteArray): HttpResponseBody {
        return HttpResponseBody(bytes)
    }
}