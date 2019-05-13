package jwt.template


import grails.rest.*
import grails.converters.*

class BaseController {
	static responseFormats = ['json', 'xml']

    AuthService authService

    protected String getUserToken() {
        request.getHeader("token")
    }

    def checkPermissions(String token) {
        if (!token) {
            throw new Exception("No token provided")
        }
        log.info "Check permissions: ${token}"
        /*User user = User.findByUsername(token, [cache: true])
        if (!user) {
            throw new Exception("No user found for token: ${token}")
        }
        user */
        authService.loginFromJWT(token)
    }

    def index() { }
}
