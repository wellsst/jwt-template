package jwt.template

import auth.AuthException
import grails.gorm.transactions.Transactional
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys

import javax.crypto.SecretKey

@Transactional
class AuthService {

    EmailService emailService

    def grailsApplication

    /* User wants to signup with the service, we gen a short lived key and send an email */

    def signupRequest(String userEmail) {
        // Lookup user?
        User existingUser = User.findByUsername(userEmail)
        if (existingUser) {
            // Already has one?
            log.info "signupRequest: ${userEmail}, already exists"
            if (existingUser.loginRequestId) {
                log.info "User ${userEmail} already exists with login link: ${existingUser.loginRequestId}"
                return existingUser
            }
        } else {
            log.info "signupRequest: ${userEmail}, creating as new user"
            existingUser = new User(username: userEmail)
        }

        // Generate a link if not already one
        existingUser.loginRequestId = UUID.randomUUID().toString()
        existingUser.loginRequestedOn = new Date()

        if (!existingUser.validate()) {
            log.info "${existingUser.errors.allErrors}"
        }

        existingUser.save(flush: true)

        // Send email
        String loginLink = emailService.sendLoginRequest(existingUser)
        log.info "Login link: ${loginLink}, ${existingUser}"
        existingUser
    }

    /* User has clicked on the email link */

    def jwtFromRequestId(String loginRequestId) {
        User requestingUser = User.findByLoginRequestId(loginRequestId)
        if (requestingUser) {
            requestingUser.clearLoginRequest()

            //Key key = Keys.secretKeyFor(SignatureAlgorithm.RS256)
            SecretKey key = Keys.hmacShaKeyFor((grailsApplication.config.getProperty('jwtKey') as String).bytes);
            String jwt = Jwts.builder().
                    setIssuer("circuitShuffle").
                    setSubject(requestingUser.username).
                    setIssuedAt(new Date()).
                    setExpiration(new Date() + 365).
                    signWith(key).compact()
            requestingUser.token = jwt
            requestingUser.save(flush: true)
            return jwt
        } else {
            String msg = "User signup attempt but no login token found for ${loginRequestId}"
            throw new AuthException(msg)
        }
    }

    def loginFromJWT(String token) {
        /*grailsApplication.config.getProperty('jwtKey')
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256)*/
        SecretKey key = Keys.hmacShaKeyFor((grailsApplication.config.getProperty('jwtKey') as String).bytes);
        /*if (Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject().equals(email)) {
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
                throw new AuthException("No user found for token: ${username}")
            }
            return user
        } catch (JwtException e) {
            throw new AuthException()
        }
    }

    def login(String userEmail, String loginRequest) throws AuthException {

        def user = User.findByUsernameAndLoginRequestId(username, loginRequest)
        if (user) {
            def token = UUID.randomUUID().toString()
            // todo: create a real full JWT token
            user.token = token
            user.save()
            log.info("User logged in ${user}")
            user
        }
        /* TODO: Check for expired, locked etc */
        else {
            def msg = "User login attempt ${userEmail} or no login token found, but user not found, users are: ${User.list(max: 10)}"
            log.warn(msg)
            throw new AuthException(msg)
        }
    }

    /* todo: setup a schedule */

    def cleanupOldRequests() {
        Date removeLoginRequestsOlderThan = new Date()
        use(TimeCategory) {
            removeLoginRequestsOlderThan - 15.minutes
        }

        List<User> cleanupList = User.findAllByLoginRequestedOnLessThan(removeLoginRequestsOlderThan)
        cleanupList.each { userToClean ->
            log.info "Cleaning old login request for user ${userToClean.username}"
            userToClean.clearLoginRequest()
        }
    }
}
