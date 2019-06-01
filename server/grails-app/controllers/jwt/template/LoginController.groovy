package jwt.template

import grails.util.Environment
import org.springframework.http.HttpStatus

class LoginController {
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

    def signupRequest() {
        String emailAddress = request.JSON.emailAddress
        log.info "signupRequest: ${emailAddress}"
        try {
            User user = authService.signupRequest(emailAddress)
            respond user.registrationRequest, status: HttpStatus.OK
        } catch (all) {
            all.printStackTrace()
            respond status: HttpStatus.UNAUTHORIZED
        }
    }

    /* User has clicked on email link and calls this */

    def requestJWT(String requestId) {
        //String requestId = request.JSON.requestId
        log.info "requestJWT: ${requestId}"
        // log.info "${User.list()}"
        try {
            String jwtToken = authService.jwtFromRequestId(requestId)
            // render text: jwtToken
            if (Environment.current == Environment.PRODUCTION) {
                redirect url: "${serverURL}/#/request-jwt?jwt=${jwtToken}"
            } else {
                redirect url: "http://localhost:4200/request-jwt?jwt=${jwtToken}"
            }
        } catch (all) {
            all.printStackTrace()
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
    def login() {
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
        /* TODO: Check for expired, locked etc */
        else {
            log.warn("User login attempt ${username}, but user not found or password invalid, users are: ${User.list(max: 10)}")
            render text: "User login attempt ${username}, but user not found or password invalid", status: HttpStatus.UNAUTHORIZED
        }
    }

    /*def signup() {
        String emailAddress = request.JSON.emailAddress
        log.info "Signup request from user: ${emailAddress}"

        User user = User.findByUsername(emailAddress)
        if (user) {
            respond text: "User already exists, try just logging in.", status: HttpStatus.IM_USED
        } else {
            def wordMap = servletContext["wordMap"]
            String password = generatePassphrase(wordMap)[0]

            User newUser = new User(username: emailAddress, password: password)
            def loginToken = UUID.randomUUID().toString()
            // todo: create a real full JWT loginToken
            newUser.loginToken = loginToken
            newUser.save(flush: true)
            Role userRole = Role.findOrSaveByAuthority("ROLE_USER")
            UserRole.create(newUser, userRole, true)

            def ctx = startAsync()
            ctx.start {
                // Long running task
                mailService.sendMail {
                    to emailAddress
                    from "websystemz@gmail.com"
                    subject "Hello from CircuitShuffle"
                    // html view: "/emails/html-hello", model: [param1: "value1", param2: "value2"]
                    html """
                    <p>Congrats!  You have signed-up to CircuitShuffle, your password is: <b>${password}</b><p>
                        <p></p>
                        <p>We suggest changing this password ASAP</p>
                        <p></p>
                        <p><a href='${grailsApplication.config.grails.serverURL}/login'>Login:  using your email address</a></p>
                        <p></p>
                        <p>Happy shuffling!</p>
                        <p></p>
                        <p>The CircuitShuffle team</p>
                        
                    """
                }
                ctx.complete()
            }
            *//*p.onError { Throwable err ->
                println "An error occured ${err.message}"
            }
            p.onComplete { result ->
                println "Promise returned $result"
            }*//*

            *//*Map response = [username: emailAddress, password:password]
            respond response*//*
            respond loginToken: loginToken
        }

    }*/
}
