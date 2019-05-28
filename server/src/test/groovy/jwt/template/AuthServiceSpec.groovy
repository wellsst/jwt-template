package jwt.template

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class AuthServiceSpec extends Specification implements ServiceUnitTest<AuthService>, DataTest {

    RegistrationRequest registrationRequest

    def setupSpec() {
        mockDomain User
        mockDomain RegistrationRequest
    }

    def setup() {
        registrationRequest = new RegistrationRequest(requestId: "1", dateCreated: new Date())
        new User(username: "test@jwt.io", registrationRequest: registrationRequest).save(flush: true, failOnError: true)
    }

    def cleanup() {
    }

    List<Class> getDomainClasses() { [User, RegistrationRequest] }

    void "jwtFromRequestId - user clicks on email link"() {
        given:

        when:
        // the id is created on beforeInsert() so we need to fetch the gen'd one back here
        String jwtString = service.jwtFromRequestId(registrationRequest.requestId)

        then:
        jwtString

    }
}
