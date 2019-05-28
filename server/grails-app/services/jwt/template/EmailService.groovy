package jwt.template

import grails.gorm.transactions.Transactional
import grails.gsp.PageRenderer
import grails.web.mapping.LinkGenerator

@Transactional
class EmailService {

    def mailService

    PageRenderer groovyPageRenderer

    LinkGenerator grailsLinkGenerator

    def grailsApplication

    def sendLoginRequest(User user) {

        String serverURL = grailsApplication.config.getProperty("grails.serverURL")
        String loginLink = "${serverURL}/requestJWT/${user.registrationRequest.requestId}"
        log.info "NOT Yet Sending login request email: ${user.username} to ${loginLink}"

        // grailsLinkGenerator.link(controller: 'login', action: 'login', id: user.registrationRequest.requestId, absolute: true)
        /*def content = groovyPageRenderer.render view: '/emails/loginRequestEmail', model: [email: user.username, loginLink: loginLink]
        log.info content*/

        // Send email todo: figure why this wont send anymore: mail sender is not mime capable, try configuring a JavaMailSender
        /*sendMail {
            multipart true
            to user.username
            from "somwhere@gmail.com"
            subject "Registration"
            html view: "/emails/loginRequestEmail",
                    model: [email: user.username, uuid: user.registrationRequest.requestId]

        }*/
        loginLink
    }
}
