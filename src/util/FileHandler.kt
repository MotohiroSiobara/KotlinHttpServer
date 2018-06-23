package util

import com.sun.prism.shader.DrawCircle_LinearGradient_REFLECT_AlphaTest_Loader
import http.exception.AccessProhibitedDirectoryException
import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files
import java.time.Instant
import java.time.ZonedDateTime

object FileHandler {

    val PUBLIC_DIR_PATH = File("public").absolutePath
    val BAD_REQUEST_FILE = File("$PUBLIC_DIR_PATH/400.html")
    val FORBIDDEN_FILE = File("$PUBLIC_DIR_PATH/403.html")
    val NOT_FOUND_FILE = File("$PUBLIC_DIR_PATH/404.html")
    val NOT_IMPLEMENTED_FILE = File("$PUBLIC_DIR_PATH/501.html")
    val HTTP_VERSION_NOT_SUPPORTED_FILE = File("$PUBLIC_DIR_PATH/505.html")

    @Throws(AccessProhibitedDirectoryException::class)
    fun loadFileFromPath(path : String): File {
        // fileパスの正規化
        val uri : URI = FileSystems.getDefault().getPath("public", path).toUri().normalize()

        return if (isValidUri(uri)) {
            File(uri)
        } else throw AccessProhibitedDirectoryException("publicディレクトリ以下のファイルにしかアクセスすることができません")
    }

    fun loadIndexHtml(path : String): File {
        return File(path + "/index.html")
    }

    @Throws(IOException::class)
    fun readFile(file : File): ByteArray {
        try {
            return Files.readAllBytes(file.toPath()) // fileをバイトで読み込む
        } catch (e : IOException) {
            throw IOException("File:" + file.getPath())
        }
    }

    fun lookupExtension(file : File): String {
        val filePath : String = file.getPath()
        return filePath.substring(filePath.lastIndexOf('.'))
    }

    fun lookupLastModifiedDateTime(file : File): ZonedDateTime {
        val instant : Instant = Instant.ofEpochMilli(file.lastModified())
        return ZonedDateTime.ofInstant(instant, RFC1123DateTimeHandler.GMT)
    }

    private fun isValidUri(uri: URI): Boolean {
        // uriのpathがPUBLIC_DIR_PATHで始まっているかどうか
        return uri.getPath().startsWith(PUBLIC_DIR_PATH)
    }
}