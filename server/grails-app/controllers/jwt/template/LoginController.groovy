package jwt.template

import grails.converters.JSON
import grails.util.Environment
import org.springframework.http.HttpStatus

class LoginController extends BaseController {
	static responseFormats = ['json', 'xml']
    String serverURL = grailsApplication.config.getProperty("grails.serverURL")
    def mailService
    AuthService authService

    def index() { }

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

    def registerRequest() {
        String emailAddress = request.JSON.emailAddress
        log.info "registerRequest: ${emailAddress}"
        try {
            User user = authService.signupRequest(emailAddress)
            int cleanupOlderThan = authService.getAppConfigValue('cleanupRequestsOlderThanMinutes', 15) as int
            render([challengeId: user.registrationRequest.challengeId, cleanupOlderThan: cleanupOlderThan] as JSON)
            //respond cleanupOlderThan, status: HttpStatus.OK
        } catch (all) {
            all.printStackTrace()
            respond status: HttpStatus.UNAUTHORIZED
        }
    }

    /* User has clicked on email link and calls this */
    def registerConfirm(String requestId) {
        log.info "registerConfirm: ${requestId}"
        try {
            if (Environment.current == Environment.DEVELOPMENT) {
                serverURL = "http://localhost:4200"
            }
            // todo: finish up the client side for this...it is created but finishe it with grabbing the requestId and asking for the challengeId
            redirect url: "${serverURL}/register-confirm?requestId=${requestId}"
        } catch (all) {
            log.error all.message
            render text: all.message, status: HttpStatus.NOT_FOUND
        }
    }

    /* user has entered challengeID from email etc and goes here to finalise the process */
    // todo: finish this
    def registerAccept(String requestId, String challengeId) {
        /*String requestId = request.JSON.requestId
        String challengeId = request.JSON.challengeId*/
        log.info "registerAccept...requestId: ${requestId}, challengeId: ${challengeId}"
        // log.info "${User.list()}"
        try {
            String jwtToken = authService.jwtFromRequestId(requestId)
            if (Environment.current == Environment.PRODUCTION) {
                redirect url: "${serverURL}/request-jwt?jwt=${jwtToken}"
            } else {
                redirect url: "http://localhost:4200/request-jwt?jwt=${jwtToken}"
            }
        } catch (all) {
            log.error all.message
            render text: all.message, status: HttpStatus.NOT_FOUND
        }
    }

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

    // Yea this is not security at all
    /*def login() {
        String username = request.JSON.username
        log.info("Login attempt: ${request.JSON}")

        def user = User.findByUsername(username)
        if (user) {
            def token = UUID.randomUUID().toString()
            // todo: create a real full JWT loginToken
            user.loginToken = token
            user.save()
            log.info("User logged in ${user}")
            respond token: token
        }
        *//* TODO: Check for expired, locked etc *//*
        else {
            log.warn("User login attempt ${username}, but user not found or password invalid, users are: ${User.list(max: 10)}")
            render text: "User login attempt ${username}, but user not found or password invalid", status: HttpStatus.UNAUTHORIZED
        }
    }*/
}
