
package http.exception;

/**
 * 404 Not Foundを表現した例外クラス
 * Constructs a `AccessProhibitedDirectoryException` with the specified
 * detail message.
 *
 * @param   s   the detail message.
 */
class NotFoundException(s: String) : Exception(s)
