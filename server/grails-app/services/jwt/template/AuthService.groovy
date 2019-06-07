package jwt.template

import auth.AuthException
import grails.gorm.transactions.Transactional
import groovy.time.TimeCategory
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys

import javax.crypto.SecretKey

@Transactional
class AuthService extends BaseService {

    EmailService emailService

    /* User wants to signup with the service, we gen a short lived key and send an email */

    def signupRequest(String userEmail) {
        // Lookup user?
        User user = User.findByUsername(userEmail)
        if (user) {
            // Already has a reg request?
            log.info "registerRequest: ${userEmail}, already exists"
            if (user.registrationRequest) {
                log.info "User ${userEmail} already exists with requestId: ${user.registrationRequest.requestId}"
                // Note: This allows the old request to be overridden, if you dont like this then uncomment and return a msg to the user
                return user
            }
        } else {
            user = new User(username: userEmail)
        }
        log.info "registerRequest: ${userEmail}, creating a new request"
        RegistrationRequest registrationRequest = new RegistrationRequest()
        registrationRequest.requestId = UUID.randomUUID() as String
        registrationRequest.challengeId = RegistrationRequest.generateChallengeId(getAppConfigValue("jwt.challengeKeyLength", 4) as Integer)
        registrationRequest.user = user
        registrationRequest.save(flush: true)
        user.registrationRequest = registrationRequest

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

    private String createJWTForUser(RegistrationRequest registrationRequest) {

        User requestingUser = registrationRequest.user// User.findByRegistrationRequest(registrationRequest)

        SecretKey key = Keys.hmacShaKeyFor((getAppConfigValue('jwt.key', '') as String).bytes)
        String jwt = Jwts.builder().
                setIssuer(getAppConfigValue("jwt.issuer", "jwt-template") as String).
                setSubject(requestingUser.username).
                setIssuedAt(new Date()).
                setExpiration(new Date() + (getAppConfigValue("jwt.daysToExpire", 365) as Integer)).
                signWith(key).compact()
        requestingUser.loginToken = jwt
        //registrationRequest.removeFromUser()
        //requestingUser.registrationRequest.delete()
        requestingUser.save(flush: true)
        jwt
    }

    /* User has clicked on the email link */

    def jwtFromRequestId(String loginRequestId, String challengeId) {
        RegistrationRequest registrationRequest = RegistrationRequest.findByRequestIdAndChallengeId loginRequestId, challengeId

        if (registrationRequest) {
            return createJWTForUser(registrationRequest)
        } else {
            String msg = "User signup attempt but no login loginToken found for ${loginRequestId} and challengeId: ${challengeId} "
            throw new AuthException(msg)
        }
    }

    def jwtFromRequestId(String loginRequestId) {
        RegistrationRequest registrationRequest = RegistrationRequest.findByRequestId loginRequestId

        if (registrationRequest) {
            return createJWTForUser(registrationRequest)
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

}
