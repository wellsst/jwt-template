package jwt.template

import grails.gorm.transactions.Transactional

@Transactional
class BaseService {

    def grailsApplication

    def getAppConfigValue(String appKey, def defaultValue) {
        grailsApplication.config.getProperty("app.${appKey}") ?: defaultValue
    }
}
