package jwt.template

import grails.converters.JSON
import grails.util.Environment
import org.springframework.http.HttpStatus

class LoginController extends BaseController {
    static responseFormats = ['json', 'xml']
    String serverURL = grailsApplication.config.getProperty("grails.serverURL")
    AuthService authService

    def index() {}

    def show() {
        if (request.get) {
            respond "testing 123"
        }

        def u = User.findByUsername(params.login)
        println "user logging in is: ${params.login}"
        if (u) {
            /*if (u.password == params.password) {
                session.user = u
                redirect(action: "home")
            }
            else {
                render(view: "login", model: [message: "Password incorrect"])
            }*/
        } else {
            println "Rendering to login page..."
            render(view: "login", model: [message: "User not found"])
        }
    }

    /* User has requested to start the registration process */

    def registerRequest() {
        String remoteAddr = request.remoteAddr
        String emailAddress = request.JSON.emailAddress
        log.info "registerRequest: ${emailAddress}"
        try {
            int requestLimit = authService.getAppConfigValue('maxRequestsPerRemoteAddr', 3) as int
            if (RegistrationRequest.countByRequestRemoteAddr(remoteAddr) < requestLimit) {
                User user = authService.signupRequest(emailAddress, remoteAddr)
                int cleanupOlderThan = authService.getAppConfigValue('cleanupRequestsOlderThanMinutes', 15) as int
                render([challengeId: user.registrationRequest.challengeId, cleanupOlderThan: cleanupOlderThan] as JSON)
                //respond cleanupOlderThan, status: HttpStatus.OK
            } else {
                render text: "Too many requests from this address", status: HttpStatus.TOO_MANY_REQUESTS
            }
        } catch (all) {
            all.printStackTrace()
            respond status: HttpStatus.UNAUTHORIZED
        }
    }

    /* User has clicked on email link and calls this */

    def registerConfirm(String requestId) {
        log.info "registerConfirm: ${requestId}"
        int requestCount = RegistrationRequest.countByRequestId requestId
        if (requestCount == 1) {
            try {
                if (Environment.current == Environment.DEVELOPMENT) {
                    serverURL = "http://localhost:4200"
                }
                redirect url: "${serverURL}/register-confirm?requestId=${requestId}"
            } catch (all) {
                log.error all.message
                render text: all.message, status: HttpStatus.NOT_FOUND
            }
        } else {
            render text: "No registration request found for id: ${requestId}", status: HttpStatus.NOT_FOUND
        }
    }

    /* user has entered challengeID from email etc and goes here to finalise the process */

    def registerAccept() {
        String requestId = request.JSON.requestId
        String challengeId = request.JSON.challengeId
        log.info "registerAccept...requestId: ${requestId}, challengeId: ${challengeId}"
        try {
            String jwtToken = authService.jwtFromRequestId(requestId, challengeId)
            /*if (Environment.current == Environment.DEVELOPMENT) {
                serverURL = "http://localhost:4200"
            }
            def redirectTo = "${serverURL}/request-jwt" //?jwt=${jwtToken}"
            log.info "registerAccept Redirect: ${redirectTo}"
            redirect url: redirectTo //, params:[jwt:jwtToken]*/
            def token = ["jwt": jwtToken]
            respond token
        } catch (all) {
            // all.printStackTrace()
            log.error all.message
            def error = [message: all.message]
            render error as JSON, status: HttpStatus.BAD_REQUEST
        }
    }

    @Deprecated
    def loginWithJWT() {
        String jwtToken = request.JSON.jwtToken
        String email = request.JSON.username
        try {
            boolean accepted = authService.loginFromJWT(jwtToken, email)
            respond accepted
        } catch (all) {
            respond status: HttpStatus.NOT_FOUND
        }
    }
}
