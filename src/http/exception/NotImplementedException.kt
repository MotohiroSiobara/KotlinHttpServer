package http.exception

/**
 * 実装されていないメソッドが要求された際に発生させる例外クラス
 * 501 Not Implementedを表現する
 */
class NotImplementedException(s: String) : Exception(s)
