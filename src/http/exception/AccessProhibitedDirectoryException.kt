package http.exception

/**
 * 公開されていないディレクトリを要求された際に発生させる例外クラス
 */
class AccessProhibitedDirectoryException(s: String) : Exception(s)
