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
        String loginLink = "${serverURL}/requestJWT/${user.loginRequestId}"

        // grailsLinkGenerator.link(controller: 'login', action: 'login', id: user.loginRequestId, absolute: true)
        def content = groovyPageRenderer.render(view: '/emails/loginRequestEmail',
                model: [email: user.username, loginLink: loginLink])

        log.info "NOT Yet Sending login request email: ${user.username} (${user.loginRequestId})"
        log.info content
        // Send email todo: figure why this wont send anymore: mail sender is not mime capable, try configuring a JavaMailSender
        /*sendMail {
            multipart true
            to user.username
            from "websystemz@gmail.com"
            subject "Hello from CircuitShuffle"
            html view: "/emails/loginRequestEmail",
                    model: [email: user.username, uuid: user.loginRequestId]

        }*/
        loginLink
    }
}
