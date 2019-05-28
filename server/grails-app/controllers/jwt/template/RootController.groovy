package jwt.template

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys

import javax.crypto.SecretKey
import javax.xml.bind.DatatypeConverter

class RootController {
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
       // redirect(uri:"/index.html")
    }


    def genkey() {
        log.info("Generate secret key...")
        def keyString = Keys.secretKeyFor(SignatureAlgorithm.HS256).toString()
        log.info(keyString)
        render keyString
    }

    def listRegReqs() {
        List<RegistrationRequest> requests = RegistrationRequest.list()
        requests.each {
            log.info it.toString()
        }
        render requests
    }
    def users() {
        List<User> users = User.list()
        users.each {
            log.info it.toString()
        }
        render users
    }

}
