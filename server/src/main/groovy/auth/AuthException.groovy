package auth

class AuthException extends Exception {

    AuthException() {
    }

    AuthException(String message) {
        super(message)
    }

    AuthException(String message, Throwable cause) {
        super(message, cause)
    }
}
