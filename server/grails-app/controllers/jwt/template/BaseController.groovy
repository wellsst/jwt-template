package jwt.template


import grails.rest.*
import grails.converters.*

class BaseController {
	static responseFormats = ['json', 'xml']

    AuthService authService

    protected String getUserToken() {
        request.getHeader("loginToken")
    }

    def checkPermissions(String token) {
        if (!token) {
            throw new Exception("No loginToken provided")
        }
        log.info "Check permissions: ${token}"
        /*User user = User.findByUsername(loginToken, [cache: true])
        if (!user) {
            throw new Exception("No user found for loginToken: ${loginToken}")
        }
        user */
        authService.loginFromJWT(token)
    }

    def index() { }
}
