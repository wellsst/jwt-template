package jwt.template

import auth.AuthException
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import groovy.time.TimeCategory
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys

import javax.crypto.SecretKey

// @GrailsCompileStatic
@Transactional(readOnly = true)
class AuthService extends BaseService {

    EmailService emailService



    /* User wants to signup with the service, we gen a short lived key and send an email */

    def signupRequest(String userEmail) {
        // Lookup user?
        User user = User.findByUsername(userEmail)
        if (user) {
            // Already has one?
            log.info "signupRequest: ${userEmail}, already exists"
            if (user.registrationRequest.requestId) {
                log.info "User ${userEmail} already exists with login link: ${user.registrationRequest.requestId}"
                return user
            }
        } else {
            log.info "signupRequest: ${userEmail}, creating as new user"
            def registrationRequest = new RegistrationRequest()
            registrationRequest.requestId = UUID.randomUUID() as String
            registrationRequest.challengeId = RegistrationRequest.generateChallengeId(getAppConfigValue("jwt.challengeKeyLength", 4) as Integer)
            user = new User(username: userEmail, registrationRequest: registrationRequest)
        }

        // Dev only: Just check for errors
        if (!user.validate()) {
            log.info "${user.errors.allErrors}"
        }

        user.save(flush: true)

        // Send email
        String loginLink = emailService.sendLoginRequest(user)
        log.info "Login link: ${loginLink}, ${user}"
        user
    }

    /* User has clicked on the email link */

    def jwtFromRequestId(String loginRequestId) {
        RegistrationRequest registrationRequest = RegistrationRequest.findByRequestId loginRequestId

        if (registrationRequest) {
            User requestingUser = registrationRequest.user

            SecretKey key = Keys.hmacShaKeyFor((getAppConfigValue('jwt.key', '') as String).bytes)
            String jwt = Jwts.builder().
                    setIssuer(getAppConfigValue("jwt.issuer", "jwt-template") as String).
                    setSubject(requestingUser.username).
                    setIssuedAt(new Date()).
                    setExpiration(new Date() + (getAppConfigValue("jwt.daysToExpire", 365) as Integer)).
                    signWith(key).compact()
            requestingUser.loginToken = jwt
            requestingUser.save(flush: true)

            requestingUser.registrationRequest.delete()
            /* requestingUser.registrationRequest = null
*/
            return jwt
        } else {
            String msg = "User signup attempt but no login loginToken found for ${loginRequestId}"
            throw new AuthException(msg)
        }
    }

    def loginFromJWT(String token) {
        /*grailsApplication.config.getProperty('jwtKey')
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256)*/
        SecretKey key = Keys.hmacShaKeyFor((getAppConfigValue('jwt.key', '') as String).bytes);
        /*if (Jwts.parser().setSigningKey(key).parseClaimsJws(loginToken).getBody().getSubject().equals(email)) {
            return
        } else {
            throw new AuthException()
        }*/

        try {

            def claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            //OK, we can trust this JWT
            String username = claims.getBody().getSubject()
            User user = User.findByUsername(username, [cache: true])
            if (!user) {
                throw new AuthException("No user found for loginToken: ${username}")
            }
            return user
        } catch (JwtException e) {
            throw new AuthException()
        }
    }

    def login(String userEmail, String loginRequest) throws AuthException {
        def query = User.where {
            username == username &&
                    registrationRequest.requestId == loginRequest
        }
        def user = query.find()
        if (user) {
            def token = UUID.randomUUID().toString()
            // todo: create a real full JWT loginToken
            user.loginToken = token
            user.save()
            log.info("User logged in ${user}")
            user
        }
        /* TODO: Check for expired, locked etc */
        else {
            def msg = "User login attempt ${userEmail} or no login loginToken found, but user not found, users are: ${User.list(max: 10)}"
            log.warn(msg)
            throw new AuthException(msg)
        }
    }

    /* todo: setup a schedule */

    def cleanupOldRequests() {
        Date removeLoginRequestsOlderThan = new Date()
        use(TimeCategory) {
            removeLoginRequestsOlderThan - 15.minutes // todo: move to config
        }

        List<RegistrationRequest> cleanupList = RegistrationRequest.findAllByDateCreatedLessThan(removeLoginRequestsOlderThan)
        RegistrationRequest.deleteAll(cleanupList)
    }
}
