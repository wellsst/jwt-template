package jwt.template

import auth.AuthCheck
import grails.rest.*
import grails.converters.*
import static org.springframework.http.HttpStatus.*

class UtilController extends BaseController {
	static responseFormats = ['json', 'xml']

    @AuthCheck
    def index() {
        try {
            User user = checkPermissions(getUserToken())

            respond "${user}"
        } catch (all) {
            log.error(all.message)
            render status: UNAUTHORIZED
        }
    }
}
