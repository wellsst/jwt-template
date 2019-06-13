package jwt.template

import auth.AuthCheck
import grails.rest.*
import grails.converters.*
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys

import javax.crypto.SecretKey
import javax.xml.bind.DatatypeConverter

import static org.springframework.http.HttpStatus.*

class UtilController extends BaseController {
	static responseFormats = ['json', 'xml']

    def index() {

        // Generate a secret key we could use for the JWT
        log.info("Generate secret key...")
        String random = UUID.randomUUID().toString()

        // Store this in config
        String base64Key = DatatypeConverter.printBase64Binary(random.getBytes())
        byte[] secretBytes = DatatypeConverter.parseBase64Binary(base64Key)

        // use this in your app code
        SecretKey secretKey = Keys.hmacShaKeyFor(secretBytes)

        //def keyString = Keys.secretKeyFor(SignatureAlgorithm.HS256)
        log.info(base64Key)
        render base64Key
    }


    def genkey() {
        log.info("Generate secret key...")
        def keyString = Keys.secretKeyFor(SignatureAlgorithm.HS256).toString()
        log.info(keyString)
        render keyString
    }

    def listRegReqs() {
        try {
            User user = checkPermissions(getUserToken())
            List<RegistrationRequest> requests = RegistrationRequest.list()
            requests.each {
                log.info it.toString()
            }
            render requests
        } catch (all) {
            log.error(all.message)
            render status: UNAUTHORIZED
        }

    }

    def users() {
        try {
            User user = checkPermissions(getUserToken())
            List<User> users = User.list()
            users.each {
                log.info it.toString()
            }
            render users
        } catch (all) {
            log.error(all.message)
            render status: UNAUTHORIZED
        }
    }

    def removeAllTokens() {
        try {
            checkPermissions(getUserToken())

            List<User> users = User.list()
            users.each { user ->
                user.registrationRequest.delete()
            }
            render "Tokens deleted"
        } catch (all) {
            log.error(all.message)
            render status: UNAUTHORIZED
        }
    }
}
