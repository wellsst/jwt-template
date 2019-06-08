package jwt.template

import grails.gorm.transactions.Transactional

@Transactional
class EmailService extends BaseService {

     def mailService

     /*PageRenderer groovyPageRenderer
     LinkGenerator grailsLinkGenerator*/

    def sendLoginRequest(User user) {
        String serverURL = grailsApplication.config.getProperty("grails.serverURL")
        String loginLink = "${serverURL}/registerConfirm/${user.registrationRequest.requestId}"
        //log.info "NOT Yet Sending login request email: ${user.username} to ${loginLink}"

/*
        log.info loginLink
        log.info getAppConfigValue("name", "JWT Template")
        log.info user.username
        log.info user.registrationRequest.requestId
        log.info getAppConfigValue('cleanupRequestsOlderThanMinutes', 15)
*/

        // Send email
        sendMail {
            multipart true
            async true
            to user.username
            from getAppConfigValue("email.from", "jwt@localhost") as String
            subject getAppConfigValue("email.reg_request.subject", "Registration Request") as String
            html view: "/emails/registrationRequestEmail",
                    model: [email           : user.username,
                            appName         : getAppConfigValue("name", "JWT Template"),
                            loginLink       : loginLink,
                            expiresInMinutes: getAppConfigValue('cleanupRequestsOlderThanMinutes', 15)]
        }
        loginLink
    }
}
